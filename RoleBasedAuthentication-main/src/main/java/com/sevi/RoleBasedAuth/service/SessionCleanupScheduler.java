package com.sevi.RoleBasedAuth.service;

import com.sevi.RoleBasedAuth.repository.SessionRepository;
import com.sevi.RoleBasedAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SessionCleanupScheduler {
    private final SessionService sessionService;
    private final UserRepository userRepository;

    @Autowired
    public SessionCleanupScheduler(SessionService sessionService, UserRepository userRepository) {

        this.sessionService = sessionService;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void cleanupAbandonedSessions() {
        userRepository.findAll().forEach(user -> {
            sessionService.closeAbandonedSessions(user, Duration.ofMinutes(5));
        });
    }
}
