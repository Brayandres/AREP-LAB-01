package edu.eci.arep.StockMarketConsultant.externalServices;

public enum TimeInterval {

    ONE_MIN("1min"),
    FIVE_MIN("5min"),
    FIFTEEN_MIN("15min"),
    THIRTY_MIN("30min"),
    SIXTY_MIN("60min");

    private final String label;

    private TimeInterval(String value) {
        this.label = value;
    }

    public String getValue() {
        return this.label;
    }
}
