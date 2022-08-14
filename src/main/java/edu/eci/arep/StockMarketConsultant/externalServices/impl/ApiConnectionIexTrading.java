package edu.eci.arep.StockMarketConsultant.externalServices.impl;

import edu.eci.arep.StockMarketConsultant.externalServices.ApiConnection;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeInterval;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeFrame;

import java.io.IOException;
import java.net.MalformedURLException;

public class ApiConnectionIexTrading implements ApiConnection {
    
    @Override
    public String getStockValuationHistory(String stockName, TimeFrame timeFrame, TimeInterval timeInterval)  throws MalformedURLException, IOException {
        return null;
    }
}