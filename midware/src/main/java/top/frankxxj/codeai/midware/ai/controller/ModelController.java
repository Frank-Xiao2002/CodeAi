package top.frankxxj.codeai.midware.ai.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.frankxxj.codeai.midware.ai.config.ModelProperties;
import top.frankxxj.codeai.midware.ai.dto.ModelDto;

import java.util.List;

@RestController
@RequestMapping("/api/model")
@RequiredArgsConstructor
@Log4j2
public class ModelController {
    private final ModelProperties modelProperties;

    @GetMapping("/all")
    public ResponseEntity<List<ModelDto>> list() {
        List<ModelDto> list = modelProperties.getOllama().stream()
                .map(api -> new ModelDto(api.getOptions().getModel(), String.join("-", api.getEnabledAuthorities())))
                .toList();
        return ResponseEntity.ok(list);
    }
}
