package com.anbroidsdev.converter;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class MoneyConverterRatesUpdater {

    public interface OnRatesUpdateListener {

        public void onRatesUpdated(Map<Currency, Double> rates, long timestamp);

    }

    private final MoneyConverter moneyConverter;
    private final OpenExchangeRatesApi openExchangeRatesApi;

    public MoneyConverterRatesUpdater(MoneyConverter moneyConverter, OpenExchangeRatesApi openExchangeRatesApi) {
        this.moneyConverter = moneyConverter;
        this.openExchangeRatesApi = openExchangeRatesApi;
    }

    public void updateRates(final OnRatesUpdateListener listener) {
        openExchangeRatesApi.getLatestRates(moneyConverter.getBase().getCurrencyCode(), new OpenExchangeRatesApi.OnLatestRatesCallback() {

            @Override
            public void onLatestRates(Map<String, Double> rates, long timestamp) {
                final Map<Currency, Double> newRates = new HashMap<>(rates.size());

                for (Map.Entry<String, Double> rate : rates.entrySet()) {
                    try {
                        newRates.put(Currency.getInstance(rate.getKey()), rate.getValue());
                    } catch (IllegalArgumentException e) {}
                }

                moneyConverter.setRates(newRates);

                if (listener != null) {
                    listener.onRatesUpdated(newRates, timestamp);
                }
            }

        });
    }

}
