package com.anbroidsdev.converter;

public class SharedPreferencesRatesSaver implements RatesSaver {

    private static final String RATES = "rates";
    private static final String TIMESTAMP = "timestamp";

    private final ObjectSharedPreferences sharedPreferences;

    private Rates lastSavedRates;
    private long lastSavedRatesTimestamp;

    public SharedPreferencesRatesSaver(ObjectSharedPreferences sharedPreferences) {
        this.sharedPreferences =  sharedPreferences;

        lastSavedRates = sharedPreferences.getObject(RATES, Rates.class);
        lastSavedRatesTimestamp = sharedPreferences.getLong(TIMESTAMP, 0L);
    }

    public void saveRates(Rates rates, long timestamp) {
        lastSavedRates = rates;
        lastSavedRatesTimestamp = timestamp;

        sharedPreferences.edit().putObject(RATES, rates);
        sharedPreferences.edit().putLong(TIMESTAMP, timestamp);
        sharedPreferences.edit().apply();
    }

    public Rates getLastSavedRates() {
        return lastSavedRates;
    }

    public long getLastSavedRatesTimestamp() {
        return lastSavedRatesTimestamp;
    }

    public void clear() {
        lastSavedRates = null;
        lastSavedRatesTimestamp = 0;

        sharedPreferences.edit().remove(RATES);
        sharedPreferences.edit().remove(TIMESTAMP);
        sharedPreferences.edit().apply();
    }

}
