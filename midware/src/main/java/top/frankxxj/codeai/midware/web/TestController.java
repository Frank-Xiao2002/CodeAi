package top.frankxxj.codeai.midware.web;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@Log4j2
public class TestController {
    @PostMapping("/token/test")
    public ResponseEntity<?> testToken() {
        return ResponseEntity.ok().build();
    }
}
