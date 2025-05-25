package com.sevi.RoleBasedAuth.controller;

import com.sevi.RoleBasedAuth.model.AuthenticationResponse;
import com.sevi.RoleBasedAuth.model.Session;
import com.sevi.RoleBasedAuth.model.User;
import com.sevi.RoleBasedAuth.repository.SessionRepository;
import com.sevi.RoleBasedAuth.service.AuthenticationService;
import com.sevi.RoleBasedAuth.service.JwtService;
import com.sevi.RoleBasedAuth.service.ScheduledCaptureService;
import com.sevi.RoleBasedAuth.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;
    private final SessionService sessionService;
    private final SessionRepository sessionRepository;
    private final ScheduledCaptureService scheduledCaptureService;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(AuthenticationService authService,
                                    SessionService sessionService,
                                    SessionRepository sessionRepository,
                                    ScheduledCaptureService scheduledCaptureService,
                                    JwtService jwtService) {
        this.authService = authService;
        this.sessionService = sessionService;
        this.sessionRepository = sessionRepository;
        this.scheduledCaptureService = scheduledCaptureService;
        this. jwtService = jwtService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody User request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

@PostMapping("/auth/logout")
public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
    System.out.println("Authorization Header: " + authHeader);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("Invalid Authorization header");
    }

    String token = authHeader.substring(7);
    User user = jwtService.extractUser(token);
    sessionService.endSessionByToken(token, LocalDateTime.now());

    if (user.getRole().name().equals("USER")) {
        scheduledCaptureService.stopCapture(user.getUsername());
    }

    return ResponseEntity.ok("Logged out successfully. Session data saved.");
}

//    @GetMapping("/auth/debug/sessions")
//    public ResponseEntity<List<Session>> getUserSessions(@RequestParam String username) {
//        return ResponseEntity.ok(sessionRepository.findByUsername(username));
//    }
//
//    @GetMapping("/debug/sessions")
//    public List<Session> getAllSessions() {
//        return sessionRepository.findAll();
//    }

}
