package com.sevi.RoleBasedAuth.model;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "sessions")
public class Session {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;  // ✅ Make sure this is here

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "is_duration")
    private Long duration;


    public void endSession() {
        if (this.logoutTime == null) {
            this.logoutTime = LocalDateTime.now();
            this.active = false;
            this.duration = Duration.between(loginTime, logoutTime).getSeconds();
        }
    }

    public Long calculateDuration() {
        if (loginTime == null) return 0L;
        LocalDateTime endTime = logoutTime != null ? logoutTime : LocalDateTime.now();
        return Duration.between(loginTime, endTime).getSeconds();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }

    public User getUser() {
        return user;
    }

    public boolean isActive() {
        return active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}
