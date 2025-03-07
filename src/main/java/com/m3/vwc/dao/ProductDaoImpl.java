package com.m3.vwc.dao;

import com.m3.vwc.dto.*;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class ProductDaoImpl implements ProductDao {
    HashMap<String, Product> products = new HashMap<>();
    private String filePath;
    public static final String DELIMITER = ",";

    public ProductDaoImpl(String fileName){
        filePath = fileName;
        try{
            loadProducts();
        }
        catch(IOException e){
            throw new DaoPersistenceException("Could not load product file.");
        }
    }

    @Override
    public void loadProducts() throws IOException {
        Scanner sc = new Scanner(
                new BufferedReader(new FileReader(filePath)));
        boolean header = true;
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(DELIMITER);
            if (line.isEmpty()) {
                continue;
            }

            String type = parts[0].trim();
            BigDecimal costPerSqFt = new BigDecimal(parts[1].trim());
            BigDecimal laborCostPerSqFt = new BigDecimal(parts[2].trim());


            Product product = new Product(type, costPerSqFt, laborCostPerSqFt);
            products.put(type, product);
            // String productType, BigDecimal costPerSqFt, BigDecimal laborCostPerSqFt
        }

    }

    @Override
    public Product getProduct(String productType){
        return products.get(productType);
    }

    @Override
    public List<Product> getAllProducts(){
        return new ArrayList<>(products.values());
    }

    @Override
    public List<String> getAllProductNames() {
        return new ArrayList<>(products.keySet());
    }

    @Override
    public BigDecimal getProductCost(String productType){
        return products.get(productType).getCostPerSqFt();
    }

    @Override
    public BigDecimal getProductLaborCost(String productType){
        return products.get(productType).getLaborCostPerSqFt();
    }
}
