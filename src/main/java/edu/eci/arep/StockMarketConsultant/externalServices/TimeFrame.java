package edu.eci.arep.StockMarketConsultant.externalServices;

public enum TimeFrame {

    INTRA_DAY("intraday"),
    DIARY("diary"),
    WEEKLY("weekly"),
    MONTHLY("monthly");

    public final String label;

    private TimeFrame(String label) {
        this.label = label;
    }

    public String getValue() {
        return this.label;
    }
}