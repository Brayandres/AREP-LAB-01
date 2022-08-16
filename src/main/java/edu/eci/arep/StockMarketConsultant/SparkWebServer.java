package edu.eci.arep.StockMarketConsultant;

import com.google.gson.Gson;
import edu.eci.arep.StockMarketConsultant.cache.CacheMemory;
import edu.eci.arep.StockMarketConsultant.externalServices.ApiConnection;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeFrame;
import edu.eci.arep.StockMarketConsultant.externalServices.TimeInterval;
import edu.eci.arep.StockMarketConsultant.externalServices.impl.ApiConnectionAlphaVantage;
import edu.eci.arep.StockMarketConsultant.response.StandardResponse;
import edu.eci.arep.StockMarketConsultant.response.StatusResponse;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static spark.Spark.*;

public class SparkWebServer {

    private static final ApiConnection externalApiService = new ApiConnectionAlphaVantage();
    private static final CacheMemory cacheService = CacheMemory.getInstance();

    public static void main(String[] args) {
        staticFileLocation("/static");
        port(getPort());
        get("/checkStocks", SparkWebServer::getStockValuationHistory);
        get("/getTimeframes", SparkWebServer::getTimeframeValues);
        get("/getTimeIntervals", SparkWebServer::getTimeIntervalValues);
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    private static String getStockValuationHistory(Request request, Response response) {
        response.type("application/json");
        try {
            String stockName = request.queryParams("symbol");
            TimeFrame timeFrame = TimeFrame.valueOf(request.queryParams("function"));
            TimeInterval timeInterval = TimeInterval.valueOf(request.queryParams("interval"));
            String returnedData = cacheService.getRequestData(externalApiService, stockName + "/" + timeFrame + "/" + timeInterval);
            response.status(HttpStatus.OK_200);
            return returnedData;
        } catch (IOException e) {
            var statusCode = HttpStatus.INTERNAL_SERVER_ERROR_500;
            response.status(statusCode);
            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, statusCode, "INTERNAL_SERVER_ERROR"));
        } catch (IllegalArgumentException e) {
            var statusCode = HttpStatus.BAD_REQUEST_400;
            response.status(statusCode);
            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, statusCode, "Some parameter value is invalid"));
        } catch (Exception e) {
            var statusCode = HttpStatus.CONFLICT_409;
            response.status(statusCode);
            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, statusCode, e.getMessage()));
        }
    }

    private static String getTimeframeValues(Request request, Response response) {
        response.type("application/json");
        ArrayList<String> values = new ArrayList<>();
        Arrays.asList(TimeFrame.values()).forEach((TimeFrame timeFrame) ->
                values.add(timeFrame.toString())
        );
        String data = new Gson().toJson(values);
        response.status(HttpStatus.OK_200);
        return data;
    }

    private static String getTimeIntervalValues(Request request, Response response) {
        response.type("application/json");
        ArrayList<String> values = new ArrayList<>();
        Arrays.asList(TimeInterval.values()).forEach((TimeInterval timeInterval) ->
                values.add(timeInterval.toString())
        );
        String data = new Gson().toJson(values);
        response.status(HttpStatus.OK_200);
        return data;
    }
}