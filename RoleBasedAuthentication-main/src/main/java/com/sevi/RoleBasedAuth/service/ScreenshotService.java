package com.sevi.RoleBasedAuth.service;

import com.sevi.RoleBasedAuth.model.Screenshot;
import com.sevi.RoleBasedAuth.repository.ScreenshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreenshotService {

    private final ScreenshotRepository screenshotRepository;

    public ScreenshotService(ScreenshotRepository screenshotRepository){
        this.screenshotRepository = screenshotRepository;
    }

    public List<Screenshot> getAllScreenshots() {
        return screenshotRepository.findAll();
    }

    public List<Screenshot> getScreenshotsByUsername(String username) {
        return screenshotRepository.findByUsername(username);
    }

    public Screenshot save(Screenshot screenshot) {
        return screenshotRepository.save(screenshot);
    }
}
