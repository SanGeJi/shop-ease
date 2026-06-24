package com.shop.entity;

import java.time.LocalDateTime;

public class Category {
    private Long id;
    private String name;
    private String icon;
    private LocalDateTime createdAt;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getIcon() { return icon; } public void setIcon(String v) { this.icon = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}