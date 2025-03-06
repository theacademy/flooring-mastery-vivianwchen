package com.m3.vwc.dto;

import java.math.BigDecimal;

public class Product {
    String productType;
    BigDecimal costPerSqFt;
    BigDecimal laborCostPerSqFt;

    public Product(String productType, BigDecimal costPerSqFt, BigDecimal laborCostPerSqFt) {
        this.productType = productType;
        this.costPerSqFt = costPerSqFt;
        this.laborCostPerSqFt = laborCostPerSqFt;
    }

    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public BigDecimal getCostPerSqFt() {
        return costPerSqFt;
    }
    public void setCostPerSqFt(BigDecimal costPerSqFt) {
        this.costPerSqFt = costPerSqFt;
    }
    public BigDecimal getLaborCostPerSqFt() {
        return laborCostPerSqFt;
    }
    public void setLaborCostPerSqFt(BigDecimal laborCostPerSqFt) {
        this.laborCostPerSqFt = laborCostPerSqFt;
    }

}
