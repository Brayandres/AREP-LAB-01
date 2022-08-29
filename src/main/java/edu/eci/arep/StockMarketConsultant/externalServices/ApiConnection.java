package edu.eci.arep.StockMarketConsultant.externalServices;

import java.io.IOException;
import java.net.MalformedURLException;

public interface ApiConnection {

    String getStockValuationHistory(String stockName, TimeFrame timeFrame, TimeInterval timeInterval) throws IOException;

    String getIterablePropertyNameFromResponseJSON(String stockName, TimeFrame timeFrame, TimeInterval timeInterval);
}