package edu.eci.arep.StockMarketConsultant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class MakeRequestThread implements Runnable {

    private static final ConcurrentHashMap<String, Boolean> resultsOfThreadRequests = new ConcurrentHashMap<>();

    private final Object mutex = new Object();
    private final Thread ownThread;
    private boolean isPaused;
    private final String PATH;

    public MakeRequestThread(String path) {
        isPaused = false;
        this.PATH = path;
        ownThread = new Thread(this);
    }

    @Override
    public void run() {
        synchronized (mutex) {
            while(isPaused) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            makeRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeRequest() throws IOException {
        // Creating request
        URL url = new URL(PATH);
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
