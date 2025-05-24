package top.frankxxj.codeai.midware;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.frankxxj.codeai.midware.user.AppUser;
import top.frankxxj.codeai.midware.user.AppUserRepository;

@Log4j2
@SpringBootApplication
@EnableCaching
public class MidwareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MidwareApplication.class, args);
    }

    @Bean
    CommandLineRunner insertFirstUser(AppUserRepository appUserRepository,
                                      PasswordEncoder bcryptEncoder) {
        return (args) -> {
            if (appUserRepository.count() == 0L) {
                appUserRepository.save(new AppUser("admin", bcryptEncoder.encode("password"), "ADMIN,USER"));
                log.info("Created system's first user 'admin' with password 'password', this user has the highest authorities.");
            }
        };
    }
}
