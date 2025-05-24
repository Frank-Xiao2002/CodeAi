package top.frankxxj.codeai.midware.ai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(ModelProperties.MODEL_PREFIX)
@Getter
@Setter
public class ModelProperties {
    public static final String MODEL_PREFIX = "codeai.model";
    private Set<OllamaConnectionApi> ollama = Set.of();
}
