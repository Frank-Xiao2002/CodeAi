package top.frankxxj.codeai.midware.web;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import top.frankxxj.codeai.midware.ai.conversation.ConversationService;
import top.frankxxj.codeai.midware.user.AppUser;
import top.frankxxj.codeai.midware.user.security.SecurityUser;

import java.util.List;

@RestController
@RequestMapping("/api/conversation")
@RequiredArgsConstructor
class ConversationController {
    private final ConversationService conversationService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/create")
    public ResponseEntity<String> createConversation(Authentication auth) {
        AppUser user = ((SecurityUser) userDetailsService.loadUserByUsername(auth.getName())).getUser();
        String id = conversationService.createConversationId(user);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConversationById(@PathVariable("id") String conversationId) {
        conversationService.deleteConversationById(conversationId);
        return ResponseEntity.ok("");
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Message>> loadConversationById(Authentication auth, @PathVariable("id") String conversationId) {
        AppUser user = ((SecurityUser) userDetailsService.loadUserByUsername(auth.getName())).getUser();
        if (conversationId.split("-")[0].equals(String.valueOf(user.getId()))) {
            var conversations = conversationService.loadConversationById(conversationId);
            return ResponseEntity.ok(conversations);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
