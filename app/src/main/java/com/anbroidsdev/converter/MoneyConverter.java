package com.anbroidsdev.converter;

import java.util.Currency;

/**
 * Created by david on 23/2/15.
 */
public class MoneyConverter {

    private Currency base;
    private Rates rates;

    public MoneyConverter(Currency base, Rates rates) {
        setBase(base);
        setRates(rates);
    }

    public void setBase(Currency base) {
        if (base == null) {
            throw new IllegalArgumentException("Base cannot be null");
        }

        if (rates != null) {
            if (!rates.containsCurrency(base)) {
                throw new IllegalArgumentException("Currency is not supported");
            }

            final Double inverseRate = 1.0 / rates.get(base).getValue();

            rates.remove(base);

            for (Rate rate : rates.list()) {
                rate.setValue(inverseRate * rate.getValue());
            }

            rates.put(new Rate(this.base, inverseRate));
        }

        this.base = base;
    }

    public Currency getBase() {
        return base;
    }

    public void setRates(Rates rates) {
        checkRates(rates);

        this.rates = rates;
    }

    public Rates getRates() {
        return rates;
    }

    public double convert(double amount, Currency currency) {
        return convert(amount, currency, base);
    }

    public double convert(double amount, Currency currency, Currency base) {
        if (!rates.containsCurrency(currency) && currency != this.base) {
            throw new IllegalArgumentException("Currency is not supported");
        }

        if (!rates.containsCurrency(base) && base != this.base) {
            throw new IllegalArgumentException("Base is not supported");
        }

        if (currency == base) {
            throw new IllegalArgumentException("Currency and Base cannot be the same");
        }

        final Double rate;
        if (currency == this.base) {
            rate = 1.0 / rates.get(base).getValue();
        } else if (base == this.base) {
            rate = rates.get(currency).getValue();
        } else {
            rate = 1.0 / rates.get(base).getValue() * rates.get(currency).getValue();
        }

        return rate * amount;
    }

    private void checkRates(Rates rates) {
        if (rates == null || rates.isEmpty()) {
            throw new IllegalArgumentException("Rates cannot be null or empty");
        }

        if (base != null && rates.containsCurrency(base)) {
            throw new IllegalArgumentException("Rates cannot contain the Base currency");
        }

        for (Rate rate : rates.list()) {
            if (rate.getValue() == null) {
                throw new IllegalArgumentException("Rates cannot have any null value");
            }

            if (rate.getValue() < 0) {
                throw new IllegalArgumentException("Rates cannot have any negative value");
            }
        }
    }

}
