package com.m3.vwc.dto;

import java.math.BigDecimal;

public class Tax {
    private String stateAbbr;
    private String stateName;
    private BigDecimal taxRate;

    public Tax(String stateAbbr, String stateName, BigDecimal taxRate) {
        this.stateAbbr = stateAbbr;
        this.stateName = stateName;
        this.taxRate = taxRate;
    }
    public String getStateAbbr() {
        return stateAbbr;
    }
    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    public BigDecimal getTaxRate() {
        return taxRate;
    }
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
}
