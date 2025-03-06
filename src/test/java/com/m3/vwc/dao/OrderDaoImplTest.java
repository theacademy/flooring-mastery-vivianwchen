package com.m3.vwc.dao;

import com.m3.vwc.dto.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoImplTest {
  private OrderDao testOrderDao;
  private Order testOrder;

  public OrderDaoImplTest() {
  }

    @BeforeEach
    public void setUp() throws Exception {
      String testFolderPath = "src/test/resources/OrdersTest";
      String backupFolderPath = "src/test/resources/Backup";
      String testFileName = "Orders_10232025.txt";

      // Delete the specific test file if it exists so orders are not overwritten.
      Path testFilePath = Paths.get(testFolderPath, testFileName);
      if (Files.exists(testFilePath)) {
        Files.delete(testFilePath);
      }

      testOrderDao = new OrderDaoImpl(testFolderPath, backupFolderPath);
    }

    @Test
    void testLoadOrders() throws Exception {
      testOrderDao.loadOrders();
      List<Order> orders = testOrderDao.getOrdersByDate(LocalDate.of(2013, 06, 02));
      assertEquals(2, orders.size());
    }

    @Test
    void testAddOrder() throws Exception {
      Order testOrder = new Order(LocalDate.of(2025, 10, 23), "Snoopy", "TX", "Laminate", new BigDecimal("180"));
      testOrder.setTaxRate(new BigDecimal("4.45"));
      testOrder.setOrderNumber(6);
      testOrder.setProductType("Tile");
      testOrder.setMaterialCost(new BigDecimal("1.26"));
      testOrder.setArea(new BigDecimal("204"));
      testOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
      testOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
      testOrder.setMaterialCost(new BigDecimal("714.00"));
      testOrder.setLaborCost(new BigDecimal("846.60"));
      testOrder.setTax(new BigDecimal("600.00"));
      testOrder.setTotal(new BigDecimal("8505.27"));
      testOrderDao.addOrder(testOrder);

      Order retrievedOrder = testOrderDao.getOrderByDateAndNumber(LocalDate.of(2025, 10, 23), 6);
      assertNotNull(retrievedOrder, "Our retrieved order should be not be null");

      assertEquals(testOrder.getOrderDate(), retrievedOrder.getOrderDate(), "Our retrieved order's date should be the same");
      assertEquals(testOrder.getCustomerName(), retrievedOrder.getCustomerName(),  "Our retrieved order's customer name should be the same");
      assertEquals(testOrder.getState(), retrievedOrder.getState(),  "Our retrieved order's state should be the same");
      assertEquals(testOrder.getTaxRate(), retrievedOrder.getTaxRate(), "Our retrieved order's tax rate should be the same");
      assertEquals(testOrder.getProductType(), retrievedOrder.getProductType(),  "Our retrieved order's product type should be the same");
      assertEquals(testOrder.getArea(), retrievedOrder.getArea(), "Our retrieved order's area should be the same");
      assertEquals(testOrder.getCostPerSquareFoot(), retrievedOrder.getCostPerSquareFoot(), "Our  retrieved order's cost per square foot should be the same");
      assertEquals(testOrder.getLaborCostPerSquareFoot(), retrievedOrder.getLaborCostPerSquareFoot(), "Our  retrieved order's labor foot should be the same");
      assertEquals(testOrder.getMaterialCost(), retrievedOrder.getMaterialCost(), "Our retrieved order's materialCost should be the same");
      assertEquals(testOrder.getLaborCost(), retrievedOrder.getLaborCost(), "Our retrieved order's laborCost should be the same");
      assertEquals(testOrder.getTax(), retrievedOrder.getTax(), "Our retrieved order's tax should be the same");
      assertEquals(testOrder.getTotal(), retrievedOrder.getTotal(), "Our retrieved order's total should be the same");
      assertEquals(testOrder.getOrderNumber(), retrievedOrder.getOrderNumber(),  "Our retrieved order's order number should be the same");

    }
  @Test
  void testWriteOrders() throws Exception {
    String testFolderPath = "src/test/resources/OrdersTest";
    String testFileName = "Orders_10232025.txt";
    Path testFilePath = Paths.get(testFolderPath, testFileName);


    if (Files.exists(testFilePath)) {
      Files.delete(testFilePath);
    }


    Order testOrder = new Order(LocalDate.of(2025, 10, 23), "Snoopy", "TX", "Laminate", new BigDecimal("180"));
    testOrder.setTaxRate(new BigDecimal("4.45"));
    testOrder.setOrderNumber(6);
    testOrder.setProductType("Tile");
    testOrder.setMaterialCost(new BigDecimal("714.00"));
    testOrder.setArea(new BigDecimal("204"));
    testOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
    testOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
    testOrder.setLaborCost(new BigDecimal("846.60"));
    testOrder.setTax(new BigDecimal("600.00"));
    testOrder.setTotal(new BigDecimal("8505.27"));
    testOrderDao.addOrder(testOrder);


    testOrderDao.writeOrders();


    assertTrue(Files.exists(testFilePath), "The order file should be created.");


    List<String> lines = Files.readAllLines(testFilePath);
    assertFalse(lines.isEmpty(), "The file should not be empty.");
    assertEquals("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total", lines.get(0), "Header should match.");

    String expectedOrderLine = "6,Snoopy,TX,4.45,Tile,204,3.50,4.15,714.00,846.60,600.00,8505.27";
    assertEquals(expectedOrderLine, lines.get(1), "The order line should match the expected format.");

    Files.delete(testFilePath);
  }

  @Test
  void testGetOrdersByDate(){
    Order testOrder = new Order(LocalDate.of(2025, 10, 23), "Snoopy", "TX", "Laminate", new BigDecimal("180"));
    testOrder.setTaxRate(new BigDecimal("4.45"));
    testOrder.setOrderNumber(6);
    testOrder.setProductType("Tile");
    testOrder.setMaterialCost(new BigDecimal("714.00"));
    testOrder.setArea(new BigDecimal("204"));
    testOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
    testOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
    testOrder.setLaborCost(new BigDecimal("846.60"));
    testOrder.setTax(new BigDecimal("600.00"));
    testOrder.setTotal(new BigDecimal("8505.27"));
    testOrderDao.addOrder(testOrder);

    List<Order> orderList = testOrderDao.getOrdersByDate(LocalDate.of(2025, 10, 23));
    assertEquals(orderList.get(0).getCustomerName(), "Snoopy");
    assertEquals(orderList.size(), 1);
  }
  @Test
  void testUpdateOrder() throws Exception {
    Order testOrder = new Order(LocalDate.of(2025, 10, 23), "Snoopy", "TX", "Laminate", new BigDecimal("180"));
    testOrder.setTaxRate(new BigDecimal("4.45"));
    testOrder.setOrderNumber(6);
    testOrder.setProductType("Wood");
    testOrder.setMaterialCost(new BigDecimal("714.00"));
    testOrder.setArea(new BigDecimal("204"));
    testOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
    testOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
    testOrder.setLaborCost(new BigDecimal("846.60"));
    testOrder.setTax(new BigDecimal("600.00"));
    testOrder.setTotal(new BigDecimal("8505.27"));
    testOrderDao.addOrder(testOrder);

    testOrder.setCustomerName("Charlie Brown");
    testOrder.setProductType("Tile");

    testOrderDao.updateOrder(testOrder);

    assertEquals(testOrder.getCustomerName(), "Charlie Brown");
    assertEquals(testOrder.getProductType(), "Tile");
  }
  @Test
  void testRemoveOrder() throws Exception {
    Order testOrder = new Order(LocalDate.of(2025, 10, 23), "Snoopy", "TX", "Laminate", new BigDecimal("180"));
    testOrder.setTaxRate(new BigDecimal("4.45"));
    testOrder.setOrderNumber(6);
    testOrder.setProductType("Tile");
    testOrder.setMaterialCost(new BigDecimal("1.26"));
    testOrder.setArea(new BigDecimal("204"));
    testOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
    testOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
    testOrder.setMaterialCost(new BigDecimal("714.00"));
    testOrder.setLaborCost(new BigDecimal("846.60"));
    testOrder.setTax(new BigDecimal("600.00"));
    testOrder.setTotal(new BigDecimal("8505.27"));
    testOrderDao.addOrder(testOrder);

    Order removedOrder = testOrderDao.removeOrder(testOrder);
    assertNotNull(removedOrder, "The removed over should not be null.");
    assertEquals(testOrder, removedOrder, "The removed over should be the same object.");
  }


  @Test
  void testExportAllData() throws DaoPersistenceException, IOException {
    String backupFolderPath = "src/test/resources/Backup";
    String exportFileName = "DataExport.txt";
    Path exportFilePath = Paths.get(backupFolderPath, exportFileName);

    Order testOrder = new Order(LocalDate.of(2025, 10, 23), "Snoopy", "TX", "Laminate", new BigDecimal("180"));
    testOrder.setTaxRate(new BigDecimal("4.45"));
    testOrder.setOrderNumber(6);
    testOrder.setProductType("Tile");
    testOrder.setMaterialCost(new BigDecimal("714.00"));
    testOrder.setArea(new BigDecimal("204"));
    testOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
    testOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
    testOrder.setLaborCost(new BigDecimal("846.60"));
    testOrder.setTax(new BigDecimal("600.00"));
    testOrder.setTotal(new BigDecimal("8505.27"));
    testOrderDao.addOrder(testOrder);

    testOrderDao.exportAllData();

    List<String> lines = Files.readAllLines(exportFilePath);
    assertTrue(Files.exists(exportFilePath));

    assertFalse(lines.isEmpty(), "The file should not be empty.");

    String line1 = "6,Snoopy,TX,4.45,Tile,204,3.50,4.15,714.00,846.60,600.00,8505.27,2025-10-23";
    assertEquals(line1, lines.get(1));

    String line2 = "2,Doctor Who,WA,9.25,Wood,243.00,5.15,4.75,1251.45,1154.25,216.51,2622.21,2013-06-02";
    assertEquals(line2, lines.get(2));

    String line3 = "3,Albert Einstein,KY,6.00,Carpet,217.00,2.25,2.10,488.25,455.70,56.64,1000.59,2013-06-02";
    assertEquals(line3, lines.get(3));
  }


}