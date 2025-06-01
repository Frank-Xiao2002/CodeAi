package top.frankxxj.codeai.midware.ai.conversation;

import org.springframework.ai.chat.messages.Message;
import top.frankxxj.codeai.midware.user.AppUser;

import java.util.List;
import java.util.Map;

public interface ConversationService {
    /**
     * Create a new conversation for a user.
     *
     * @param user the user object {@link AppUser}
     * @return id of the new conversation
     */
    String createConversationId(AppUser user);

    void deleteConversationById(String conversationId);

    Map<String, List<String>> loadConversationByUser(AppUser user);

    List<Message> loadConversationById(String conversationId);
}
