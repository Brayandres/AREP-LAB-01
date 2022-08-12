package edu.eci.arep.StockMarketConsultant.externalServices.impl;

import edu.eci.arep.StockMarketConsultant.externalServices.ApiConnection;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeFrame;

public class ApiConnectionAlphaVantage implements ApiConnection {

    @Override
    public String getStockValuationHistory(String stockName, TimeFrame timeFrame) {
        return null;
    }
}