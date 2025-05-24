package top.frankxxj.codeai.midware.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import top.frankxxj.codeai.midware.ai.ChatService;
import top.frankxxj.codeai.midware.ai.ModelSwitcher;
import top.frankxxj.codeai.midware.web.dtos.ChatDTO;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Log4j2
class ChatController {
    private final ModelSwitcher modelSwitcher;
    private final ChatService chatService;

    @PostMapping("/generate")
    public Flux<String> generate(@RequestHeader("Codeai-model") String modelName, @RequestBody ChatDTO body) {
        return chatService.codeGeneration(modelName, body);
    }

    @PostMapping("/generate/test")
    public String generateTest(@RequestHeader("Codeai-model") String modelName, @RequestBody ChatDTO body) {
        log.warn("body: {}", body);
        var client = modelSwitcher.getClient(modelName);
        return client.prompt().user("Give me a joke.").call().content();
    }

    @PostMapping("/fix")
    public Flux<String> fix(@RequestHeader("Codeai-model") String modelName, @RequestBody ChatDTO body) {
        return modelSwitcher.getClient(modelName)
                .prompt().user(body.userPrompt()).stream().content();
    }

    @PostMapping("/complete")
    public Flux<String> complete(@RequestHeader("Codeai-model") String modelName, @RequestBody ChatDTO body) {
        return modelSwitcher.getClient(modelName)
                .prompt().user(body.userPrompt()).stream().content();
    }
}
