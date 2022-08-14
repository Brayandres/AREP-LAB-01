package edu.eci.arep.StockMarketConsultant.externalServices;

public enum TimeFrame {

    INTRA_DAY("TIME_SERIES_INTRADAY"),
    DAILY("TIME_SERIES_DAILY"),
    WEEKLY("TIME_SERIES_WEEKLY"),
    MONTHLY("TIME_SERIES_MONTHLY");

    private final String label;

    private TimeFrame(String label) {
        this.label = label;
    }

    public String getValue() {
        return this.label;
    }
}