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

      // Delete the specific test file if it exists
      Path testFilePath = Paths.get(testFolderPath, testFileName);
      if (Files.exists(testFilePath)) {
        Files.delete(testFilePath);
      }

      testOrderDao = new OrderDaoImpl(testFolderPath, backupFolderPath);

      testOrderDao.loadOrders();

    }

    @Test
    void testLoadOrders() throws Exception {
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

      List<Order> orders = testOrderDao.getOrdersByDate(LocalDate.of(2025, 10, 23));
      Order retrievedOrder = orders.stream()
              .filter(order -> order.getOrderNumber() == 6)
              .findFirst()
              .orElse(null);


      assertEquals(testOrder.getOrderDate(), retrievedOrder.getOrderDate());
      assertEquals(testOrder.getState(), retrievedOrder.getState());
      assertEquals(testOrder.getOrderNumber(), retrievedOrder.getOrderNumber());
      assertEquals(testOrder.getProductType(), retrievedOrder.getProductType());
      assertEquals(testOrder.getMaterialCost(), retrievedOrder.getMaterialCost());
      assertEquals(testOrder.getArea(), retrievedOrder.getArea());
      assertEquals(testOrder.getTax(), retrievedOrder.getTax());
      assertEquals(testOrder.getTotal(), retrievedOrder.getTotal());
      assertEquals(testOrder.getTaxRate(), retrievedOrder.getTaxRate());
      assertEquals(testOrder.getTotal(), retrievedOrder.getTotal());


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
    testOrder.setProductType("Tile");
    testOrder.setMaterialCost(new BigDecimal("714.00"));
    testOrder.setArea(new BigDecimal("204"));
    testOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
    testOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
    testOrder.setLaborCost(new BigDecimal("846.60"));
    testOrder.setTax(new BigDecimal("600.00"));
    testOrder.setTotal(new BigDecimal("8505.27"));
    testOrderDao.addOrder(testOrder);

    testOrder.setCustomerName("Charlie Brown");

    testOrderDao.updateOrder(testOrder);

    assertEquals(testOrder.getCustomerName(), "Charlie Brown");
  }
  @Test
  public void testRemoveOrder() throws Exception {
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

    System.out.println(exportFilePath);
    List<String> lines = Files.readAllLines(exportFilePath);
    assertTrue(Files.exists(exportFilePath));

    assertFalse(lines.isEmpty(), "The file should not be empty.");

    String line1 = "6,Snoopy,TX,4.45,Tile,204,3.50,4.15,714.00,846.60,600.00,8505.27,2025-10-23";
    assertEquals(line1, lines.get(1));

    String line2 = "2,Doctor Who,WA,9.25,Wood,243.00,5.15,4.75,1154.25,1251.45,216.51,2622.21,2013-06-02";
    assertEquals(line2, lines.get(2));

    String line3 = "3,Albert Einstein,KY,6.00,Carpet,217.00,2.25,2.10,455.70,488.25,56.64,1000.59,2013-06-02";
    assertEquals(line3, lines.get(3));
  }

}