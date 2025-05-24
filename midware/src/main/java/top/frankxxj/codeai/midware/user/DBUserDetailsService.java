package top.frankxxj.codeai.midware.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.frankxxj.codeai.midware.user.security.SecurityUser;

@Service
@RequiredArgsConstructor
public class DBUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findAppUserByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new SecurityUser(appUser);
    }

}
