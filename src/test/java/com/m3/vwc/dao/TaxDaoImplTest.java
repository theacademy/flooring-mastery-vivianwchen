package com.m3.vwc.dao;

import com.m3.vwc.dto.Tax;
import com.m3.vwc.service.FlooringServiceLayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxDaoImplTest {

    private TaxDao testTaxDao;
    public TaxDaoImplTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testTaxDao = ctx.getBean("taxDao", TaxDaoImpl.class);
    }

    @Test
    void testLoadProducts() throws DaoPersistenceException, IOException {
        testTaxDao.loadTax();

        List<String> tax = testTaxDao.getAllStateNames();
        assertFalse(tax.isEmpty(), "Tax list should be empty.");
        assertTrue(tax.size() > 0, "Tax are present.");
    }

    @Test
    void testGetTax(){
        assertNotNull(testTaxDao.getTax("TX"), "Tax info for TX should be present.");
        assertNotNull(testTaxDao.getTax("WA"), "Tax info for WA should be present.");
        assertNotNull(testTaxDao.getTax("KY"), "Tax info for KY should be present.");
        assertNotNull(testTaxDao.getTax("CA"), "Tax info for CA should be present.");
    }

    @Test
    void testGetAllStateNames(){
        assertNotNull(testTaxDao.getAllStateNames());
        assertTrue(testTaxDao.getAllStateNames().contains("TX"), "TX should be present.");
        assertTrue(testTaxDao.getAllStateNames().contains("WA"), "WA should be present.");
        assertTrue(testTaxDao.getAllStateNames().contains("KY"), "KY should be present.");
        assertTrue(testTaxDao.getAllStateNames().contains("CA"), "CA should be present.");
    }

    @Test
    void testGetTaxRate(){
        assertEquals(new BigDecimal("4.45"), testTaxDao.getTaxRate("TX"));
        assertEquals(new BigDecimal("9.25"), testTaxDao.getTaxRate("WA"));
        assertEquals(new BigDecimal("6.00"), testTaxDao.getTaxRate("KY"));
        assertEquals(new BigDecimal("25.00"), testTaxDao.getTaxRate("CA"));

    }
}