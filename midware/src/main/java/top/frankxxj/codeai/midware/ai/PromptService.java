package top.frankxxj.codeai.midware.ai;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import top.frankxxj.codeai.midware.web.dtos.ChatDTO;

@Log4j2
@Service
class PromptService {
    public static final String ZH_SYS_PROMPT = "你是CodeAi，一个编程助手，帮助用户解决编程问题。请根据用户的输入提供相关的代码示例和解释，用中文回答。";
    public static final String EN_SYS_PROMPT = "You are CodeAi, a programming assistant, helping users solve programming problems. Please provide relevant code examples and explanations based on the user's input. Respond in English.";

    public static final String ZH_USER_SNIPPET_PROMPT = """
            我请求你重点关注以下代码片段：%s
            """;
    public static final String EN_USER_SNIPPET_PROMPT = """
            Please take a close look at the following code snippet: %s
            """;

    public static final String ZH_USER_PROMPT = """
            我的需求或问题是：%s
            """;

    public static final String EN_USER_PROMPT = """
            My request or question is: %s
            """;

    public static final String ZH_FILES_PROMPT = """
            另外，用户还上传了以下文件：%s
            请根据这些文件提供相关的代码示例和解释。
            """;

    public static final String EN_FILES_PROMPT = """
            Additionally, the user has uploaded the following files: %s
            Please provide relevant code examples and explanations based on these files.
            """;

    public String buildUserPrompt(ChatDTO dto) {
        log.warn(dto);
        String prompt = "";
        if (dto.lang().equals("Chinese")) {
            prompt = prompt
                    .concat(dto.snippet() == null ? "" : ZH_USER_SNIPPET_PROMPT.formatted(dto.snippet()))
                    .concat(dto.files() == null ? "" :
                            ZH_FILES_PROMPT.formatted(dto.files().stream()
                                    .map(file -> file.filename() + ": " + file.content())
                                    .reduce((a, b) -> a + "\n" + b).orElse("")))
                    .concat(ZH_USER_PROMPT.formatted(dto.userPrompt()));
        } else {
            prompt = prompt
                    .concat(dto.snippet() == null ? "" : EN_USER_SNIPPET_PROMPT.formatted(dto.snippet()))
                    .concat(dto.files() == null || dto.files().isEmpty() ? "" :
                            EN_FILES_PROMPT.formatted(dto.files().stream()
                                    .map(file -> file.filename() + ": " + file.content())
                                    .reduce((a, b) -> a + "\n" + b).orElse("")))
                    .concat(EN_USER_PROMPT.formatted(dto.userPrompt()));
        }
        log.debug(prompt);
        return prompt;
    }

    public String buildSystemPrompt(ChatDTO body) {
        return switch (body.lang()) {
            case "Chinese" -> ZH_SYS_PROMPT;
            default -> EN_SYS_PROMPT;
        };
    }
}
