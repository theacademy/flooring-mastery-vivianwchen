package com.m3.vwc.controller;

import com.m3.vwc.ui.*;
import com.m3.vwc.dto.*;
import com.m3.vwc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component("controller")
public class FlooringController {
    private FlooringView view;
    private FlooringServiceLayer service;

    @Autowired
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
                    LocalDate displayDate = getDate();
                    List<Order> orderList = getAllOrdersForDate(displayDate);
                    displayOrders(orderList);
                    break;
                case 2:
                    LocalDate orderDate = getOrderDateInput();
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
                    LocalDate editDate = getDate();
                    int orderNum = getOrderNum();
                    Order currOrder = getOrderByNum(editDate, orderNum);
                    String newName = getUpdatedName(currOrder.getCustomerName());
                    String newState = getUpdatedState(currOrder.getState());
                    String newType = getUpdatedType(currOrder.getProductType());
                    BigDecimal newArea = getUpdatedArea(currOrder.getArea());


                    String updatedName = newName.isEmpty() ? currOrder.getCustomerName() : newName;
                    String updatedState = newState.isEmpty() ? currOrder.getState() : newState;
                    String updatedType = newType.isEmpty() ? currOrder.getProductType() : newType;
                    BigDecimal updatedArea = (newArea.compareTo(BigDecimal.ZERO) == 0) ? currOrder.getArea() : newArea;
                    Order updatedOrder = createNewOrder(currOrder.getOrderDate(), updatedName, updatedState, updatedType, updatedArea);
                    view.displayUpdatedOrderMsg();
                    displayCurrentOrder(updatedOrder);
                    getSaveUpdatedData();
                    break;
                case 4:
                    //Remove order
                    LocalDate removalDate = getDate();
                    int removalOrderNum = getOrderNum();
                    Order removalOrder = getOrderByNum(removalDate, removalOrderNum);
                    displayCurrentOrder(removalOrder);
                    removeOrder(removalOrder);
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

    public LocalDate getOrderDateInput(){
        while(true){
            String dateInput = view.promptDate();
            try{
                return service.validateNewDate(dateInput);
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
                view.displayOrderSuccessMessage();
            }
            else{
                view.displayOrderErrorMessage();
            }
        }
        else{
            view.displayOrderUnsuccessfulMessage();
        }
    }

    public LocalDate getDate(){
        while (true) {
            String date = view.promptDate();
            try{
                return service.validateDate(date);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }

    public List<Order> getAllOrdersForDate(LocalDate date){
        try{
            return service.validateOrderExists(date);
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
    public Order getOrderByNum(LocalDate date, int orderNum){
       List<Order> orderList = getAllOrdersForDate(date);
       try{
           return service.validateOrderExistsOnDate(orderList, orderNum);
       }catch(InvalidInputException e){
           view.displayMessage(e.getMessage());
       }
       return null;
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
                return service.validateState(state, true);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }

    public String getUpdatedType(String type){
        while (true){
            String updatedType = view.promptUpdatedType(type);
            try{
                return service.validateProduct(type, true);
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

    public String getSaveUpdatedData(){
        while (true){
            String userInput = view.promptUseSaveData();
            try{
                return service.validateUserYN(userInput);
            }catch(InvalidInputException e){
                view.displayMessage(e.getMessage());
            }
        }
    }

    public void updateEditedOrder(Order order){
        if (service.updateOrder(order) != null){
            view.displayUpdateSuccessMsg();
        }else{
         view.displayUpdateUnsuccessfulMsg();
        }

    }
    public void removeOrder(Order order){
        String input = view.promptRemovalMsg();
        if (input.equalsIgnoreCase("Y")){
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
        } catch(Exception e){
            view.displayMessage(e.getMessage());
        }
    }

}
