package com.shop.entity;

import java.time.LocalDateTime;

public class Address {
    private Long id;
    private Long userId;
    private String receiver;
    private String phone;
    private String address;
    private Integer isDefault;
    private LocalDateTime createdAt;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public String getReceiver() { return receiver; } public void setReceiver(String v) { this.receiver = v; }
    public String getPhone() { return phone; } public void setPhone(String v) { this.phone = v; }
    public String getAddress() { return address; } public void setAddress(String v) { this.address = v; }
    public Integer getIsDefault() { return isDefault; } public void setIsDefault(Integer v) { this.isDefault = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}
