package com.anbroidsdev.converter;

public interface RatesSaver {

    public void saveRates(Rates rates, long timestamp);
    public Rates getLastSavedRates();
    public long getLastSavedRatesTimestamp();

}
