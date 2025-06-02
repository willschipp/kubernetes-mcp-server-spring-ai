package ai.someexamplesof.mcpclient.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api")
public class ToolEndpoint {
    
    @Autowired
    ToolCallbackProvider toolCallbackProvider;

    // @GetMapping("/tools")
    // public Mono<List<Map<String,String>>> getNames() throws Exception {
    //     return Mono.fromCallable(() -> {
    //         List<Map<String,String>> results = new ArrayList<>();
    //         for (ToolCallback tool : toolCallbackProvider.getToolCallbacks()){
    //             Map<String,String> map = new HashMap<>();
    //             map.put("name",tool.getToolDefinition().name());
    //             map.put("description",tool.getToolDefinition().description());
    //             results.add(map);
    //         } //end loop
    //         return results; //return
    //     }).subscribeOn(Schedulers.boundedElastic());
    // }

    @GetMapping("/tools")
    public List<Map<String, String>> getNames() {
        List<Map<String, String>> results = new ArrayList<>();
        for (ToolCallback tool : toolCallbackProvider.getToolCallbacks()) {
            Map<String, String> map = new HashMap<>();
            map.put("name", tool.getToolDefinition().name());
            map.put("description", tool.getToolDefinition().description());
            results.add(map);
        }
        return results;
    }    

}
