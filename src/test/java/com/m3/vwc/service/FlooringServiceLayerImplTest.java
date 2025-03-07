package com.m3.vwc.service;

import com.m3.vwc.dao.*;
import com.m3.vwc.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlooringServiceLayerImplTest {

    private FlooringServiceLayerImpl service;

    @BeforeEach
    void setUp() {
        // Load Spring context
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // Get beans from the context
        service = context.getBean("service", FlooringServiceLayerImpl.class);
    }

    @Test
    void testValidateDate() throws InvalidInputException {
        LocalDate date = service.validateDate("12/31/2025", true);
        assertEquals(LocalDate.of(2025, 12, 31), date);
    }

    @Test
    void testValidateName() throws InvalidInputException {
        String name = service.validateName("John Doe", false);
        assertEquals("John Doe", name);
    }

    @Test
    void testValidateState() throws InvalidInputException {
        String state = service.validateState("TX", false);
        assertEquals("TX", state);
    }

    @Test
    void testValidateProduct() throws InvalidInputException {
        String product = service.validateProduct("Tile", false);
        assertEquals("Tile", product);
    }

    @Test
    void testValidateUserYN() throws InvalidInputException {
        String input = service.validateUserYN("Y");
        assertEquals("Y", input);
    }

    @Test
    void testValidateOrderNum() throws InvalidInputException {
        int orderNum = service.validateOrderNum("123");
        assertEquals(123, orderNum);
    }

    @Test
    void testValidateArea() throws InvalidInputException {
        BigDecimal area = service.validateArea("150", false);
        assertEquals(new BigDecimal("150"), area);
    }

    @Test
    void testCreateNewOrder() throws DaoPersistenceException {
        LocalDate orderDate = LocalDate.of(2025, 10, 23);
        String customerName = "Snoopy";
        String state = "TX";
        String type = "Tile";
        BigDecimal area = new BigDecimal("200");

        Order order = service.createNewOrder(orderDate, customerName, state, type, area);

        assertNotNull(order);
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(customerName, order.getCustomerName());
        assertEquals(state, order.getState());
        assertEquals(type, order.getProductType());
    }

    @Test
    void testAddOrderToList() throws DaoPersistenceException {
        LocalDate orderDate = LocalDate.of(2025, 10, 23);
        Order order = service.createNewOrder(orderDate, "Snoopy", "TX", "Tile", new BigDecimal("200"));
        Order result = service.addOrderToList(order);
        assertNotNull(result);
        assertEquals(order.getOrderNumber(), result.getOrderNumber());
    }

    @Test
    void testUpdateOrder() throws DaoPersistenceException {
        LocalDate orderDate = LocalDate.of(2025, 10, 23);
        Order order = service.createNewOrder(orderDate, "Snoopy", "TX", "Tile", new BigDecimal("200"));
        service.addOrderToList(order);
        order.setCustomerName("Charlie Brown");
        Order updatedOrder = service.updateOrder(order);
        assertEquals("Charlie Brown", updatedOrder.getCustomerName());
    }

    @Test
    void testRemoveOrderFromList() throws DaoPersistenceException {
        LocalDate orderDate = LocalDate.of(2025, 10, 23);
        Order order = service.createNewOrder(orderDate, "Snoopy", "TX", "Tile", new BigDecimal("200"));
        service.addOrderToList(order);
        Order removedOrder = service.removeOrderFromList(order);
        assertNotNull(removedOrder);
    }

    @Test
    void testExportAllOrders() throws DaoPersistenceException, IOException {
        String backupFolderPath = "src/test/resources/Backup";
        Path exportFilePath = Paths.get(backupFolderPath, "DataExport.txt");

        if (Files.exists(exportFilePath)) {
            Files.delete(exportFilePath);
        }

        service.exportAllOrders();

        assertTrue(Files.exists(exportFilePath), "The export file should be created.");
        Files.deleteIfExists(exportFilePath);
    }

    @Test
    void testGetProducts() {
        List<Product> products = service.getProducts();
        assertNotNull(products);
        assertFalse(products.isEmpty());
    }
}
