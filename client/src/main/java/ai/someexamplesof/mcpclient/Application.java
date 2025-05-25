package ai.someexamplesof.mcpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {
    
    public static void main(String... args) throws Exception {
        SpringApplication.run(Application.class,args);
    }

    @Autowired
    private List<McpAsyncClient> mcpAsyncClients;  // For async client

    @Bean
    public List<McpFunctionCallback> functionCallbacks(McpAsyncClient mcpClient) {
        return mcpClient.listTools(null)
            .tools()
            .stream()
            .map(tool -> new McpFunctionCallback(mcpClient, tool))
            .toList();
    }    

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, List<McpFunctionCallback> functionCallbacks) {
        return chatClientBuilder
            .defaultFunctions(functionCallbacks)
            .build();
    }    
}
