package com.shop.entity;

import java.time.LocalDateTime;

public class Review {
    private Long id;
    private Long productId;
    private Long userId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private String username; // join
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getProductId() { return productId; } public void setProductId(Long v) { this.productId = v; }
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public Integer getRating() { return rating; } public void setRating(Integer v) { this.rating = v; }
    public String getContent() { return content; } public void setContent(String v) { this.content = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public String getUsername() { return username; } public void setUsername(String v) { this.username = v; }
}