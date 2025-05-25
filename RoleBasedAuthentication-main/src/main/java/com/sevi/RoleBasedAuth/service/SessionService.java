package com.sevi.RoleBasedAuth.service;

import com.sevi.RoleBasedAuth.model.Session;
import com.sevi.RoleBasedAuth.model.User;
import com.sevi.RoleBasedAuth.repository.SessionRepository;
import com.sevi.RoleBasedAuth.repository.TokenRepository;
import com.sevi.RoleBasedAuth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
//    private final UserRepository userRepository;
    private final TokenRepository tokenRepository; // Add this
    private final JwtService jwtService;

    public SessionService(SessionRepository sessionRepository,
                          TokenRepository tokenRepository,
                          JwtService jwtService) {
        this.sessionRepository = sessionRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
    }

    public Session createLoginSession(User user, String token) {
        Session session = new Session();
        session.setUser(user);
        session.setUsername(user.getUsername());
        session.setLoginTime(LocalDateTime.now());
        session.setActive(true);
        session.setToken(token);
       // log.info("Creating new session for user: {}", user.getUsername());
        return sessionRepository.save(session);
    }

    public void endSessionByToken(String token, LocalDateTime logoutTime) {
        sessionRepository.findByTokenAndActive(token, true)
                .ifPresent(session -> endSession(session, logoutTime));
    }


    public void closeAbandonedSessions(User user, Duration maxDuration) {
        LocalDateTime cutoff = LocalDateTime.now().minus(maxDuration);
        sessionRepository.findByUserAndActiveAndLoginTimeBefore(user, true, cutoff)
                .forEach(this::endSession);  // now matches the method signature
    }


    private void endSession(Session session) {
        session.setLogoutTime(LocalDateTime.now());
        session.setActive(false);
        session.setDuration(Duration.between(session.getLoginTime(), session.getLogoutTime()).getSeconds());
        sessionRepository.save(session);
    }

    private void endSession(Session session, LocalDateTime logoutTime) {
        session.setLogoutTime(logoutTime);
        session.setActive(false);
        session.setDuration(Duration.between(session.getLoginTime(), logoutTime).getSeconds());
        sessionRepository.save(session);
    }

}