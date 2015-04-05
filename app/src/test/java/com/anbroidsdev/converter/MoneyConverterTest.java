package com.anbroidsdev.converter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class MoneyConverterTest {

    /**
     * Code corresponding to <a href="http://en.wikipedia.org/wiki/ISO_4217">ISO 4217</a>
     */
    private static final Currency BASE = Currency.getInstance("USD");

    private static final Rates RATES = new Rates();

    static {
        RATES.put(new Rate(Currency.getInstance("EUR"), 0.879689294));
        RATES.put(new Rate(Currency.getInstance("GBP"), 0.656183));
        RATES.put(new Rate(Currency.getInstance("JPY"), 120.171));
    }

    private static final Double[] AMOUNTS = new Double[]{-2.0,
                                                         -1.0,
                                                         1.0,
                                                         2.0};

    private MoneyConverter moneyConverter;

    @Before
    public void setUp() throws Exception {
        moneyConverter = new MoneyConverter(BASE, RATES.copy());
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
        new MoneyConverter(BASE, new Rates());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRatesHasAnyNullValue() throws Exception {
        final Currency key = Currency.getInstance("EUR");
        final Rates rates = RATES.copy();

        rates.put(new Rate(key, null));

        new MoneyConverter(BASE, rates);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRatesHasAnyNegativeValue() throws Exception {
        final Currency key = Currency.getInstance("EUR");
        final Rates rates = RATES.copy();
        final Double value = rates.get(key).getValue();

        rates.put(new Rate(key, -value));

        new MoneyConverter(BASE, rates);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRatesContainsTheBase() throws Exception {
        final Rates rates = RATES.copy();
        rates.put(new Rate(BASE, null));

        new MoneyConverter(BASE, rates);
    }

    @Test
    public void shouldConvertAmountToAGivenCurrencyWithDefaultBase() throws Exception {
        for (Rate rate : RATES.list()) {
            for (Double amount : AMOUNTS) {
                final double convertedAmount = moneyConverter.convert(moneyConverter.convert(amount, rate.getCurrency()),
                                                                      BASE,
                                                                      rate.getCurrency());

                // Converting twice should return the original amount
                assertEquals(amount, convertedAmount, 0.000000000000001);
            }
        }
    }

    @Test
    public void shouldConvertAmountToAGivenCurrencyWithCustomBase() throws Exception {
        final List<Currency> currencies = new ArrayList<>();
        currencies.add(BASE);
        for (Rate rate : RATES.list()) {
            currencies.add(rate.getCurrency());
        }

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

        double inverseRate = 1.0 / RATES.get(newBaseCurrency).getValue();

        assertFalse(moneyConverter.getRates().containsCurrency(newBaseCurrency));
        assertTrue(moneyConverter.getRates().containsCurrency(baseCurrency));
        assertEquals(inverseRate, moneyConverter.getRates().get(baseCurrency).getValue(), 0);

        for (Rate rate : moneyConverter.getRates().list()) {
            if (rate.getCurrency() != baseCurrency) {
                assertEquals(inverseRate * RATES.get(rate.getCurrency()).getValue(), rate.getValue(), 0);
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
