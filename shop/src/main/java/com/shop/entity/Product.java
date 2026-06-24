package com.shop.entity;

import java.time.LocalDateTime;

public class Product {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private String imageUrl;
    private Integer status;
    private LocalDateTime createdAt;
    private String categoryName; // join
    private Long minPrice; // min sku price
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public Long getCategoryId() { return categoryId; } public void setCategoryId(Long v) { this.categoryId = v; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String v) { this.imageUrl = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public String getCategoryName() { return categoryName; } public void setCategoryName(String v) { this.categoryName = v; }
    public Long getMinPrice() { return minPrice; } public void setMinPrice(Long v) { this.minPrice = v; }
}
