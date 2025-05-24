package top.frankxxj.codeai.midware.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.frankxxj.codeai.midware.security.service.RSATokenService;
import top.frankxxj.codeai.midware.user.dto.BasicUserDto;
import top.frankxxj.codeai.midware.user.security.SecurityUser;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Log4j2
class LoginController {
    private final UserDetailsService userDetailsService;
    private final RSATokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<TokenDTO> login(@RequestBody BasicUserDto basicUserDto) {
        String token = null;
        TokenDTO response = null;
        try {
            UserDetails principle = userDetailsService.loadUserByUsername(basicUserDto.name());
            log.debug("User {} login success", basicUserDto.name());
            log.debug("principle class: {}", principle.getClass());
            var auth = UsernamePasswordAuthenticationToken.authenticated(principle, null, principle.getAuthorities());
            token = tokenService.generateToken(auth);
            response = new TokenDTO(token, ((SecurityUser) principle).getUser().getRoles());
            log.debug("response: {}", response);
            log.debug("Bearer Token generated for user: {}", principle.getUsername());
        } catch (UsernameNotFoundException e) {
            log.warn("User not found: {}", basicUserDto.name());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(response);
    }
}
