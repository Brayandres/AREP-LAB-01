package edu.eci.arep.StockMarketConsultant.externalServices;

public interface ApiConnection {

    String getStockValuationHistory(String stockName, TimeFrame timeFrame);

}