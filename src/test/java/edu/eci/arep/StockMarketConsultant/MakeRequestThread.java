package edu.eci.arep.StockMarketConsultant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MakeRequestThread implements Runnable {

    private static final ConcurrentHashMap<String, Boolean> resultsOfThreadRequests = new ConcurrentHashMap<>();

    private final Object mutex = new Object();
    private final List<String[]> requestsParams;
    private final Thread ownThread;
    private boolean isPaused;
    private final String HOST;

    public MakeRequestThread(String host, ArrayList<String[]> requestsParams) {
        isPaused = false;
        this.HOST = host;
        this.requestsParams = requestsParams;
        ownThread = new Thread(this);
    }

    @Override
    public void run() {
        for (int i = 0; i < requestsParams.size(); i++) {
            synchronized (mutex) {
                while (isPaused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            boolean wasProcessed;
            String[] paramsSet = requestsParams.get(i);
            try {
                makeRequest(paramsSet[0], paramsSet[1], paramsSet[2]);
                wasProcessed = true;
            } catch (Exception e) {
                wasProcessed = false;
                e.printStackTrace();
            }
            resultsOfThreadRequests.put(ownThread.getName() +"/case="+i+"/params="+paramsSet[0]+"-"+paramsSet[1]+"-"+paramsSet[2], wasProcessed);
        }
    }

    private void makeRequest(String function, String symbol, String interval) throws IOException {
        // Setting uri
        var uri = "?function=" + function + "&symbol=" + symbol + "&interval=" + interval;
        // Creating request
        URL url = new URL(HOST + uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        // Setting request properties
        connection.setRequestProperty("Content-Type", "application/json");
        // Reading response
        StringBuilder content = new StringBuilder();
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            content.append(inputLine);
        }
        input.close();
        connection.disconnect();
        // Prepare response
        String response = content.toString();
    }

    public static ConcurrentHashMap<String, Boolean> getResultsOfThreadRequests() {
        return resultsOfThreadRequests;
    }

    public Thread getThread() {
        return ownThread;
    }

    public synchronized void resumeNow() {
        isPaused = false;
        notifyAll();
    }

    public synchronized void pauseNow() {
        isPaused = true;
        notifyAll();
    }

    public synchronized boolean isPaused() {
        return isPaused;
    }
}
