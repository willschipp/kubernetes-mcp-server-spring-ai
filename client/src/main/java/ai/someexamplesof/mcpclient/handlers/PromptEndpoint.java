package ai.someexamplesof.mcpclient.handlers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ai.someexamplesof.mcpclient.tools.DateTimeTools;

@RestController
@RequestMapping("/ai")
public class PromptEndpoint {


    @Autowired
    private ChatClient chatClient;

    @GetMapping("/ask")
    public String ask(@RequestParam String prompt) {
        //dump the chat client
        return chatClient.prompt(prompt)
            // .tools(new DateTimeTools()) //adding tools
            .call()
            .content();
    }
}
