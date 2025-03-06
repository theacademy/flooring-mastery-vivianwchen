package com.m3.vwc.dao;

import com.m3.vwc.dto.*;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Component
public class TaxDaoImpl implements TaxDao{
    Map<String, Tax> stateTaxes = new HashMap<>();

    public TaxDaoImpl() throws DaoPersistenceException {
        try{
            loadTax();
        }catch(IOException e){
            throw new DaoPersistenceException("Could not load tax file.");
        }
    }
    @Override
    public void loadTax() throws IOException {
        Scanner sc = new Scanner(
                new BufferedReader(new FileReader("src/main/resources/Data/Taxes.txt")));
        if (sc.hasNextLine()){
            sc.nextLine();
        }
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(",");
            String abbr = parts[0];
            String name = parts[1];
            BigDecimal rate = new BigDecimal(parts[2]);

            Tax tax = new Tax(abbr, name, rate);
            stateTaxes.put(abbr, tax);
        }
    }
    @Override
    public Tax getTax(String state){
        return stateTaxes.get(state);
    }
    @Override
    public List<String> getAllStateNames() {
        return new ArrayList<>(stateTaxes.keySet());
    }
    @Override
    public BigDecimal getTaxRate(String state){
        return stateTaxes.get(state).getTaxRate();
    }
}
