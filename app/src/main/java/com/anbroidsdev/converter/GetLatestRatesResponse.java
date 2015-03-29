package com.anbroidsdev.converter;

import com.google.common.base.Objects;

import java.util.Map;

public class GetLatestRatesResponse {

    private final String base;
    private final Map<String, Double> rates;
    private final long timestamp;

    public GetLatestRatesResponse(String base, Map<String, Double> rates, long timestamp) {
        this.base = base;
        this.rates = rates;
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetLatestRatesResponse that = (GetLatestRatesResponse) o;

        return Objects.equal(this.base, that.base) &&
                Objects.equal(this.rates, that.rates) &&
                Objects.equal(this.timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(base, rates, timestamp);
    }

}
