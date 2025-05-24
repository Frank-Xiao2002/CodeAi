package top.frankxxj.codeai.midware.ai;

import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j2
public class DefaultModelSwitcher implements ModelSwitcher {
    private static final Map<String, ChatClient> chatClients = new HashMap<>();

    public ChatClient getClient(String modelName) {
        if (chatClients.containsKey(modelName))
            return chatClients.get(modelName);
        else
            throw new IllegalArgumentException("ChatClient for model " + modelName + " not found");
    }

    public void addClient(String modelName, ChatClient chatClient) {
        if (chatClients.containsKey(modelName)) {
            log.error("ChatClient for model {} already exists", modelName);
            throw new IllegalArgumentException("ChatClient for model " + modelName + " already exists");
        } else {
            chatClients.put(modelName, chatClient);
            log.info("Add ChatClient for model: {}", modelName);
        }
    }

    public Set<String> getModelNames() {
        return chatClients.keySet();
    }
}
