package edu.eci.arep.StockMarketConsultant.externalServices;

public enum TimeFrame {

    INTRA_DAY(""),
    DIARY(""),
    WEEKLY(""),
    MONTHLY("");

    public final String label;

    private TimeFrame(String label) {
        this.label = label;
    }
}