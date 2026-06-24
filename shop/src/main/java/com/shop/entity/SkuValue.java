package com.shop.entity;

public class SkuValue {
    private Long id;
    private Long skuId;
    private Long specId;
    private Long optionId;
    private String specName;  // join
    private String optionValue; // join
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getSkuId() { return skuId; } public void setSkuId(Long v) { this.skuId = v; }
    public Long getSpecId() { return specId; } public void setSpecId(Long v) { this.specId = v; }
    public Long getOptionId() { return optionId; } public void setOptionId(Long v) { this.optionId = v; }
    public String getSpecName() { return specName; } public void setSpecName(String v) { this.specName = v; }
    public String getOptionValue() { return optionValue; } public void setOptionValue(String v) { this.optionValue = v; }
}