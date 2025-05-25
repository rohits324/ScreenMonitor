package com.sevi.RoleBasedAuth.config;

import com.sevi.RoleBasedAuth.repository.TokenRepository;
import com.sevi.RoleBasedAuth.repository.UserRepository;
import com.sevi.RoleBasedAuth.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.time.LocalDateTime;


@Configuration
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {
    private final TokenRepository tokenRepository;
    private final SessionService sessionService;
    private final UserRepository userRepository;

    public LogoutHandler(TokenRepository tokenRepository,
                         SessionService sessionService,
                         UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.sessionService = sessionService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        // Handle explicit logout
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            handleTokenLogout(token);
        }

        // Handle session timeout/connection drop
        if (authentication != null) {
            String username = authentication.getName();
            handleAbandonedSessions(username);
        }
    }

    private void handleTokenLogout(String token) {
        LocalDateTime logoutTime = LocalDateTime.now();
        // Mark token as logged out
        tokenRepository.findByToken(token).ifPresent(t -> {
            t.setLoggedOut(true);
            tokenRepository.save(t);
        });

        // Mark session as ended
        sessionService.endSessionByToken(token, logoutTime);
    }

    private void handleAbandonedSessions(String username) {
        // Find all active sessions for this user
        userRepository.findByUsername(username).ifPresent(user -> {
            // Close all active sessions older than 5 minutes
            sessionService.closeAbandonedSessions(user, Duration.ofMinutes(5));
        });
    }
}