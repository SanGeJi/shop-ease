package com.shop.entity;

public class OrderItem {
    private Long id;
    private Long orderId;
    private Long skuId;
    private String productName;
    private String skuSpecs;
    private Long price;
    private Integer quantity;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getOrderId() { return orderId; } public void setOrderId(Long v) { this.orderId = v; }
    public Long getSkuId() { return skuId; } public void setSkuId(Long v) { this.skuId = v; }
    public String getProductName() { return productName; } public void setProductName(String v) { this.productName = v; }
    public String getSkuSpecs() { return skuSpecs; } public void setSkuSpecs(String v) { this.skuSpecs = v; }
    public Long getPrice() { return price; } public void setPrice(Long v) { this.price = v; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer v) { this.quantity = v; }
}