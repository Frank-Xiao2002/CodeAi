package top.frankxxj.codeai.midware.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import top.frankxxj.codeai.midware.ai.conversation.ConversationService;
import top.frankxxj.codeai.midware.user.AppUser;
import top.frankxxj.codeai.midware.user.security.SecurityUser;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/load")
    public ResponseEntity<Map<String, List<String>>> loadConversations(Authentication auth) {
        AppUser user = ((SecurityUser) userDetailsService.loadUserByUsername(auth.getName())).getUser();
        var conversations = conversationService.loadConversationByUser(user);
        return ResponseEntity.ok(conversations);
    }

}
