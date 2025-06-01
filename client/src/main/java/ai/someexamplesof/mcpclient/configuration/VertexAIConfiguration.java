package ai.someexamplesof.mcpclient.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class VertexAIConfiguration {

    private static final Log logger = LogFactory.getLog(VertexAIConfiguration.class);
    
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {

        logger.info("building from the vertex builder");
        Set<String> names = new HashSet<>();
        // //get the tool names
        for (ToolCallback callBack : toolCallbackProvider.getToolCallbacks()) {
            logger.info(callBack.getToolMetadata().toString());
            logger.info(callBack.getToolDefinition().toString());


            // logger.info(callBack.getToolDefinition().name());
            // names.add(callBack.getToolDefinition().name());            
        } //end for
        //build a vertex specific client
        return chatClientBuilder.defaultOptions(VertexAiGeminiChatOptions.builder()
                .model(VertexAiGeminiChatModel.ChatModel.GEMINI_2_0_FLASH_LIGHT)
                .toolCallbacks(toolCallbackProvider.getToolCallbacks())
                // .toolNames(names)
                .build())
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();
    }

}
