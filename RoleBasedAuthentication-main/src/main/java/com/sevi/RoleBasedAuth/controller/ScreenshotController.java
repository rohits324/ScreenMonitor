//
//
//package com.sevi.RoleBasedAuth.controller;
//
//import com.sevi.RoleBasedAuth.model.Screenshot;
//import com.sevi.RoleBasedAuth.service.ScreenshotService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/screenshots")
//public class ScreenshotController {
//
//    private final ScreenshotService screenshotService;
//
//    public ScreenshotController(ScreenshotService screenshotService){
//        this.screenshotService = screenshotService;
//    }
//
//
//    @GetMapping
//    public ResponseEntity<List<Screenshot>> getAllScreenshots() {
//        return ResponseEntity.ok(screenshotService.getAllScreenshots());
//    }
//
//    @GetMapping("/user/{username}")
//    public ResponseEntity<List<Screenshot>> getScreenshotsByUsername(@PathVariable String username) {
//        return ResponseEntity.ok(screenshotService.getScreenshotsByUsername(username));
//    }
//}

package com.sevi.RoleBasedAuth.controller;

import com.sevi.RoleBasedAuth.model.Screenshot;
import com.sevi.RoleBasedAuth.service.JwtService;
import com.sevi.RoleBasedAuth.service.ScreenshotService;
import com.sevi.RoleBasedAuth.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screenshots")
public class ScreenshotController {

    @Autowired
    private ScreenshotService screenshotService;

    @Autowired
    private JwtService jwtService;


    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getScreenshotsByUser(@PathVariable String username, HttpServletRequest request) {
        // 1. Extract token
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header.");
        }

        String jwt = authHeader.substring(7); // Remove "Bearer "
        String tokenUsername;

        try {
            tokenUsername = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token.");
        }

        // 2. Ensure user is requesting their own screenshots
        if (!tokenUsername.equals(username)) {
            return ResponseEntity.status(403).body("Access denied: You can only view your own screenshots.");
        }

        List<Screenshot> screenshots = screenshotService.getScreenshotsByUsername(username);
        return ResponseEntity.ok(screenshots);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Screenshot>> getAllScreenshots() {
        return ResponseEntity.ok(screenshotService.getAllScreenshots());
    }
}

