package edu.eci.arep.StockMarketConsultant;

import edu.eci.arep.StockMarketConsultant.externalServices.StockSymbols;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeFrame;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeInterval;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class SparkWebServerTest {

    private final String PATH = "http://localhost:4567/checkStocks";
    //private final String PATH = "*Heroku*";
    private final TimeInterval[] intervals = TimeInterval.values();
    private final StockSymbols[] symbols = StockSymbols.values();
    private final TimeFrame[] functions = TimeFrame.values();
    private final List<MakeRequestThread> threads = new ArrayList<>();
    private final int threadsQty = 10;
    private final int requestsPerThread = 2;

    private ArrayList<String[]> generateCaseForRequest(int requestsQty) {
        ArrayList<String[]> requestParams = new ArrayList<>();
        for (int i = 0; i < requestsQty; i++) {
            String function = functions[new Random().nextInt(functions.length)].name();
            String symbol = symbols[new Random().nextInt(symbols.length)].name();
            String interval = intervals[new Random().nextInt(intervals.length)].name();
            requestParams.add(new String[]{function, symbol, interval});
        }
        return requestParams;
    }

    private void startThreads() {
        for (MakeRequestThread thread: threads) {
            System.out.println(" -- Starting thread: "+thread.getThread().getName());
            thread.getThread().start();
        }
        /*for (MakeRequestThread thread: threads) {
            try {
                thread.getThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

    @Test
    public void testingTestClass() {
        ArrayList<String[]> res = generateCaseForRequest(10);
        for (String[] params: res) {
            System.out.println("Case: "+Arrays.asList(params));
        }
        assertEquals(1 + 1, 2);
    }

    @Test
    public void testingServerConcurrencyFeature() {
        System.out.println("\n--------------------------------------------\n--------------------------------------------");
        for (int i = 0; i < threadsQty; i++) {
            threads.add(new MakeRequestThread(PATH, generateCaseForRequest(requestsPerThread)));
        }
        startThreads();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\nRESULTS: "+MakeRequestThread.getResultsOfThreadRequests());
    }
}