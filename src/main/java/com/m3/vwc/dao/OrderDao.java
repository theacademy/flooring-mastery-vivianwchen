package com.m3.vwc.dao;

import com.m3.vwc.dto.Order;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface OrderDao {
    void loadOrders() throws IOException;
    Order addOrder(Order order) throws DaoPersistenceException;
    void writeOrders();
    List<Order> getOrdersByDate(LocalDate date);
    Order updateOrder(Order order);
    Order removeOrder(Order order);
    void deleteOrderFile(LocalDate date) throws DaoPersistenceException;
    void exportAllData();
}
