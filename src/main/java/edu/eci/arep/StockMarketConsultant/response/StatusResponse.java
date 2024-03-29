package edu.eci.arep.StockMarketConsultant.response;

public enum StatusResponse {

    SUCCESS("Success"),
    ERROR("Error");

    private final String status;

    private StatusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
