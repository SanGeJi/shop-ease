package com.shop.entity;

import java.time.LocalDateTime;

public class UserCoupon {
    private Long id;
    private Long userId;
    private Long couponId;
    private String status;
    private LocalDateTime claimedAt;
    // join
    private String code;
    private String name;
    private String type;
    private Integer value;
    private Long minSpend;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public Long getCouponId() { return couponId; } public void setCouponId(Long v) { this.couponId = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public LocalDateTime getClaimedAt() { return claimedAt; } public void setClaimedAt(LocalDateTime v) { this.claimedAt = v; }
    public String getCode() { return code; } public void setCode(String v) { this.code = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getType() { return type; } public void setType(String v) { this.type = v; }
    public Integer getValue() { return value; } public void setValue(Integer v) { this.value = v; }
    public Long getMinSpend() { return minSpend; } public void setMinSpend(Long v) { this.minSpend = v; }
}