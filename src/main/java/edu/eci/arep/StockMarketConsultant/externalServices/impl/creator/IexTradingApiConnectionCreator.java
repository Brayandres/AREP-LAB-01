package edu.eci.arep.StockMarketConsultant.externalServices.impl.creator;

import edu.eci.arep.StockMarketConsultant.externalServices.ApiConnection;
import edu.eci.arep.StockMarketConsultant.externalServices.ApiConnectionCreator;
import edu.eci.arep.StockMarketConsultant.externalServices.impl.ApiConnectionIexTrading;

public class IexTradingApiConnectionCreator implements ApiConnectionCreator {

    @Override
    public ApiConnection createApiConnection() {
        return new ApiConnectionIexTrading();
    }
}