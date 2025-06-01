package top.frankxxj.codeai.midware.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import top.frankxxj.codeai.midware.ai.ChatService;
import top.frankxxj.codeai.midware.ai.ModelSwitcher;
import top.frankxxj.codeai.midware.user.security.SecurityUser;
import top.frankxxj.codeai.midware.web.dtos.ChatDTO;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Log4j2
class ChatController {
    private final ModelSwitcher modelSwitcher;
    private final ChatService chatService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestHeader("Codeai-model") String modelName, @RequestBody ChatDTO body,
                                           Authentication auth) {
        String username = auth.getName();
        var appUser = ((SecurityUser) userDetailsService.loadUserByUsername(username)).getUser();
        if (Long.parseLong(body.conversationId().split("-")[0]) == appUser.getId()) {
            return ResponseEntity.ok(chatService.codeGeneration(modelName, body));
        } else {
            //return Flux.error(new IllegalArgumentException("Conversation ID does not match user ID."));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/generate/test")
    public String generateTest(@RequestHeader("Codeai-model") String modelName, @RequestBody ChatDTO body) {
        log.warn("body: {}", body);
        var client = modelSwitcher.getClient(modelName);
        return client.prompt().user("讲一个笑话").call().content();
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
