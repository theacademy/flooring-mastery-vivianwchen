package com.m3.vwc.dao;


import com.m3.vwc.dto.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface TaxDao {
    public void loadTax() throws IOException;

    public Tax getTax(String state);

    public List<String> getAllStateNames();

    public BigDecimal getTaxRate(String state);
}
