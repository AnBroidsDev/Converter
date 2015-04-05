package com.anbroidsdev.converter;

import java.util.Arrays;
import java.util.Currency;

public class Rate {

    private Currency currency;
    private Double value;

    public Rate(Currency currency, Double value) {
        if (currency == null) {
            throw new IllegalArgumentException("currency cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("value cannot less than 0");
        }

        this.currency = currency;
        this.value = value;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate that = (Rate) o;
        return currency.equals(that.currency) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{currency, value});
    }

}
