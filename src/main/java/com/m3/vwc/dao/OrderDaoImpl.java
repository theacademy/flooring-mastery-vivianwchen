package com.m3.vwc.dao;

import com.m3.vwc.dto.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class OrderDaoImpl implements OrderDao {
    private Map<LocalDate, List<Order>> orders = new HashMap<>();
    private String folderPath = "src/main/resources/Orders";
    public static final String DELIMITER = ",";
    private static int orderCounter = 0;

    public OrderDaoImpl() {
        loadOrders();
    }
    @Override
    public void loadOrders() throws DaoPersistenceException {
        orders.clear();

        try {
            Files.list(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            List<String> lines = Files.readAllLines(file);
                            LocalDate date = getOrderDate(file.getFileName().toString());
                            boolean isHeader = true;
                            for (String line : lines) {
                                if (isHeader){
                                    isHeader = false;
                                    continue;
                                }
                                Order order = unmarshallOrder(line);
                                order.setOrderDate(date);
                                orders.computeIfAbsent(date, key -> new ArrayList()).add(order);
                                orderCounter = Math.max(orderCounter, order.getOrderNumber());
                            }
                        } catch (IOException e) {
                            throw new DaoPersistenceException("Could not read order file: " + file.getFileName());
                        }
                    });
            orderCounter++;
        } catch (IOException e) {
            throw new DaoPersistenceException("Could not find any orders.");
        }

    }

    @Override
    public Order addOrder(Order order) throws DaoPersistenceException {
        order.setOrderNumber(orderCounter++);
        orders.computeIfAbsent(order.getOrderDate(), key -> new ArrayList()).add(order);
        writeOrders();
        return order;
    }

    @Override
    public LocalDate getOrderDate(String fileName){
        String date = fileName.substring(7, 15);
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("MMddyyyy"));
    }

    @Override
    public void writeOrders() throws DaoPersistenceException {
        String folderPath = "src/main/resources/Orders";

        for (LocalDate date : orders.keySet()) {
            String dateString = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
            String fileName = "Orders_" + dateString + ".txt";
            Path filePath = Paths.get(folderPath, fileName);

            try (PrintWriter out = new PrintWriter(new FileWriter(filePath.toFile()))) {
                // Write header
                out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

                // Write all orders for this date
                for (Order order : orders.get(date)) {
                    String currentOrder = marshallOrder(order);
                    out.println(currentOrder);
                }
                out.flush();  // Ensure all data is written
            } catch (IOException e) {
                throw new DaoPersistenceException("Could not write order file: " + filePath);
            }
        }
    }
    @Override
    public List<Order> getOrdersByDate(LocalDate date){
        return new ArrayList<>(orders.get(date));
    }

    @Override
    public Order updateOrder(Order order){
        List<Order> listOrder = orders.get(order.getOrderDate());
        if (listOrder != null) {
            for (int i = 0; i < listOrder.size(); i++) {
                if (listOrder.get(i).getOrderNumber() == order.getOrderNumber()) {
                    listOrder.set(i, order);
                    return order;
                }
            }
        }
        writeOrders();
        return null;
    }

    @Override
    public Order removeOrder(Order order){
        List<Order> listOrder = orders.get(order.getOrderDate());
        if (listOrder != null) {
            for (int i = 0; i < listOrder.size(); i++) {
                if (listOrder.get(i).getOrderNumber() == order.getOrderNumber()) {
                    listOrder.remove(i);
                    if (listOrder.isEmpty()){
                        orders.remove(order.getOrderDate());
                        deleteOrderFile(order.getOrderDate());
                    }
                    writeOrders();
                    return order;
                }
            }
        }
        writeOrders();
        return null;
    }
    @Override
    public void exportAllData() throws DaoPersistenceException {
        String backupFolderPath = "src/main/resources/Backup";
        Path backupFolder = Paths.get(backupFolderPath);
        Path filePath = Paths.get(backupFolderPath, "DataExport.txt");
        boolean isOverwritten = false;
        try {
            Files.createDirectories(backupFolder);

            boolean appendMode = Files.exists(filePath) && isOverwritten;

            try (PrintWriter out = new PrintWriter(new FileWriter(filePath.toFile(), appendMode))) {
                if (!appendMode) {
                    out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate");
                }

                for (LocalDate date : orders.keySet()) {
                    for (Order order : orders.get(date)) {
                        String currentOrder = marshallOrder(order);
                        out.print(currentOrder);
                        out.println(DELIMITER + date.toString());
                    }
                }
                out.flush();
            }

            isOverwritten= true;
        } catch (IOException e) {
            throw new DaoPersistenceException("Could not export data.");
        }
    }

    @Override
    public void deleteOrderFile(LocalDate date) throws DaoPersistenceException {
        String dateString = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        String fileName = "Orders_" + dateString + ".txt";
        Path filePath = Paths.get(folderPath, fileName);

        try{
            Files.deleteIfExists(filePath);
        }catch(IOException e){
            throw new DaoPersistenceException("Could not delete file: " + filePath);
        }
    }

    public String marshallOrder(Order order){
        String orderAsText = order.getOrderNumber() + DELIMITER;
        orderAsText += order.getCustomerName() + DELIMITER;
        orderAsText += order.getState() + DELIMITER;
        orderAsText += order.getTaxRate() + DELIMITER;
        orderAsText += order.getProductType() + DELIMITER;
        orderAsText += order.getArea() + DELIMITER;
        orderAsText += order.getCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getLaborCostPerSquareFoot() + DELIMITER;
        orderAsText += order.getMaterialCost() + DELIMITER;
        orderAsText += order.getLaborCost() + DELIMITER;
        orderAsText += order.getTax() + DELIMITER;
        orderAsText += order.getTotal();
        return orderAsText;
    }


    public Order unmarshallOrder(String line){
        String[] parts = (line.split(DELIMITER));

        int orderNum = Integer.parseInt(parts[0]);
        String customerName = parts[1];
        String state =  parts[2];
        BigDecimal taxRate = new BigDecimal(parts[3]);
        String productType = parts[4];
        BigDecimal area = new BigDecimal(parts[5]);
        BigDecimal costPerSquareFoot = new BigDecimal(parts[6]);
        BigDecimal laborCostPerSquareFoot = new BigDecimal(parts[7]);
        BigDecimal materialCost = new BigDecimal(parts[8]);
        BigDecimal laborCost = new BigDecimal(parts[9]);
        BigDecimal tax =  new BigDecimal(parts[10]);
        BigDecimal total = new BigDecimal(parts[11]);

        Order order = new Order(orderNum, customerName, state, taxRate, productType, area, costPerSquareFoot, laborCostPerSquareFoot, laborCost, materialCost, tax, total);
        return order;
    }

}
