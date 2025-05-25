package com.sevi.RoleBasedAuth.service;

import com.sevi.RoleBasedAuth.model.Screenshot;
import com.sevi.RoleBasedAuth.model.Session;
import com.sevi.RoleBasedAuth.model.User;
import com.sevi.RoleBasedAuth.repository.ScreenshotRepository;
import com.sevi.RoleBasedAuth.repository.SessionRepository;
import com.sevi.RoleBasedAuth.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;

@Service
public class ScheduledCaptureService {

    private final ScreenshotRepository screenshotRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public ScheduledCaptureService(ScreenshotRepository screenshotRepository, SessionRepository sessionRepository,
                                   UserRepository userRepository){
        this.screenshotRepository = screenshotRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    // Store logged-in user sessions
    private final Map<String, Integer> activeSessions = new ConcurrentHashMap<>();

    public void startCapture(String username, int sessionId) {
        activeSessions.put(username, sessionId);
    }

    public void stopCapture(String username) {
        activeSessions.remove(username);
    }

    @Scheduled(fixedRate = 60000) // every 1 minute
    public void captureScreenshots() {
        for (Map.Entry<String, Integer> entry : activeSessions.entrySet()) {
            String username = entry.getKey();
            Integer sessionId = entry.getValue();

            try {
                byte[] imageBytes = captureScreen();
                Session session = sessionRepository.findById(sessionId).orElse(null);
                if (session == null) continue;

                Screenshot screenshot = new Screenshot();
                screenshot.setUsername(username);
                screenshot.setCaptureTime(LocalDateTime.now());
                screenshot.setImageData(imageBytes);
                screenshot.setSession(session);

                screenshotRepository.save(screenshot);
            } catch (Exception e) {
                e.printStackTrace(); // log error
            }
        }
    }

    private byte[] captureScreen() throws Exception {
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(screenFullImage, "jpg", baos);
        return baos.toByteArray();
    }
}
