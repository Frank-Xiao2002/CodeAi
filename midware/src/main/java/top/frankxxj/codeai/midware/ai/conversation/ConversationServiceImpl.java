package top.frankxxj.codeai.midware.ai.conversation;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.content.Content;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import top.frankxxj.codeai.midware.user.AppUser;
import top.frankxxj.codeai.midware.user.security.SecurityUser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public String createConversationId(AppUser user) {
        return String.format("%s-%s", user.getId(), System.currentTimeMillis());
    }

    @Override
    public void deleteConversationById(String conversationId) {
        Long id = ((SecurityUser) userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName())).getUser().getId();
        if (conversationId.split("-")[0].equals(String.valueOf(id))) {
            jdbcChatMemoryRepository.deleteByConversationId(conversationId);
            log.debug("Deleted conversation with ID: {}", conversationId);
        }
    }

    @Override
    public Map<String, List<String>> loadConversationByUser(AppUser user) {
        return jdbcChatMemoryRepository.findConversationIds().stream()
                .filter(conversationId -> conversationId.split("-")[0].equals(String.valueOf(user.getId())))
                .collect(Collectors.toMap(
                        conversationId -> conversationId,
                        conversationId -> jdbcChatMemoryRepository.findByConversationId(conversationId)
                                .stream()
                                .map(Content::getText) // Assuming `getText()` retrieves the message content
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public List<Message> loadConversationById(String conversationId) {
        return jdbcChatMemoryRepository.findByConversationId(conversationId);
    }

}
