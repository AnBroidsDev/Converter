package com.anbroidsdev.converter;

import org.junit.Before;
import org.junit.Test;

import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class MoneyConverterTest {

    /**
     * Code corresponding to <a href="http://en.wikipedia.org/wiki/ISO_4217">ISO 4217</a>
     */
    private static final Currency BASE = Currency.getInstance("USD");

    private static final Map<Currency, Double> RATES = new HashMap<>();

    static {
        RATES.put(Currency.getInstance("EUR"), 0.879689294);
        RATES.put(Currency.getInstance("GBP"), 0.656183);
        RATES.put(Currency.getInstance("JPY"), 120.171);
    }

    private static final Double[] AMOUNTS = new Double[]{-2.0,
                                                         -1.0,
                                                         1.0,
                                                         2.0};

    private MoneyConverter moneyConverter;

    @Before
    public void setUp() throws Exception {
        moneyConverter = new MoneyConverter(BASE, new HashMap<>(RATES));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfBaseIsNull() throws Exception {
        new MoneyConverter(null, RATES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRatesAreNull() throws Exception {
        new MoneyConverter(BASE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRatesAreEmpty() throws Exception {
        new MoneyConverter(BASE, new HashMap<Currency, Double>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRatesHasAnyNullValue() throws Exception {
        final Currency key = Currency.getInstance("EUR");
        final Double value = RATES.get(key);

        RATES.put(key, null);

        try {
            new MoneyConverter(BASE, RATES);
        } catch (IllegalArgumentException e) {
            RATES.put(key, value);

            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRatesHasAnyNegativeValue() throws Exception {
        final Currency key = Currency.getInstance("EUR");
        final Double value = RATES.get(key);

        RATES.put(key, -value);

        try {
            new MoneyConverter(BASE, RATES);
        } catch (IllegalArgumentException e) {
            RATES.put(key, value);

            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRatesContainsTheBase() throws Exception {
        RATES.put(BASE, null);

        try {
            new MoneyConverter(BASE, RATES);
        } catch (IllegalArgumentException e) {
            RATES.remove(BASE);

            throw e;
        }
    }

    @Test
    public void shouldConvertAmountToAGivenCurrencyWithDefaultBase() throws Exception {
        for (Map.Entry<Currency, Double> entry : RATES.entrySet()) {
            for (Double amount : AMOUNTS) {
                final double convertedAmount = moneyConverter.convert(moneyConverter.convert(amount, entry.getKey()),
                                                                      BASE,
                                                                      entry.getKey());

                // Converting twice should return the original amount
                assertEquals(amount, convertedAmount, 0.000000000000001);
            }
        }
    }

    @Test
    public void shouldConvertAmountToAGivenCurrencyWithCustomBase() throws Exception {
        final Set<Currency> currencies = new HashSet<>();
        currencies.add(BASE);
        currencies.addAll(RATES.keySet());

        for (Currency currency : currencies) {
            for (Currency base : currencies) {
                if (currency != base) {
                    for (Double amount : AMOUNTS) {
                        final double convertedAmount = moneyConverter.convert(moneyConverter.convert(amount, currency, base),
                                                                              base,
                                                                              currency);

                        // Converting twice should return the original amount
                        assertEquals(amount, convertedAmount, 0.000000000000001);
                    }
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfOutputCurrencyIsNotSupported() throws Exception {
        moneyConverter.convert(1, Currency.getInstance("ARS"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfCustomBaseIsNotSupported() throws Exception {
        moneyConverter.convert(1, Currency.getInstance("EUR"), Currency.getInstance("ARS"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfOutputCurrencyAndCustomBaseAreTheSame() throws Exception {
        moneyConverter.convert(1, BASE, BASE);
    }

    @Test
    public void shouldChangeBase() throws Exception {
        final Currency newBaseCurrency = Currency.getInstance("EUR");

        moneyConverter.setBase(newBaseCurrency);

        assertEquals(newBaseCurrency, moneyConverter.getBase());
    }

    @Test
    public void shouldUpdateRatesWhenBaseIsChanged() throws Exception {
        final Currency baseCurrency = Currency.getInstance("USD");
        final Currency newBaseCurrency = Currency.getInstance("EUR");

        moneyConverter.setBase(newBaseCurrency);

        double inverseRate = 1.0 / RATES.get(newBaseCurrency);

        assertFalse(moneyConverter.getRates().containsKey(newBaseCurrency));
        assertTrue(moneyConverter.getRates().containsKey(baseCurrency));
        assertEquals(inverseRate, moneyConverter.getRates().get(baseCurrency), 0);

        final Map<Currency, Double> rates = new HashMap<>(moneyConverter.getRates());
        rates.remove(newBaseCurrency);

        for (Map.Entry<Currency, Double> entry : rates.entrySet()) {
            if (entry.getKey() != baseCurrency) {
                assertEquals(inverseRate * RATES.get(entry.getKey()), entry.getValue(), 0);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNewBaseCurrencyIsNull() throws Exception {
        moneyConverter.setBase(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNewBaseCurrencyIsNotSupported() throws Exception {
        moneyConverter.setBase(Currency.getInstance("ARS"));
    }

}
