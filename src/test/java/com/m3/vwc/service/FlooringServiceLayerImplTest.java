package com.m3.vwc.service;

import com.m3.vwc.dao.*;
import com.m3.vwc.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FlooringServiceLayerImplTest {
    private FlooringServiceLayer testService;

    public FlooringServiceLayerImplTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testService = ctx.getBean("service", FlooringServiceLayer.class);
    }

    @Test
    void testValidateDateValid() throws InvalidInputException {
        LocalDate date = testService.validateDate("12/25/2025", true);
        assertEquals(LocalDate.of(2025, 12, 25), date, "Date should be in the correct format");
    }

    @Test
    void testValidateDateInvalid() {
        assertThrows(InvalidInputException.class, () -> testService.validateDate("13/25/2025", true), "Date should be in the correct format");
    }

    @Test
    void testValidateNameValid() throws InvalidInputException {
        String name = testService.validateName("John Doe", false);
        assertEquals("John Doe", name);
    }

    @Test
    void testValidateNameInvalid() {
        assertThrows(InvalidInputException.class, () -> testService.validateName("!@#$", false));
    }

    @Test
    void testValidateStateValid() throws InvalidInputException {
        String state = testService.validateState("TX", false);
        assertEquals("TX", state);
    }

    @Test
    void testValidateStateInvalid() {
        assertThrows(InvalidInputException.class, () -> testService.validateState("NY", false));
    }

    @Test
    void testValidateProductValid() throws InvalidInputException {
        String product = testService.validateProduct("Wood", false);
        assertEquals("Wood", product);
    }

    @Test
    void testValidateProductInvalid() {
        assertThrows(InvalidInputException.class, () -> testService.validateProduct("Marble", false));
    }

    @Test
    void testValidateUserYNValid() throws InvalidInputException {
        assertEquals("Y", testService.validateUserYN("Y"));
        assertEquals("N", testService.validateUserYN("N"));
    }

    @Test
    void testValidateUserYNInvalid() {
        assertThrows(InvalidInputException.class, () -> testService.validateUserYN("A"));
    }

    @Test
    void testValidateAreaValid() throws InvalidInputException {
        BigDecimal area = testService.validateArea("150", false);
        assertEquals(new BigDecimal("150"), area);
    }

    @Test
    void testValidateAreaInvalid() {
        assertThrows(InvalidInputException.class, () -> testService.validateArea("50", false));
        assertThrows(InvalidInputException.class, () -> testService.validateArea("abc", false));
    }

    @Test
    void testCreateNewOrder() {
        Order order = testService.createNewOrder(LocalDate.of(2025, 12, 25), "John Doe", "TX", "Wood", new BigDecimal("200"));
        order.setOrderNumber(22);
        assertEquals(new BigDecimal("5.15"), order.getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.75"), order.getLaborCostPerSquareFoot());
        assertEquals(new BigDecimal("4.45"), order.getTaxRate());
        assertEquals(new BigDecimal("1030.00"), order.getMaterialCost());
        assertEquals(new BigDecimal("950.00"), order.getLaborCost());
        assertEquals(new BigDecimal("88.89"), order.getTax());
        assertEquals(new BigDecimal("2068.89"), order.getTotal());
    }

    @Test
    void testAddOrderToList() {
        Order order = testService.createNewOrder(LocalDate.of(2025, 12, 25), "John Doe", "TX", "Wood", new BigDecimal("200"));
        Order addedOrder = testService.addOrderToList(order);
        assertNotNull(addedOrder);
    }

    @Test
    void testUpdateOrder() {
        Order order = testService.createNewOrder(LocalDate.of(2025, 12, 25), "John Doe", "TX", "Wood", new BigDecimal("200"));
        testService.addOrderToList(order);
        order.setCustomerName("Jane Doe");
        Order updatedOrder = testService.updateOrder(order);
        assertEquals("Jane Doe", updatedOrder.getCustomerName());
    }

    @Test
    void testRemoveOrderFromList() {
        Order order = testService.createNewOrder(LocalDate.of(2025, 12, 25), "John Doe", "TX", "Wood", new BigDecimal("200"));
        testService.addOrderToList(order);
        Order removedOrder = testService.removeOrderFromList(order);
        assertNotNull(removedOrder);
    }

    @Test
    void testExportAllOrders() {
        assertDoesNotThrow(() -> testService.exportAllOrders());
    }
}