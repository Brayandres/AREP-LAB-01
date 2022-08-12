package edu.eci.arep.StockMarketConsultant.externalServices.impl;

import edu.eci.arep.StockMarketConsultant.externalServices.ApiConnection;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeFrame;

public class ApiConnectionAlphaVantage implements ApiConnection {

    private final String API_KEY = "STQM13VWU74MMD5H";

    @Override
    public String getStockValuationHistory(String stockName, TimeFrame timeFrame) {
        return null;
    }
}