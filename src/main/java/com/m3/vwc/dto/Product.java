package com.m3.vwc.dto;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productType, product.productType) && Objects.equals(costPerSqFt, product.costPerSqFt) && Objects.equals(laborCostPerSqFt, product.laborCostPerSqFt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productType, costPerSqFt, laborCostPerSqFt);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productType='" + productType + '\'' +
                ", costPerSqFt=" + costPerSqFt +
                ", laborCostPerSqFt=" + laborCostPerSqFt +
                '}';
    }
}
