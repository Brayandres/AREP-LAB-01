package edu.eci.arep.StockMarketConsultant.cache;

import edu.eci.arep.StockMarketConsultant.externalServices.ApiConnection;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeFrame;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeInterval;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class CacheMemory {

    private static volatile CacheMemory instance;
    private static final Object mutex = new Object();
    private final ConcurrentHashMap<String, Hashtable<String, String>> memory;
    private final ApiConnection externalApi;
    private final DateTimeFormatter formatter;
    private final int dataLifetimePolicyInMinutes;

    private CacheMemory(ApiConnection externalApi, int dataLifetimePolicyInMinutes) {
        memory = new ConcurrentHashMap<>();
        this.externalApi = externalApi;
        this.dataLifetimePolicyInMinutes = dataLifetimePolicyInMinutes;
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    }

    public static CacheMemory getInstance(ApiConnection externalApi, int dataDurationInMinutes) {
        CacheMemory result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    instance = result = new CacheMemory(externalApi, dataDurationInMinutes);
                }
            }
        }
        return instance;
    }

    public String getRequestData(String requestIdentifier) throws IOException {
        String response;
        if (!isDataStored(requestIdentifier)) {
            storeData(requestIdentifier);
        }
        response = prepareResponse(requestIdentifier);
        return response;
    }

    private boolean isDataStored(String dataIdentifier) {
        return memory.containsKey(dataIdentifier);
    }

    private void storeData(String dataIdentifier) throws IOException {
        String[] requestParams = dataIdentifier.split("/");
        String stockName = requestParams[0];
        TimeFrame timeFrame = TimeFrame.valueOf(requestParams[1]);
        TimeInterval timeInterval = (!Objects.equals(requestParams[2], "null")) ? TimeInterval.valueOf(requestParams[2]) : null;
        String requestResponse = externalApi.getStockValuationHistory(stockName, timeFrame, timeInterval);
        String requiredProperty = externalApi.getIterablePropertyNameFromResponseJSON(stockName, timeFrame, timeInterval);
        Hashtable<String, String> fullResponse = new Hashtable<>();
        fullResponse.put("response", requestResponse);
        fullResponse.put("property", requiredProperty);
        fullResponse.put("savedOn", formatter.format(LocalDateTime.now()));
        memory.put(dataIdentifier, fullResponse);
    }

    private Hashtable<String, String> retrieveData(String dataIdentifier) throws IOException {
        Hashtable<String, String> data = memory.get(dataIdentifier);
        LocalDateTime dataCreationDate = LocalDateTime.parse(data.get("savedOn"), formatter);
        if (Duration.between(dataCreationDate, LocalDateTime.now()).toMinutes() > dataLifetimePolicyInMinutes) {
            storeData(dataIdentifier);
            data = memory.get(dataIdentifier);
        }
        return data;
    }

    private String prepareResponse(String dataIdentifier) throws IOException {
        Hashtable<String, String> fullResponse = retrieveData(dataIdentifier);
        return "" +
                "[" +
                    "{" +
                        "\"property\": \"" + fullResponse.get("property") +
                    "\"}," +
                    "{" +
                        "\"response\": " + fullResponse.get("response") +
                    "}" +
                "]";
    }
}