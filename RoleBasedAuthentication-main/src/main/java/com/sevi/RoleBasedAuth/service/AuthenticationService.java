package com.sevi.RoleBasedAuth.service;

import com.sevi.RoleBasedAuth.model.AuthenticationResponse;
import com.sevi.RoleBasedAuth.model.Token;
import com.sevi.RoleBasedAuth.model.User;
import com.sevi.RoleBasedAuth.repository.TokenRepository;
import com.sevi.RoleBasedAuth.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    private final SessionService sessionService;

    private final ScheduledCaptureService scheduledCaptureService;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager,
                                 SessionService sessionService,
                                 ScheduledCaptureService scheduledCaptureService) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.sessionService = sessionService;
        this.scheduledCaptureService = scheduledCaptureService;
    }

    public AuthenticationResponse register(User request) {

        // check if user already exist. if exist than authenticate the user
        if(repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "User already exist");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        user.setRole(request.getRole());

        user = repository.save(user);

        String jwt = jwtService.generateToken(user);

        saveUserToken(jwt, user);
        sessionService.createLoginSession(user, jwt);

        return new AuthenticationResponse(jwt, "User registration was successful");

    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(jwt, user);
        var session = sessionService.createLoginSession(user, jwt);

        if (user.getRole().name().equals("USER")) {
            scheduledCaptureService.startCapture(user.getUsername(), session.getId());
        }




        return new AuthenticationResponse(jwt, "User login was successful");

    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}



//package com.sevi.RoleBasedAuth.service;
//
//import com.sevi.RoleBasedAuth.model.AuthenticationResponse;
//import com.sevi.RoleBasedAuth.model.Session;
//import com.sevi.RoleBasedAuth.model.Token;
//import com.sevi.RoleBasedAuth.model.User;
//import com.sevi.RoleBasedAuth.repository.SessionRepository;
//import com.sevi.RoleBasedAuth.repository.TokenRepository;
//import com.sevi.RoleBasedAuth.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class AuthenticationService {
//
//    private final UserRepository repository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final TokenRepository tokenRepository;
//    private final AuthenticationManager authenticationManager;
//    private final SessionRepository sessionRepository;  // 👈 added
//
//    public AuthenticationService(UserRepository repository,
//                                 PasswordEncoder passwordEncoder,
//                                 JwtService jwtService,
//                                 TokenRepository tokenRepository,
//                                 AuthenticationManager authenticationManager,
//                                 SessionRepository sessionRepository) {  // 👈 added
//        this.repository = repository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtService = jwtService;
//        this.tokenRepository = tokenRepository;
//        this.authenticationManager = authenticationManager;
//        this.sessionRepository = sessionRepository; // 👈 assigned
//    }
//
//    public AuthenticationResponse register(User request) {
//        if (repository.findByUsername(request.getUsername()).isPresent()) {
//            return new AuthenticationResponse(null, "User already exist");
//        }
//
//        User user = new User();
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(request.getRole());
//
//        user = repository.save(user);
//
//        String jwt = jwtService.generateToken(user);
//        saveUserToken(jwt, user);
//
//        return new AuthenticationResponse(jwt, "User registration was successful");
//    }
//
//    public AuthenticationResponse authenticate(User request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()
//                )
//        );
//
//        User user = repository.findByUsername(request.getUsername()).orElseThrow();
//        String jwt = jwtService.generateToken(user);
//
//        revokeAllTokenByUser(user);
//        saveUserToken(jwt, user);
//        createLoginSession(user); // 👈 Add login session record
//
//        return new AuthenticationResponse(jwt, "User login was successful");
//    }
//
//    private void revokeAllTokenByUser(User user) {
//        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
//        if (validTokens.isEmpty()) {
//            return;
//        }
//        validTokens.forEach(t -> t.setLoggedOut(true));
//        tokenRepository.saveAll(validTokens);
//    }
//
//    private void saveUserToken(String jwt, User user) {
//        Token token = new Token();
//        token.setToken(jwt);
//        token.setLoggedOut(false);
//        token.setUser(user);
//
//        tokenRepository.save(token);
//    }
//
//    // 🔐 Session logic for login
//    private void createLoginSession(User user) {
//        Session session = new Session();
//        session.setUser(user);
//        session.setLoginTime(LocalDateTime.now());
//        session.setActive(true);
//        sessionRepository.save(session);
//    }
//
//}
