package top.frankxxj.codeai.midware.ai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.api.common.OllamaApiConstants;

import java.util.List;

@Getter
@Setter
public class OllamaConnectionApi {
    private String baseUrl = OllamaApiConstants.DEFAULT_BASE_URL;
    private List<String> enabledAuthorities = List.of("USER", "ADMIN");
    private OllamaOptions options;
}
