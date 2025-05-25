package ai.someexamplesof.mcpclient.handlers;

@RestController
@RequestMapping("/ai")
public class PromptEndpoint {


    @Autowired
    private ChatClient chatClient;

    @GetMapping("/ask")
    public String ask(@RequestParam String prompt) {
        return chatClient.prompt(prompt)
            .call()
            .content();
    }
}
