package top.frankxxj.codeai.midware.web.dtos;

import java.util.List;
import java.util.Map;

public record ChatDTO(String userPrompt,
                      String conversationId,
                      String snippet,
                      List<Map<String, String>> files
) {
}
