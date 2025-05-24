package top.frankxxj.codeai.midware.user.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.frankxxj.codeai.midware.user.AppUser;

import java.util.Arrays;
import java.util.Collection;

public class SecurityUser implements UserDetails {
    @Getter
    private AppUser user;

    public SecurityUser(AppUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(user.getRoles().split(AppUser.ROLE_SPLIT))
                .map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }
}
