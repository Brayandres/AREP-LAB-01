package edu.eci.arep.StockMarketConsultant.cache;

public class CacheMemory {

    private static volatile CacheMemory instance;
    private static final Object mutex = new Object();

    private CacheMemory() {

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
}