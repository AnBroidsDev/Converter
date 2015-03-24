package com.anbroidsdev.converter;

import java.util.Map;

public interface OpenExchangeRatesApi {

    public void getLatestRates(String base, OnLatestRatesCallback callback);

    public interface OnLatestRatesCallback {

        public void onLatestRates(Map<String, Double> rates, long timestamp);

    }

}
