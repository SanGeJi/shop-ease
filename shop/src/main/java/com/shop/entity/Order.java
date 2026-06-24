package com.shop.entity;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long total;
    private Long discount;
    private String status;
    private String receiver;
    private String phone;
    private String address;
    private Long couponId;
    private LocalDateTime createdAt;
    private String username; // join
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getOrderNo() { return orderNo; } public void setOrderNo(String v) { this.orderNo = v; }
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public Long getTotal() { return total; } public void setTotal(Long v) { this.total = v; }
    public Long getDiscount() { return discount; } public void setDiscount(Long v) { this.discount = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public String getReceiver() { return receiver; } public void setReceiver(String v) { this.receiver = v; }
    public String getPhone() { return phone; } public void setPhone(String v) { this.phone = v; }
    public String getAddress() { return address; } public void setAddress(String v) { this.address = v; }
    public Long getCouponId() { return couponId; } public void setCouponId(Long v) { this.couponId = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public String getUsername() { return username; } public void setUsername(String v) { this.username = v; }
}