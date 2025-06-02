package ai.someexamplesof.mcpclient.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ai.someexamplesof.mcpclient.service.ConfigurationRegister;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/app")
public class ApplicationEndpoint {

    private static final Log logger = LogFactory.getLog(ApplicationEndpoint.class);

    @Autowired
    ConfigurationRegister configurationRegister;

    @Autowired
    private ChatClient chatClient;    
    
    // @PostMapping(value="/kubeconfig",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    // public Mono<ResponseEntity<Void>> uploadKubeConfig(@RequestPart("file") FilePart configFile) throws Exception {
    //     logger.info("invoked the /app/kubeconfig endpoint " + configFile.filename());

    //     return Mono.fromCallable(() -> {
    //         Path tempDir = Files.createTempDirectory("config-upload");
    //         Path destination = tempDir.resolve(configFile.filename());
    //         //register
    //         configurationRegister.add(configFile.filename(),destination.toString());
    //         //save
    //         return destination;
    //     })
    //     .flatMap(destination -> configFile.transferTo(destination)
    //         .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build())));
    // }

    @PostMapping(value="/kubeconfig", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadKubeConfig(@RequestParam("file") MultipartFile configFile) throws IOException {
        // logger.info("invoked the /app/kubeconfig endpoint {}", configFile.getOriginalFilename());
        logger.info("invoked the /app/kubeconfig endpoint " + configFile.getOriginalFilename());

        try {
            Path tempDir = Files.createTempDirectory("config-upload");
            Path destination = tempDir.resolve(configFile.getOriginalFilename());
            configFile.transferTo(destination); // This throws IOException if there's a problem
            configurationRegister.add(configFile.getOriginalFilename(), destination.toString());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Error uploading kubeconfig file: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Or a more specific error code
        }
    }    

    // public Mono<String> check(@PathVariable("kubeconfig_name") String filename) throws Exception {

    @GetMapping("/status/{kubeconfig_name}")
    public String check(@PathVariable("kubeconfig_name") String filename) throws Exception {
        //now send
        // return Mono.fromCallable(() -> {
        //     String prompt = "You are an expert Kubernetes Administrator.  Are there any pods in error in the cluster? kubeconfig=";
        //     //get the config
        //     String kubeconfig = configurationRegister.getContent(filename);
        //     prompt += kubeconfig;
        //     return chatClient.prompt(prompt)
        //         .call()
        //         .content();
        // });
        // return Mono.just(chatClient.prompt(prompt)
        //     .call()
        //     .content());//put it in the mono
        String prompt = "You are an expert Kubernetes Administrator.  Are there any pods in error in the cluster? kubeconfig=";
        //get the config
        String kubeconfig = configurationRegister.getContent(filename);
        prompt += kubeconfig;
        return chatClient.prompt(prompt).call().content();

    }

}
