package top.frankxxj.codeai.midware.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.frankxxj.codeai.midware.user.dto.BasicUserDto;
import top.frankxxj.codeai.midware.user.dto.ChangePasswordDto;
import top.frankxxj.codeai.midware.user.security.SecurityUser;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Log4j2
class UserController {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder bcryptEncoder;
    private final UserDetailsService userDetailsService;
    private final UserCache userCache;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody BasicUserDto body) {
        Runnable r = () -> {
            log.info("Creating user: {}", body.name());
            AppUser appUser = new AppUser(body.name(), bcryptEncoder.encode(body.pwd()));
            appUserRepository.save(appUser);
            log.debug("User {} created", body.name());
        };
        try {
            userDetailsService.loadUserByUsername(body.name());
        } catch (UsernameNotFoundException exception) {
            new Thread(r).start();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
    }

    @PostMapping("/password")
    public ResponseEntity<String> changePassword(Authentication auth,
                                                 @RequestBody ChangePasswordDto body) {
        String username = auth.getName();
        var appUser = ((SecurityUser) userDetailsService.loadUserByUsername(username)).getUser();
        if (bcryptEncoder.matches(body.oldPassword(), appUser.getPwd())) {
            appUser.setPwd(body.newPassword());
            appUserRepository.save(appUser);
            userCache.removeUserFromCache(username);
            log.debug("User {} changed password", appUser.getName());
            return ResponseEntity.ok("Password changed");
        } else {
            log.error("User {} failed to change password", appUser.getName());
            return new ResponseEntity<>("Wrong password", HttpStatus.BAD_REQUEST);
        }
    }

}
