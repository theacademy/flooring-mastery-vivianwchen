package com.m3.vwc.service;

import com.m3.vwc.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringServiceLayer {
    LocalDate validateNewDate(String date) throws InvalidInputException;

    String validateName(String name, boolean allowEmpty) throws InvalidInputException;

    String validateState(String state, boolean allowEmpty) throws InvalidInputException;

    String validateProduct(String type, boolean allowEmpty) throws InvalidInputException;

    List<Order> validateOrderExists(LocalDate date) throws InvalidInputException;

    Order validateOrderExistsOnDate(List<Order> orderList, int orderNum) throws InvalidInputException;

    LocalDate validateDate(String date) throws InvalidInputException;

    int validateOrderNum(String orderNum) throws InvalidInputException;

    BigDecimal validateArea(String area, boolean allowEmpty) throws InvalidInputException;

    String validateUserYN(String input) throws InvalidInputException;

    Order createNewOrder(LocalDate orderDate, String customerName, String state, String type, BigDecimal area);

    Order addOrderToList(Order order);

    List<Order> getAllOrdersOnDate(LocalDate date);

    Order updateOrder(Order order);

    Order removeOrderFromList(Order order);

    void exportAllOrders();

    List<Product> getProducts();

}

