package com.m3.vwc.controller;

import com.m3.vwc.dao.DaoPersistenceException;
import com.m3.vwc.ui.*;
import com.m3.vwc.dto.*;
import com.m3.vwc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringController {
    private FlooringView view;
    private FlooringServiceLayer service;

    FlooringController(FlooringView view, FlooringServiceLayer service){
        this.view = view;
        this.service = service;
    }
    public void run(){
        boolean keepGoing = true;
        while (keepGoing){
            int menuSelection = getMenuSelection();

            switch(menuSelection){
                case 1:
                    LocalDate displayDate = getCurrentOrderDate();
                    List<Order> orderList = getAllOrdersForDate(displayDate);
                    displayOrders(orderList);
                    break;
                case 2:
                    LocalDate orderDate = getNewOrderDateInput();
                    String customerName = getCustomerName();
                    String state = getState();
                    displayCurrentProducts();
                    String type = getType();
                    BigDecimal area = getArea();

                    Order order = createNewOrder(orderDate, customerName, state, type, area);
                    displayCurrentOrder(order);
                    addOrder(order);
                    break;
                case 3:
                    // Edit an Order
                    LocalDate editDate = getCurrentOrderDate();
                    int orderNum = getOrderNum();
                    Order currOrder = getSpecificOrder(editDate, orderNum);
                    if (currOrder != null) {
                        displayUpdateOrderMsg();
                        displayCurrentOrder(currOrder);
                        String newName = getUpdatedName(currOrder.getCustomerName());
                        String newState = getUpdatedState(currOrder.getState());
                        String newType = getUpdatedType(currOrder.getProductType());
                        BigDecimal newArea = getUpdatedArea(currOrder.getArea());

                        String updatedName = newName.isEmpty() ? currOrder.getCustomerName() : newName;
                        String updatedState = newState.isEmpty() ? currOrder.getState() : newState;
                        String updatedType = newType.isEmpty() ? currOrder.getProductType() : newType;
                        BigDecimal updatedArea = (newArea.compareTo(BigDecimal.ZERO) == 0) ? currOrder.getArea() : newArea;
                        Order updatedOrder = createNewOrder(currOrder.getOrderDate(), updatedName, updatedState, updatedType, updatedArea);
                        updatedOrder.setOrderNumber(currOrder.getOrderNumber());

                        view.displayUpdatedOrderMsg();
                        displayCurrentOrder(updatedOrder);
                        String userInput = getIfUserSavesUpdated();
                        updateEditedOrder(updatedOrder, userInput);
                    }
                    break;

                case 4:
                    //Remove order
                    LocalDate removalDate = getCurrentOrderDate();
                    int removalOrderNum = getOrderNum();
                    Order removalOrder = getSpecificOrder(removalDate, removalOrderNum);
                    if (removalOrder != null) {
                        displayCurrentOrder(removalOrder);
                        String userInput = getIfUserRemovesOrder();
                        removeOrder(removalOrder, userInput);
                    }
                    break;
                case 5:
                    // Export Data
                    exportData();

                    break;
                case 6:
                    // Exit
                    keepGoing = false;
                    break;
            }
        }
    }

    public Order createNewOrder(LocalDate date, String name, String state, String type, BigDecimal area) {
        Order order = service.createNewOrder(date, name, state, type, area);
        return order;
    }

    public LocalDate getNewOrderDateInput(){
        while(true){
            String dateInput = view.promptDate();
            try{
                return service.validateDate(dateInput, true);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }
    public String getCustomerName(){
        while(true){
            String customerName = view.promptCustomerName();
            try{
                return service.validateName(customerName, false);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }
    public String getState(){
        while(true){
            String state = view.promptState();
            try{
                return service.validateState(state, false);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }
    public String getType(){
        while(true){
            String type = view.promptType();
            try{
                return service.validateProduct(type, false);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }
    public BigDecimal getArea(){
        while(true){
            String area = view.promptArea();
            try{
                return service.validateArea(area, false);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }

    public void displayCurrentProducts(){
        List<Product> productList = service.getProducts();
        view.displayProducts(productList);
    }

    public void displayCurrentOrder(Order order){
        view.displayOrderSummary(order);
    }

    public String getUserYN(){
        while (true){
            String userInput = view.promptUserPlaceOrder();
            try{
                return service.validateUserYN(userInput);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }

    public void addOrder(Order order){
        String input = getUserYN();
        if (input.equalsIgnoreCase("Y")){
            if (service.addOrderToList(order) != null){
                view.displayOrderSuccessMessage(order);
            }
            else{
                view.displayOrderErrorMessage();
            }
        }
        else{
            view.displayOrderUnsuccessfulMessage();
        }
    }

    public LocalDate getCurrentOrderDate(){
        while (true) {
            String date = view.promptDate();
            try{
                return service.validateDate(date, false);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }

    public List<Order> getAllOrdersForDate(LocalDate date){
        try{
            return service.validateOrdersExists(date);
        }catch(Exception e){
            view.displayMessage(e.getMessage());
        }
        return null;
    }

    public int getMenuSelection(){
        return view.printMenuAndGetSelection();
    }


    public void displayOrders(List<Order> orderList){
        if (orderList != null) {
            view.displayAllOrders(orderList);
        }
    }
    public int getOrderNum(){
        while (true){
            String num = view.promptOrderNumber();
            try{
                return service.validateOrderNum(num);
            }
            catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }

    }

    public String getUpdatedName(String name){
        while(true){
            String updatedName = view.promptUpdatedName(name);
            try{
                return service.validateName(updatedName, true);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }

    public String getUpdatedState(String state){
        while(true){
            String updatedState = view.promptUpdatedState(state);
            try{
                return service.validateState(updatedState, true);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }

    public String getUpdatedType(String type){
        while (true){
            String updatedType = view.promptUpdatedType(type);
            try{
                return service.validateProduct(updatedType, true);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }

    public BigDecimal getUpdatedArea(BigDecimal area){
        while (true){
            String updatedArea = view.promptUpdatedArea(area);;
            try{
                return service.validateArea(updatedArea, true);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }

    public String getIfUserRemovesOrder(){
        while (true){
            String userInput = view.promptRemovalMsg();
            try{
                return service.validateUserYN(userInput);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }

    public String getIfUserSavesUpdated(){
        while (true){
            String userInput = view.promptUseSaveData();
            try{
                return service.validateUserYN(userInput);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }
    public void updateEditedOrder(Order order, String userInput) {
        if (userInput.equalsIgnoreCase("Y")) {
            if (service.updateOrder(order) != null) {
                view.displayUpdateSuccessMsg();
            }
        } else {
            view.displayUpdateUnsuccessfulMsg();
        }

    }
    public void removeOrder(Order order, String userInput){
        if (userInput.equalsIgnoreCase("Y")){
            if (service.removeOrderFromList(order) != null){
                view.displayRemovalSuccessMsg();
            }
            else{
                view.displayRemovalErrorMsg();
            }
        }
        else{
            view.displayRemovalUnsuccessfulMsg();
        }
    }

    public void exportData(){
        try {
            service.exportAllOrders();
            view.displayExportSuccess();
        } catch(Exception e){
            view.displayMessage(e.getMessage());
        }
    }

    public Order getSpecificOrder(LocalDate date,  int orderNum){
        try {
            Order order = service.getOrderByDateAndNumber(date, orderNum);
            return order;
        }catch(DaoPersistenceException e){
            view.displayMessage(e.getMessage());
        }
        return null;
    }

    public void displayUpdateOrderMsg(){
        view.displayUpdatingOrderMsg();
    }

}
