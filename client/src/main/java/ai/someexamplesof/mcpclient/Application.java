package ai.someexamplesof.mcpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;


@SpringBootApplication
public class Application {

    private static final Log logger = LogFactory.getLog(Application.class);
    
    public static void main(String... args) throws Exception {
        SpringApplication.run(Application.class,args);
    }

    // @Bean
    // public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
    //     logger.info(toolCallbackProvider.getToolCallbacks()[0].getToolDefinition()); //quick dump
    //     //build a list of names
    //     List<String> names = new ArrayList<>();
    //     for (ToolCallback toolCallback : toolCallbackProvider.getToolCallbacks()) {
    //         names.add(toolCallback.getToolDefinition().name());
    //     }
    //     return chatClientBuilder
    //         .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
    //         .defaultToolNames(names.toArray(new String[names.size()]))
    //         .defaultAdvisors(new SimpleLoggerAdvisor())
    //         .build();
    // }    
}
