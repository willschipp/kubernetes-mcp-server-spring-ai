package ai.someexamplesof.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ai.someexamplesof.mcp.tools.Events;
import ai.someexamplesof.mcp.tools.General;


@SpringBootApplication
public class Application {

    public static void main(String... args) throws Exception {
        SpringApplication.run(Application.class,args);
    }


	/**
	 * expose the event tools
	 * @param events
	 * @return
	 */
    @Bean
	public ToolCallbackProvider eventTools(Events events) {
		return MethodToolCallbackProvider.builder().toolObjects(events).build();
	}

	/**
	 * expose the general tools
	 * @param general
	 * @return
	 */
    @Bean
	public ToolCallbackProvider generalTools(General general) {
		return MethodToolCallbackProvider.builder().toolObjects(general).build();
	}


}