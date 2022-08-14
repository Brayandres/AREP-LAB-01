package edu.eci.arep.StockMarketConsultant;

import static spark.Spark.*;

public class SparkWebServer {

    public static void main(String[] args) {
        staticFileLocation("/static");
        port(getPort());
        get("/hello", (req, res) -> "Hello Heroku");
    }

    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}