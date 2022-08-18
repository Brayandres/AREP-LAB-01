package edu.eci.arep.StockMarketConsultant;

import edu.eci.arep.StockMarketConsultant.externalServices.StockSymbols;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeFrame;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeInterval;
import org.awaitility.Awaitility;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SparkWebServerTest {

    private final String PATH = "http://localhost:4567/checkStocks";
    //private final String PATH = "*Heroku*";
    private final TimeInterval[] intervals = TimeInterval.values();
    private final StockSymbols[] symbols = StockSymbols.values();
    private final TimeFrame[] functions = TimeFrame.values();
    private final List<MakeRequestThread> threads = new ArrayList<>();
    private final int threadsQty = 10;
    private final int requestsPerThread = 5;

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
        for (MakeRequestThread thread: threads) {
            try {
                thread.getThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testingServerConcurrencyFeature() {
        for (int i = 0; i < threadsQty; i++) {
            threads.add(new MakeRequestThread(PATH, generateCaseForRequest(requestsPerThread)));
        }
        startThreads();

        Awaitility.await().until(() -> MakeRequestThread.getResultsOfThreadRequests().size() == threadsQty * requestsPerThread);

        AtomicBoolean wasAllRequestSuccessfully = new AtomicBoolean(true);
        Map<String, Boolean> requestsResults = MakeRequestThread.getResultsOfThreadRequests();
        Collection<Boolean> allRequestsStatus = requestsResults.values();
        allRequestsStatus.forEach(wasRequestsSuccessfully ->
            wasAllRequestSuccessfully.set(wasAllRequestSuccessfully.get() && wasRequestsSuccessfully)
        );

        assertNotNull(requestsResults);
        assertEquals(threadsQty * requestsPerThread, requestsResults.size());
        assertEquals(threadsQty * requestsPerThread, allRequestsStatus.size());
        assertTrue(wasAllRequestSuccessfully.get());
    }
}