package com.anbroidsdev.converter;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by david on 23/2/15.
 */
public class MoneyConverter {

    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final String[] SUPPORTED_CURRENCY_CODES = {
            "EUR",
            "USD",
            "GBP",
            "JPY",
    };
    private String base;
    private Map<String,Double> rates;

    public MoneyConverter(String base, Map<String, Double> rates) {

        if (base == null || base.isEmpty())
            throw new IllegalArgumentException("Base can not be null or empty");

        if (!isValidCurrencyCode(base))
            throw new IllegalArgumentException("Invalid Base currency code");

        if (rates == null || rates.isEmpty())
            throw new IllegalArgumentException("Rates can no be null or empty");

        if (!isValidCurrencyCodes(rates))
            throw new IllegalArgumentException("At least one currency is not valid in Rates");

        this.base = base;
        this.rates = rates;
    }



    public void setBase(String newBase) {

        if (!isValidCurrencyCode(newBase)){
            throw new IllegalArgumentException("Base conversion is not valid");
        }
        if (!isSupportedCurrencyCode(newBase)){
            throw new IllegalArgumentException("Base conversion is not supported");
        }

        Double value = rates.get(newBase);

        if (value == null)
            throw new IllegalArgumentException("Rate not available");

        for (String currency : rates.keySet()){
            rates.put(currency,rates.get(currency)/value);
        }

        rates.put(this.base,1.0/value);
        rates.remove(newBase);

        this.base = newBase;
    }

    public double convert(double amount,String currency){

        if (amount < 0)
            throw new IllegalArgumentException("Amount to convert must be greater than 0");

        if (!isValidCurrencyCode(currency))
            throw new IllegalArgumentException("Currency code is not valid");

        if (!isSupportedCurrencyCode(currency))
            throw new IllegalArgumentException("Currency code is not supported");

        double convertedValue = rates.get(currency) * amount;

        return convertedValue;
    }

    private boolean isValidCurrencyCode(String code){

        if (code == null || code.length() != CURRENCY_CODE_LENGTH)
            return false;

        return true;
    }

    private boolean isSupportedCurrencyCode(String code){

        if (code == null || !Arrays.asList(SUPPORTED_CURRENCY_CODES).contains(code))
            return false;

        return true;
    }

    private boolean isValidCurrencyCodes(Map<String, Double> rates) {

        for (String key : rates.keySet()){
            if (!isValidCurrencyCode(key))
                return false;
        }
        return true;
    }
}
