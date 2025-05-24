package top.frankxxj.codeai.midware.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import top.frankxxj.codeai.midware.web.dtos.ChatDTO;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ModelSwitcher modelSwitcher;
    private final Advisor messageChatMemoryAdvisor;

    public Flux<String> codeGeneration(String modelName, ChatDTO body) {
        var client = modelSwitcher.getClient(modelName);
        if (body.conversationId() == null) {
            return client.prompt()
                    .user(body.userPrompt())
                    .stream().content();
        } else {
            return client.prompt()
                    // add messageChatMemoryAdvisor to the client
                    .advisors(messageChatMemoryAdvisor)
                    .advisors(advisorSpec -> {
                        // set the conversationId to the advisor
                        advisorSpec.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, body.conversationId());
                    })
                    .user(body.userPrompt())
                    .stream().content();
        }
    }

    public String codeGenerationTest(String modelName) {
        var client = modelSwitcher.getClient(modelName);
        return client.prompt().user("Give me a joke.").call().content();
    }

    public Flux<String> codeFixing(String modelName, String userPrompt) {
        var client = modelSwitcher.getClient(modelName);
        return client.prompt().user(userPrompt).stream().content();
    }
}
