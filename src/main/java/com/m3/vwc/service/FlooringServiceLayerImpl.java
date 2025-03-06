package com.m3.vwc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import com.m3.vwc.dao.*;

import com.m3.vwc.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class FlooringServiceLayerImpl implements FlooringServiceLayer {
    private OrderDao orderDao;
    private TaxDao taxDao;
    private ProductDao productDao;

    public FlooringServiceLayerImpl(OrderDao orderDao, TaxDao taxDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
    }

    public List<Order> validateOrderExists(LocalDate date) throws InvalidInputException{
        try{
            return orderDao.getOrdersByDate(date);
        }
        catch(Exception e){
            throw new InvalidInputException("No orders exist for this date.");
        }
    }

    @Override
    public LocalDate validateDate(String date, boolean newOrder) throws InvalidInputException{
        try {
            LocalDate orderDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            if (newOrder){
                if (orderDate.isBefore(LocalDate.now())) {
                    throw new InvalidInputException("Date must be in the future");
                }
            }
            return orderDate;
        } catch (DateTimeParseException e){
            throw new InvalidInputException("Invalid input. Please enter a valid date in mm/dd/yyyy format.");
        }
    }

    public String validateName(String name, boolean allowEmpty) throws InvalidInputException{
        if (name.trim().isEmpty()){
            if (allowEmpty){
                return name;
            }
            else{
                throw new InvalidInputException("Invalid input. Please enter a valid name.");
            }
        }
        if (!name.matches("[a-zA-Z0-9., ]+")){
            throw new InvalidInputException("Name must contain only letters, numbers, periods, commas, and spaces.");
        }
        return name;
    }

    @Override
    public String validateState(String state, boolean allowEmpty) throws InvalidInputException{
        List<String> stateNames = taxDao.getAllStateNames();
        if (state.trim().isEmpty()){
            if (allowEmpty){
                return state;
            }
            else{
                throw new InvalidInputException("Invalid input. State cannot be empty.");
            }
        }
        if (!stateNames.contains(state)) {
            throw new InvalidInputException("Invalid state. We do not deliver here.");
        }
        return state;
    }

    @Override
    public String validateProduct(String type, boolean allowEmpty) throws InvalidInputException{
        List<String> productList = productDao.getAllProductNames();
        if (type.trim().isEmpty()){
            if (allowEmpty){
                return type;
            }
            else{
                throw new InvalidInputException("Invalid input. Product type cannot be empty.");
            }
        }
        if (!productList.contains(type)) {
            throw new InvalidInputException("Invalid product. Please enter a valid product type.");
        }
        return type;
    }

    @Override
    public String validateUserYN(String input) throws InvalidInputException{
        if (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N")) {
            throw new InvalidInputException("Invalid input. Please enter Y or N.");
        }
        return input;
    }
    public int validateOrderNum(String orderNum) throws InvalidInputException{
        try {
            return Integer.parseInt(orderNum);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid order number.");
        }
    }

    public BigDecimal validateArea(String area, boolean allowEmpty) throws InvalidInputException{
        try{
            if (area.trim().isEmpty()){
                if (allowEmpty){
                    return BigDecimal.ZERO;
                }
                else{
                    throw new  InvalidInputException("Invalid input. Area cannot be empty.");
                }
            }
            BigDecimal areaData = new BigDecimal(area);
            if (areaData.compareTo(BigDecimal.valueOf(100)) < 0){
                throw new InvalidInputException("Invalid area. Please enter an area greater than 100.");
            }
            return areaData;
        }catch(NumberFormatException e){
            throw new InvalidInputException("Invalid area input. Please enter a positive decimal.");
        }

    }

    @Override
    public Order createNewOrder(LocalDate orderDate, String customerName, String state, String type, BigDecimal area){
        Order order = new Order(orderDate, customerName, state, type, area);
        BigDecimal currProductCost = productDao.getProductCost(type);
        BigDecimal currLaborCost = productDao.getProductLaborCost(type);
        BigDecimal currTaxRate = taxDao.getTaxRate(state);

        order.setCostPerSquareFoot(currProductCost);
        order.setLaborCostPerSquareFoot(currLaborCost);
        order.setTaxRate(currTaxRate);

        // Correct tax rate calculation
        BigDecimal taxRate = currTaxRate.divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);

        BigDecimal materialCost = order.getArea()
                .multiply(currProductCost)
                .setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal laborCost = order.getArea()
                .multiply(currLaborCost)
                .setScale(2, RoundingMode.HALF_EVEN);

        // Use taxRate instead of currTaxRate directly
        BigDecimal taxCost = (materialCost.add(laborCost))
                .multiply(taxRate)
                .setScale(2, RoundingMode.HALF_EVEN);

        BigDecimal totalCost = (materialCost.add(laborCost)
                .add(taxCost))
                .setScale(2, RoundingMode.HALF_EVEN);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(taxCost);
        order.setTotal(totalCost);

        return order;
    }


    @Override
    public Order getOrderByDateAndNumber(LocalDate date, int orderNum){
        return orderDao.getOrderByDateAndNumber(date, orderNum);
    }
    @Override
    public List<Product> getProducts(){
        return productDao.getAllProducts();
    }


    @Override
    public Order addOrderToList(Order order){
        return orderDao.addOrder(order);
    }

    @Override
    public Order updateOrder(Order order){
        return orderDao.updateOrder(order);
    }

    @Override
    public Order removeOrderFromList(Order order){
        return orderDao.removeOrder(order);
    }

    @Override
    public void exportAllOrders(){
        orderDao.exportAllData();
    }

}
