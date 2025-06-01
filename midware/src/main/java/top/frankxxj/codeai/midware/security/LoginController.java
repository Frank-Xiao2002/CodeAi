package top.frankxxj.codeai.midware.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/token")
    public ResponseEntity<LoginResponseDto> login(@RequestBody BasicUserDto basicUserDto) {
        String token = null;
        LoginResponseDto response = null;
        try {
            UserDetails principle = userDetailsService.loadUserByUsername(basicUserDto.name());
            log.debug("User {} login success", basicUserDto.name());
            log.debug("principle class: {}", principle.getClass());
            if (passwordEncoder.matches(basicUserDto.pwd(), principle.getPassword())) {
                var auth = UsernamePasswordAuthenticationToken.authenticated(principle, null, principle.getAuthorities());
                token = tokenService.generateToken(auth);
                var id = ((SecurityUser) principle).getUser().getId();
                response = new LoginResponseDto(id, token, ((SecurityUser) principle).getUser().getRoles());
                log.debug("response: {}", response);
                log.debug("Bearer Token generated for user: {}", principle.getUsername());
            } else
                throw new IllegalArgumentException("Invalid username or password");
        } catch (UsernameNotFoundException e) {
            log.warn("User not found: {}", basicUserDto.name());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(response);
    }
}
