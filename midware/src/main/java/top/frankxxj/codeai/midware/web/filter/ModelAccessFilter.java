package top.frankxxj.codeai.midware.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.frankxxj.codeai.midware.ai.ModelAccessManager;

import java.io.IOException;
import java.util.Collection;

@Component
@Log4j2
@RequiredArgsConstructor
public class ModelAccessFilter extends OncePerRequestFilter {

    private final ModelAccessManager modelAccessManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, AccessDeniedException {
        String modelName = request.getHeader("Codeai-model");
        if (modelName != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            log.debug("Authentication: {}", authentication);
            log.debug("authentication.getName(): {}", authentication.getName());
            log.debug("authentication.getPrincipal(): {}", authentication.getPrincipal());
            log.debug("User has authorities: {}", authorities);
            if (modelAccessManager.hasAccess(modelName, authorities)) {
                filterChain.doFilter(request, response);
            } else {
                log.debug("User {} does not have access to model: {}", authentication.getName(), modelName);
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        String.format("You do not have access to model %s.", modelName));
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !uri.startsWith("/api/chat");
    }
}