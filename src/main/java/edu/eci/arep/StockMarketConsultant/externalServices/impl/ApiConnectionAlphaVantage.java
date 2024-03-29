package edu.eci.arep.StockMarketConsultant.externalServices.impl;

import edu.eci.arep.StockMarketConsultant.externalServices.ApiConnection;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeInterval;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConnectionAlphaVantage implements ApiConnection {

    private final String HOST;
    private final String API_KEY;

    public ApiConnectionAlphaVantage() {
        HOST = "https://www.alphavantage.co/";
        API_KEY = "STQM13VWU74MMD5H";
    }

    @Override
    public String getStockValuationHistory(String stockName, TimeFrame timeFrame, TimeInterval timeInterval) throws IOException {
        var function = timeFrame.getValue();
        var uri = (timeFrame != TimeFrame.INTRA_DAY) ?
                "query?function=" + function + "&symbol=" + stockName + "&datatype=json" + "&apikey=" + API_KEY:
                "query?function=" + function + "&symbol=" + stockName + "&interval=" + timeInterval.getValue() + "&datatype=json" + "&apikey=" + API_KEY;

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

        return content.toString();
    }

    @Override
    public String getIterablePropertyNameFromResponseJSON(String stockName, TimeFrame timeFrame, TimeInterval timeInterval) {
        var complement = "Time Series";
        var property = "";
        if (timeFrame == TimeFrame.INTRA_DAY) {
            property = complement + " (" + timeInterval.getValue() + ")";
        } else if (timeFrame == TimeFrame.DAILY) {
            var tf = timeFrame.toString();
            property = complement + " (" + tf.charAt(0)+tf.substring(1).toLowerCase() + ")";
        }
        else {
            var tf = timeFrame.toString();
            property = tf.charAt(0)+tf.substring(1).toLowerCase() + " " + complement;
        }
        return property;
    }
}