package top.frankxxj.codeai.midware.ai;

import org.springframework.ai.chat.client.ChatClient;

public interface ModelSwitcher {

    /**
     * Choose the correct chatclient based on the model name
     *
     * @param modelName the model name
     * @return the chat client
     */
    public ChatClient getClient(String modelName);
}
