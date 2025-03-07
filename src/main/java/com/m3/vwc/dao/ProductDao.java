package com.m3.vwc.dao;

import com.m3.vwc.dto.Order;
import com.m3.vwc.dto.Product;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    void loadProducts() throws IOException;

    Product getProduct(String productType);

    List<Product> getAllProducts();

    List<String> getAllProductNames();

    BigDecimal getProductCost(String productType);

    BigDecimal getProductLaborCost(String productType);
}
