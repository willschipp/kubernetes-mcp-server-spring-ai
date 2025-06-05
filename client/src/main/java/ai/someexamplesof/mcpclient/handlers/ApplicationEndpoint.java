package ai.someexamplesof.mcpclient.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ai.someexamplesof.mcpclient.service.ConfigurationRegister;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api")
public class ApplicationEndpoint {

    private static final Log logger = LogFactory.getLog(ApplicationEndpoint.class);

    @Autowired
    ConfigurationRegister configurationRegister;

    @Autowired
    private ChatClient chatClient;    

    @Autowired
    ToolCallbackProvider toolCallbackProvider;

    @Value("${prompt:You are an expert Kubernetes Administrator.  Are there any pods in error in the cluster? kubeconfig=}")
    private String basePrompt;

    /**
     * simple endpoint to allow uploading of a kubeconfig so the tool can connect to a specific cluster
     * @param configFile
     * @return
     * @throws IOException
     */
    @PostMapping(value="/kubeconfig", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadKubeConfig(@RequestParam("file") MultipartFile configFile) throws IOException {
        logger.info("invoked the /app/kubeconfig endpoint " + configFile.getOriginalFilename());

        try {
            Path tempDir = Files.createTempDirectory("config-upload");
            Path destination = tempDir.resolve(configFile.getOriginalFilename());
            configFile.transferTo(destination); 
            configurationRegister.add(configFile.getOriginalFilename(), destination.toString());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Error uploading kubeconfig file: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }    

    /**
     * This is the guts of it --> asking if there are any issues in the cluster!
     * @param filename
     * @return
     * @throws Exception
     */
    // @GetMapping("/status/{kubeconfig_name}")
    public String check(@PathVariable("kubeconfig_name") String filename) throws Exception {
        // String prompt = "You are an expert Kubernetes Administrator.  Are there any pods in error in the cluster? kubeconfig=";
        String prompt = basePrompt;
        //get the config
        String kubeconfig = configurationRegister.getContent(filename);
        prompt += kubeconfig;
        return chatClient.prompt(prompt)
            .call() //tools have been automatically added from the MCP server
            .content();
    }

    @GetMapping("/status/{kubeconfig_name}")
    public Mono<String> checkForErrors(@PathVariable("kubeconfig_name") String configName) throws Exception {
        return Mono.fromCallable(() -> {
            String prompt = basePrompt;
            //get the config
            String kubeconfig = configurationRegister.getContent(configName);
            prompt += kubeconfig;
            return chatClient.prompt(prompt)
                .call() //tools have been automatically added from the MCP server
                .content();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * helper function to return what tools it can see
     * @return
     */
    // @GetMapping("/tools")
    public List<Map<String, String>> getNames() {
        List<Map<String, String>> results = new ArrayList<>();
        for (ToolCallback tool : toolCallbackProvider.getToolCallbacks()) {
            Map<String, String> map = new HashMap<>();
            map.put("name", tool.getToolDefinition().name()); //pull out the name
            map.put("description", tool.getToolDefinition().description()); //pull out the description --> these would be sent to the LLM
            results.add(map);
        }
        return results;
    }  

    @GetMapping("/tools")
    public Flux<Map<String,String>> getTools() {
        return Flux.fromArray(toolCallbackProvider.getToolCallbacks()).map(tool -> {
            Map<String, String> map = new HashMap<>();
            map.put("name", tool.getToolDefinition().name()); //pull out the name
            map.put("description", tool.getToolDefinition().description()); //pull out the description --> these would be sent to the LLM
            return map;            
        });
    }
}
