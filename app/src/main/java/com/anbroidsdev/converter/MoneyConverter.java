package com.anbroidsdev.converter;

import java.util.Currency;
import java.util.Map;

/**
 * Created by david on 23/2/15.
 */
public class MoneyConverter {

    private Currency base;
    private final Map<Currency, Double> rates;

    public MoneyConverter(Currency base, Map<Currency, Double> rates) {
        if (base == null) {
            throw new IllegalArgumentException("Base cannot be null");
        }

        if (rates == null || rates.isEmpty()) {
            throw new IllegalArgumentException("Rates cannot be null or empty");
        }

        for (Map.Entry<Currency, Double> entry : rates.entrySet()) {
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("Rates cannot have any null value");
            }
        }

        this.base = base;
        this.rates = rates;
    }

    public void setBase(Currency currency) {
        if (!rates.containsKey(currency)) {
            throw new IllegalArgumentException("Currency is not supported");
        }

        final Double inverseRate = 1.0 / rates.get(currency);

        rates.remove(currency);

        for (Map.Entry<Currency, Double> entry : rates.entrySet()) {
            entry.setValue(inverseRate * entry.getValue());
        }

        rates.put(base, inverseRate);

        base = currency;
    }

    public Currency getBase() {
        return base;
    }

    public Map<Currency, Double> getRates() {
        return rates;
    }

    public double convert(double amount, Currency currency) {
        return convert(amount, currency, base);
    }

    public double convert(double amount, Currency currency, Currency base) {
        if (!rates.containsKey(currency) && currency != this.base) {
            throw new IllegalArgumentException("Currency is not supported");
        }

        if (!rates.containsKey(base) && base != this.base) {
            throw new IllegalArgumentException("Base is not supported");
        }

        if (currency == base) {
            throw new IllegalArgumentException("Currency and Base cannot be the same");
        }

        final Double rate;
        if (currency == this.base) {
            rate = 1.0 / rates.get(base);
        } else if (base == this.base) {
            rate = rates.get(currency);
        } else {
            rate = 1.0 / rates.get(base) * rates.get(currency);
        }

        return rate * amount;
    }

}
