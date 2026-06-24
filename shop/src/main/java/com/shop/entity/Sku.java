package com.shop.entity;

public class Sku {
    private Long id;
    private Long productId;
    private String skuCode;
    private Long price;
    private Integer stock;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getProductId() { return productId; } public void setProductId(Long v) { this.productId = v; }
    public String getSkuCode() { return skuCode; } public void setSkuCode(String v) { this.skuCode = v; }
    public Long getPrice() { return price; } public void setPrice(Long v) { this.price = v; }
    public Integer getStock() { return stock; } public void setStock(Integer v) { this.stock = v; }
}