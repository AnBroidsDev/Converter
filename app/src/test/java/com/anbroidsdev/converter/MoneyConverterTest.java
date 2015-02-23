package com.anbroidsdev.converter;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class MoneyConverterTest {

    /**
     * Code corresponding to <a href="http://en.wikipedia.org/wiki/ISO_4217">ISO 4217</a>
     */
    private static final String BASE = "USD";

    private static final Map<String, Double> RATES;

    static {
        RATES = new HashMap<>();
        RATES.put("EUR", 0.879689294);
        RATES.put("GBP", 0.656183);
        RATES.put("JPY", 120.171);
    }

    private MoneyConverter moneyConverter;

    @Before
    public void setUp() throws Exception {
        moneyConverter = new MoneyConverter(BASE, RATES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfBaseIsNull() throws Exception {
        new MoneyConverter(null, RATES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfBaseIsEmpty() throws Exception {
        new MoneyConverter("", RATES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfBaseIsNotAValidCurrencyCode() throws Exception {
        new MoneyConverter("NO_VALID", RATES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRatesAreNull() throws Exception {
        new MoneyConverter(BASE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRatesAreEmpty() throws Exception {
        new MoneyConverter(BASE, new HashMap<String, Double>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRatesContainsAnyNotValidCurrencyCode() throws Exception {
        final Map<String, Double> rates = new HashMap<>();
        rates.put("NO_VALID", 0.879689294);

        new MoneyConverter(BASE, rates);
    }

    @Test
    public void shouldConvertBaseCase() throws Exception {
        double convertedAmount = moneyConverter.convert(1, "EUR");

        assertEquals(RATES.get("EUR"), convertedAmount, 0);
    }

    @Test
    public void shouldConvertRandomNumber() throws Exception {
        double amount = new Random().nextDouble();
        double convertedAmount = moneyConverter.convert(amount, "EUR");

        assertEquals(RATES.get("EUR") * amount, convertedAmount, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAmountIsNegative() throws Exception {
        moneyConverter.convert(-1, "EUR");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfOutputCurrencyIsNotSupported() throws Exception {
        moneyConverter.convert(1, "ARS");
    }

    @Test
    public void shouldUpdateRatesWhenBaseIsChanged() throws Exception {
        moneyConverter.setBase("EUR");

        assertFalse(RATES.containsKey("EUR"));
        assertTrue(RATES.containsKey("USD"));
        assertEquals(1.0 / 0.879689294, RATES.get("USD"), 0);
        assertEquals(0.656183 / 0.879689294, RATES.get("GBP"), 0);
        assertEquals(120.171 / 0.879689294, RATES.get("JPY"), 0);
    }



    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfBaseCurrencyIsNotSupported() throws Exception {
        moneyConverter.setBase("ARS");
    }

}
