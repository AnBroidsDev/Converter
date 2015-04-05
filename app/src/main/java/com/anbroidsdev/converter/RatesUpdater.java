package com.anbroidsdev.converter;

import java.util.Map;

public class RatesUpdater {

    public interface OnRatesUpdateListener {

        public void onRatesUpdated();

    }

    private final MoneyConverter moneyConverter;
    private final OpenExchangeRatesApi openExchangeRatesApi;
    private final RatesSaver ratesSaver;

    public RatesUpdater(MoneyConverter moneyConverter, OpenExchangeRatesApi openExchangeRatesApi, RatesSaver ratesSaver) {
        this.moneyConverter = moneyConverter;
        this.openExchangeRatesApi = openExchangeRatesApi;
        this.ratesSaver = ratesSaver;
    }

    public void updateRates(final OnRatesUpdateListener listener) {
        openExchangeRatesApi.getLatestRates(moneyConverter.getBase().getCurrencyCode(), new OpenExchangeRatesApi.OnLatestRatesCallback() {

            @Override
            public void onLatestRates(Map<String, Double> rates, long timestamp) {
                final Rates newRates = new Rates(rates);

                ratesSaver.saveRates(newRates, timestamp);

                moneyConverter.setRates(newRates);

                if (listener != null) {
                    listener.onRatesUpdated();
                }
            }

        });
    }

}
