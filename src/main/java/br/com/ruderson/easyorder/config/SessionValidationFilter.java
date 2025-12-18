package br.com.ruderson.easyorder.config;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.ruderson.easyorder.service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SessionValidationFilter extends OncePerRequestFilter {

    private final SessionService sessionService;

    public SessionValidationFilter(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth && authentication.isAuthenticated()) {
            String sidClaim = jwtAuth.getToken().getClaimAsString("sid");
            if (!StringUtils.hasText(sidClaim)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sessão inválida");
                return;
            }

            try {
                UUID sid = UUID.fromString(sidClaim);
                if (!sessionService.isSidActive(sid)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sessão expirada ou encerrada");
                    return;
                }
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sessão inválida");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
