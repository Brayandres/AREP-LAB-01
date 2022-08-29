package edu.eci.arep.StockMarketConsultant.cache;

import edu.eci.arep.StockMarketConsultant.externalServices.ApiConnection;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeFrame;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeInterval;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class CacheMemory {

    private static volatile CacheMemory instance;
    private static final Object mutex = new Object();
    private final ConcurrentHashMap<String, String> memory;

    private CacheMemory() {
        memory = new ConcurrentHashMap<>();
    }

    public static CacheMemory getInstance() {
        CacheMemory result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    instance = result = new CacheMemory();
                }
            }
        }
        return instance;
    }

    public String getRequestData(ApiConnection externalApi, String requestIdentifier) throws IOException {
        String response;
        if (!isDataStored(requestIdentifier)) {
            storeData(requestIdentifier, externalApi);
        }
        response = retrieveData(requestIdentifier);
        return response;
    }

    private boolean isDataStored(String dataIdentifier) {
        return memory.containsKey(dataIdentifier);
    }

    private void storeData(String dataIdentifier, ApiConnection externalApi) throws IOException {
        String[] requestParams = dataIdentifier.split("/");
        String stockName = requestParams[0];
        TimeFrame timeFrame = TimeFrame.valueOf(requestParams[1]);
        TimeInterval timeInterval = (!Objects.equals(requestParams[2], "null")) ? TimeInterval.valueOf(requestParams[2]) : null;
        String requestResponse = externalApi.getStockValuationHistory(stockName, timeFrame, timeInterval);
        memory.put(dataIdentifier, requestResponse);
    }

    private String retrieveData(String dataIdentifier) {
        return memory.get(dataIdentifier);
    }
}