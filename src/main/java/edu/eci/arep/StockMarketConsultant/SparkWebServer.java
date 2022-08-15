package edu.eci.arep.StockMarketConsultant;

import com.google.gson.Gson;
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

import static spark.Spark.*;

public class SparkWebServer {

    private static final ApiConnection service = new ApiConnectionAlphaVantage();

    public static void main(String[] args) {
        staticFileLocation("/static");
        port(getPort());
        get("/checkStocks", SparkWebServer::getStockValuationHistory);
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
            System.out.println("\nGet Body: {symbol="+stockName+", function="+timeFrame+", interval="+timeInterval+"}");
            response.status(HttpStatus.OK_200);
            return service.getStockValuationHistory(stockName, timeFrame, timeInterval);
        } catch (IOException e) {
            var statusCode = HttpStatus.INTERNAL_SERVER_ERROR_500;
            response.status(statusCode);
            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, statusCode, "Service temporarily unavailable"));
        } catch (IllegalArgumentException e) {
            var statusCode = HttpStatus.BAD_REQUEST_400;
            response.status(statusCode);
            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, statusCode, "Some parameter name is invalid"));
        } catch (Exception e) {
            var statusCode = HttpStatus.CONFLICT_409;
            response.status(statusCode);
            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, statusCode, e.getMessage()));
        }
    }
}