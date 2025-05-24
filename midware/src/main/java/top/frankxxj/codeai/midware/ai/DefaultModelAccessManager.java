package top.frankxxj.codeai.midware.ai;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class DefaultModelAccessManager implements ModelAccessManager {
    private final Map<String, List<String>> modelAccessAuthorityMap = new HashMap<>();

    @Override
    public void addModelAccess(String modelName, List<String> enabledAuthorities) {
        if (modelAccessAuthorityMap.containsKey(modelName)) {
            log.error("Model access for {} already exists", modelName);
            throw new IllegalArgumentException("Model access for " + modelName + " already exists");
        } else {
            modelAccessAuthorityMap.put(modelName, enabledAuthorities);
            log.debug("Add model access for: {}", modelName);
        }
    }

    @Override
    public boolean hasAccess(String modelName, Collection<? extends GrantedAuthority> userAuthorities) {
        List<String> enabledAuthorities = modelAccessAuthorityMap.get(modelName);
        if (validateModelName(modelName, enabledAuthorities)) {
            return false;
        }
        return userAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(enabledAuthorities::contains);
    }

    private static boolean validateModelName(String modelName, List<String> enabledAuthorities) {
        if (enabledAuthorities == null) {
            log.warn("No access configuration found for model: {}", modelName);
            return true;
        }
        return false;
    }


}
