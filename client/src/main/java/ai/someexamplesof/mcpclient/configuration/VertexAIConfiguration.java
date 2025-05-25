package ai.someexamplesof.mcpclient.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VertexAIConfiguration {

    private static final Log logger = LogFactory.getLog(VertexAIConfiguration.class);
    
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {

        logger.info("building from the vertex builder");
        //build a vertex specific client
        return chatClientBuilder.defaultOptions(VertexAiGeminiChatOptions.builder()
                .model(VertexAiGeminiChatModel.ChatModel.GEMINI_1_5_FLASH)
                .toolCallbacks(toolCallbackProvider.getToolCallbacks())
                .build())
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();
    }

}
