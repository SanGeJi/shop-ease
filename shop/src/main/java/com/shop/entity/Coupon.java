package com.shop.entity;

import java.time.LocalDateTime;

public class Coupon {
    private Long id;
    private String code;
    private String name;
    private String type; // fixed / percent
    private Integer value;
    private Long minSpend;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Integer total;
    private Integer claimed;
    private Integer status;
    private LocalDateTime createdAt;
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getCode() { return code; } public void setCode(String v) { this.code = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getType() { return type; } public void setType(String v) { this.type = v; }
    public Integer getValue() { return value; } public void setValue(Integer v) { this.value = v; }
    public Long getMinSpend() { return minSpend; } public void setMinSpend(Long v) { this.minSpend = v; }
    public LocalDateTime getValidFrom() { return validFrom; } public void setValidFrom(LocalDateTime v) { this.validFrom = v; }
    public LocalDateTime getValidTo() { return validTo; } public void setValidTo(LocalDateTime v) { this.validTo = v; }
    public Integer getTotal() { return total; } public void setTotal(Integer v) { this.total = v; }
    public Integer getClaimed() { return claimed; } public void setClaimed(Integer v) { this.claimed = v; }
    public Integer getStatus() { return status; } public void setStatus(Integer v) { this.status = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}