package com.anbroidsdev.converter;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rates {

    private final Map<Currency, Rate> rates;

    public Rates() {
        this.rates = new HashMap<>();
    }

    public Rates(Map<String, Double> rates) {
        this.rates = new HashMap<>(rates.size());

        for (Map.Entry<String, Double> rate : rates.entrySet()) {
            try {
                final Currency currency = Currency.getInstance(rate.getKey());
                this.rates.put(currency, new Rate(currency, rate.getValue()));
            } catch (IllegalArgumentException e) {}
        }
    }

    public boolean containsCurrency(Currency currency) {
        return rates.containsKey(currency);
    }

    public void put(Rate rate) {
        rates.put(rate.getCurrency(), rate);
    }

    public Rate get(Currency currency) {
        return rates.get(currency);
    }

    public Rate remove(Currency currency) {
        return rates.remove(currency);
    }

    public boolean isEmpty() {
        return rates.isEmpty();
    }

    public Rates copy() {
        final Rates copyRates = new Rates();

        for (Map.Entry<Currency, Rate> entry : rates.entrySet()) {
            copyRates.put(new Rate(entry.getValue().getCurrency(), entry.getValue().getValue()));
        }

        return copyRates;
    }

    public List<Rate> list() {
        final List<Rate> list = new ArrayList<>(rates.size());

        for (Map.Entry<Currency, Rate> entry : rates.entrySet()) {
            list.add(entry.getValue());
        }

        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rates that = (Rates) o;
        return rates.equals(that.rates);
    }

    @Override
    public int hashCode() {
        return rates.hashCode();
    }

}
