package ai.someexamplesof.mcpclient.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * class that builds a specific configuration for Google VertexAI
 */
@Configuration
public class VertexAIConfiguration {
    
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
        //build a vertex specific client
        return chatClientBuilder.defaultOptions(VertexAiGeminiChatOptions.builder()
                .model(VertexAiGeminiChatModel.ChatModel.GEMINI_2_0_FLASH_LIGHT) //specific model selection
                .toolCallbacks(toolCallbackProvider.getToolCallbacks()) //pass the tools discovered from the MCP server
                .build())
            .defaultAdvisors(new SimpleLoggerAdvisor()) //simple logger of the prompt
            .build();
    }

}
