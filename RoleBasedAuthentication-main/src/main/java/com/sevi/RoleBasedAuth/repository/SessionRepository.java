package com.sevi.RoleBasedAuth.repository;

import com.sevi.RoleBasedAuth.model.Session;
import com.sevi.RoleBasedAuth.model.Token;
import com.sevi.RoleBasedAuth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findTopByUserAndActiveOrderByLoginTimeDesc(User user, boolean active);


    // Alternative if you need to find by username
    Optional<Session> findTopByUsernameAndActiveOrderByLoginTimeDesc(String username, boolean active);
    Optional<Session> findByTokenAndActive(String token, boolean active);
    List<Session>findByToken(String token);


    // For debugging - add these:
    List<Session> findByUser(User user);
    List<Session> findByUsername(String username);


    List<Session> findByUserAndActiveAndLoginTimeBefore(User user, boolean active, LocalDateTime cutoff);


}
