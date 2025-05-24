package top.frankxxj.codeai.midware.ai.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.frankxxj.codeai.midware.ai.DefaultModelAccessManager;
import top.frankxxj.codeai.midware.ai.DefaultModelSwitcher;
import top.frankxxj.codeai.midware.ai.ModelAccessManager;
import top.frankxxj.codeai.midware.ai.ModelSwitcher;

import java.util.Set;

@Log4j2
@AutoConfiguration
@EnableConfigurationProperties({ModelProperties.class})
class ModelAutoConfig {
    @Bean
    @ConditionalOnMissingBean(ModelSwitcher.class)
    public DefaultModelSwitcher defaultModelSwitcher(ModelProperties modelProperties) {
        var switcher = new DefaultModelSwitcher();
        Set<OllamaConnectionApi> ollamaConnections = modelProperties.getOllama();
        for (OllamaConnectionApi connection : ollamaConnections) {
            String baseUrl = connection.getBaseUrl();
            OllamaOptions options = connection.getOptions();
            OllamaChatModel model = OllamaChatModel.builder()
                    .ollamaApi(OllamaApi.builder().baseUrl(baseUrl).build())
                    .defaultOptions(options).build();
            ChatClient chatClient;
            if (options.getModel().equalsIgnoreCase("qwen2.5-coder:1.5b-instruct")) {
                chatClient = ChatClient.builder(model).build();
            } else {
                chatClient = ChatClient.builder(model)
                        .defaultSystem("Your name is Fine-tuned Qwen. Your creator is Frank Xiao.").build();
            }
            switcher.addClient(options.getModel(), chatClient);
        }
        log.info("ModelSwitcher initialized with the following models: {}", switcher.getModelNames());
        return switcher;
    }

    @Bean
    @ConditionalOnMissingBean(ModelAccessManager.class)
    public DefaultModelAccessManager defaultModelAccessManager(ModelProperties modelProperties) {
        var accessManager = new DefaultModelAccessManager();
        Set<OllamaConnectionApi> ollamaConnections = modelProperties.getOllama();
        for (OllamaConnectionApi connection : ollamaConnections) {
            String modelName = connection.getOptions().getModel();
            accessManager.addModelAccess(modelName, connection.getEnabledAuthorities());
        }
        log.info("DefaultModelAccessManager initialized successfully");
        return accessManager;
    }
}
