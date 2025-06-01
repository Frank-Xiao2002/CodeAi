package top.frankxxj.codeai.midware.web.dtos;

import java.util.List;

public record ChatDTO(String userPrompt,
                      String conversationId,
                      String snippet,
                      String lang,
                      List<FileDTO> files
) {
}
