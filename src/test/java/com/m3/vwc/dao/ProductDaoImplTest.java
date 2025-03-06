package com.m3.vwc.dao;

import com.m3.vwc.dto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoImplTest {
    private ProductDao testProductDao;


    public ProductDaoImplTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testProductDao = ctx.getBean("productDao", ProductDaoImpl.class);
    }

    @Test
    void testloadProducts() throws DaoPersistenceException, IOException {
        testProductDao.loadProducts();

        List<Product> products = testProductDao.getAllProducts();
        assertFalse(products.isEmpty(), "Product list should be empty.");
        assertTrue(products.size() > 0, "Products are present.");
    }

    @Test
    void testGetProduct(){
        Product product = testProductDao.getProduct("Carpet");
        assertNotNull(product, "Product should not be null for Carpet");
        assertEquals(new BigDecimal(2.25), product.getCostPerSqFt(), "Cost per sq ft should match for Carpet.");
        assertEquals(new BigDecimal("2.10"), product.getLaborCostPerSqFt(), "Labor cost per sq ft should match for Carpet.");

        product = testProductDao.getProduct("Laminate");
        assertNotNull(product, "Product should not be null for Laminate.");
        assertEquals(new BigDecimal("1.75"), product.getCostPerSqFt(), "Cost per Sq Ft should match for Laminate.");
        assertEquals(new BigDecimal("2.10"), product.getLaborCostPerSqFt(), "Labor cost per Sq Ft should match for Laminate.");

        product = testProductDao.getProduct("Tile");
        assertNotNull(product, "Product should not be null for Tile.");
        assertEquals(new BigDecimal("3.50"), product.getCostPerSqFt(), "Cost per Sq Ft should match for Tile.");
        assertEquals(new BigDecimal("4.15"), product.getLaborCostPerSqFt(), "Labor cost per Sq Ft should match for Tile.");

        product = testProductDao.getProduct("Wood");
        assertNotNull(product, "Product should not be null for Wood.");
        assertEquals(new BigDecimal("5.15"), product.getCostPerSqFt(), "Cost per Sq Ft should match for Wood.");
        assertEquals(new BigDecimal("4.75"), product.getLaborCostPerSqFt(), "Labor cost per Sq Ft should match for Wood.");

    }

    @Test
    void testGetAllProducts(){
        List<Product> products = testProductDao.getAllProducts();
        assertFalse(products.isEmpty(), "Product list should not be empty.");
        assertTrue(products.size() > 0, "Products are present.");
    }

    @Test
    void testGetAllProductNames(){
        List<String> products = testProductDao.getAllProductNames();
        assertFalse(products.isEmpty(), "Product list should not be empty.");
        assertTrue(products.contains("Wood"), "Product list should contain Wood.");
        assertTrue(products.contains("Tile"), "Product list should contain Tile.");
        assertTrue(products.contains("Carpet"), "Product list should contain Carpet.");
        assertTrue(products.contains("Laminate"), "Product list should contain Wood.");
    }

    @Test
    void testGetProductCost(){
        BigDecimal woodCost = testProductDao.getProductCost("Wood");
        assertEquals(new BigDecimal("5.15"), woodCost, "Cost per Sq Ft should match for Wood.");


    }

    void testGetLaborCost(){
        BigDecimal laborCost = testProductDao.getProductCost("Wood");
        assertEquals(new BigDecimal("4.75"), laborCost, "Labor cost per Sq Ft should match for Wood.");
    }
}