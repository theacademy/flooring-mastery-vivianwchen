package com.m3.vwc.ui;

import com.m3.vwc.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class FlooringView {
    private UserIO io;

    @Autowired
    public FlooringView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection(){
        io.print("****************** Menu Selection ******************");
        io.print("              * 1. Display Orders");
        io.print("              * 2. Add an Order");
        io.print("              * 3. Edit an Order");
        io.print("              * 4. Remove an Order");
        io.print("              * 5. Export All Data");
        io.print("              * 6. Quit");
        io.print("****************************************************");
        io.print("");

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public String promptDate(){
        return io.readString("Please enter order date (mm/dd/yyyy): ");
    }

    public String promptCustomerName(){
        return io.readString("Please enter customer name: ");
    }

    public String promptState(){
        return io.readString("Please enter state: ");
    }

    public String promptType(){
        return io.readString("Please enter product type: ");
    }

    public String promptArea(){
        return io.readString("Please enter area (min. 100 sq. ft): ");
    }

    public void displayAllOrders(List<Order> orderList){
        for (Order order : orderList) {
            displayOrderSummary(order);
        }
    }
    public void displayOrderSummary(Order order){
        io.print("  Customer: " + order.getCustomerName());
        io.print("      Date: " + order.getOrderDate());
        io.print("      State: " + order.getState());
        io.print("      Product Type: " + order.getProductType());
        io.print("      Area: " + order.getArea());
        io.print("      Cost per Sq. Ft.: $" + order.getCostPerSquareFoot());
        io.print("      Labor Cost per Sq. Ft.: $" + order.getLaborCostPerSquareFoot());
        io.print("      Material Cost: $"  + order.getMaterialCost());
        io.print("      Labor Cost: $" + order.getLaborCost());
        io.print("      Tax: $" + order.getTax());
        io.print("      Total Cost: $" + order.getTotal());
        io.print("");
    }

    public void displayProducts(List<Product> productList){
        io.print("****************************** PRODUCT LIST ******************************");
        io.print("Product       Cost per Sq. Ft.     Labor Cost Per Sq. Ft.");
        for (Product product : productList){
            io.print(product.getProductType() + "           " + product.getCostPerSqFt() + "                     " + product.getLaborCostPerSqFt());
        }
        io.print("**************************************************************************");

    }
    public String promptUserPlaceOrder(){
        return io.readString("Would you like to place your order? y/n");
    }

    public void displayMessage(String message) {
        io.print(message);
    }

    public void displayOrderSuccessMessage(Order order){
        io.print("Success! Order has been placed. Order Number #" + order.getOrderNumber());
    }

    public void displayOrderErrorMessage(){
        io.print("Error. Order could not be placed.");
    }

    public void displayOrderUnsuccessfulMessage(){
        io.print("Order was not placed.");
    }

    public String promptOrderNumber(){
        return io.readString("Please enter order number: ");
    }

    public String promptUpdatedName(String name){
        return io.readString("Please enter updated customer name (" + name + "): ");
    }

    public String promptUpdatedState(String state){
        return io.readString("Please enter updated state (" + state + "): ");
    }

    public String promptUpdatedType(String type){
        return io.readString("Please enter updated customer name (" + type + "): ");
    }

    public String promptUpdatedArea(BigDecimal area){
        return io.readString("Please enter updated area (" + area + "):");
    }

    public String promptUseSaveData(){
        return io.readString("Would you like to save these updates? y/n ");
    }

    public void displayUpdatedOrderMsg(){
        io.print("Here is the new order information:");
    }

    public void displayUpdateSuccessMsg(){
        io.print("Success! Order has been updated.");
    }

    public void displayUpdateUnsuccessfulMsg(){
        io.print("Error. Order could not be updated.");
    }

    public String promptRemovalMsg(){
        return io.readString("Are you sure you'd like to remove this order? y/n ");
    }

    public void displayRemovalSuccessMsg(){
        io.print("Success! Order has been removed.");
    }

    public void displayRemovalUnsuccessfulMsg(){
        io.print("Order was not removed.");
    }

    public void displayRemovalErrorMsg(){
        io.print("Error. Order could not be removed.");
    }

    public void displayOrderNotFound(){
        io.print("Order could not be found.");
    }
    public void displayUpdatingOrderMsg(){
        io.print("Here is the order information to be updated:");
    }
}
