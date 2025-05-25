package com.sevi.RoleBasedAuth.repository;

import com.sevi.RoleBasedAuth.model.Screenshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScreenshotRepository extends JpaRepository<Screenshot, Long> {
    List<Screenshot> findByUsername(String username);
    List<Screenshot> findBySessionId(Integer sessionId);
}
