package com.shop.entity;

import java.time.LocalDateTime;

public class CartItem {
    private Long id;
    private Long userId;
    private Long skuId;
    private Integer quantity;
    private LocalDateTime createdAt;
    // join 字段
    private Long productId;
    private String productName;
    private String productImage;
    private Long price;
    private Integer stock;
    private String skuSpecs;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public Long getSkuId() { return skuId; } public void setSkuId(Long v) { this.skuId = v; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer v) { this.quantity = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public Long getProductId() { return productId; } public void setProductId(Long v) { this.productId = v; }
    public String getProductName() { return productName; } public void setProductName(String v) { this.productName = v; }
    public String getProductImage() { return productImage; } public void setProductImage(String v) { this.productImage = v; }
    public Long getPrice() { return price; } public void setPrice(Long v) { this.price = v; }
    public Integer getStock() { return stock; } public void setStock(Integer v) { this.stock = v; }
    public String getSkuSpecs() { return skuSpecs; } public void setSkuSpecs(String v) { this.skuSpecs = v; }
}