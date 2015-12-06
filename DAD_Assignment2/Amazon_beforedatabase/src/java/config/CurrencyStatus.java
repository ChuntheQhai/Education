/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author ChunQhai
 */
public class CurrencyStatus {
    
    private  String currencyStatus = "";
    
    Connection con;
    private CurrencyStatus() {
    }
    
    public static CurrencyStatus getInstance() {
        return CurrencyStatusHolder.INSTANCE;
    }
    
    public  void setCurrencyStatus(String newCurrency){
        this.currencyStatus = newCurrency;
    }
    
    public String getCurrencyStatus() throws Exception{
        return currencyStatus;
    }
    
    
    private static class CurrencyStatusHolder {

        private static final CurrencyStatus INSTANCE = new CurrencyStatus();
    }
}
