package top.frankxxj.codeai.midware.ai;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface ModelAccessManager {
    void addModelAccess(String modelName, List<String> enabledAuthorities);

    boolean hasAccess(String modelName, Collection<? extends GrantedAuthority> userAuthorities);
}
