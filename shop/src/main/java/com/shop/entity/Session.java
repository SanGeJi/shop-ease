package com.shop.entity;

import java.time.LocalDateTime;

public class Session {
    private String token;
    private Long userId;
    private String role;
    private LocalDateTime expiresAt;

    public String getToken() { return token; }
    public void setToken(String v) { this.token = v; }
    public Long getUserId() { return userId; }
    public void setUserId(Long v) { this.userId = v; }
    public String getRole() { return role; }
    public void setRole(String v) { this.role = v; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime v) { this.expiresAt = v; }
}