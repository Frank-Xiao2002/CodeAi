package top.frankxxj.codeai.midware.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import top.frankxxj.codeai.midware.web.dtos.ChatDTO;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ModelSwitcher modelSwitcher;
    private final Advisor messageChatMemoryAdvisor;
    private final PromptService promptService;

    public String codeGeneration(String modelName, ChatDTO body) {
        var client = modelSwitcher.getClient(modelName);
        if (body.conversationId() == null) {
            return client.prompt()
                    .user(body.userPrompt())
                    .call().content();
        } else {
            return client.prompt(promptService.buildUserPrompt(body))
                    // add messageChatMemoryAdvisor to the client
                    .advisors(messageChatMemoryAdvisor)
                    .advisors(advisorSpec -> {
                        // set the conversationId to the advisor
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, body.conversationId());
                    })
                    .system(promptService.buildSystemPrompt(body))
                    .user(body.userPrompt())
                    .call().content();
        }
    }

}
