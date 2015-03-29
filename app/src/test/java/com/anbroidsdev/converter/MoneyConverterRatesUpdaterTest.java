package com.anbroidsdev.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyConverterRatesUpdaterTest {

    private static final Currency BASE = Currency.getInstance("USD");
    private static final Map<String, Double> RATES = new HashMap<>();
    static {
        RATES.put("EUR", 0.879689294);
        RATES.put("GBP", 0.656183);
        RATES.put("JPY", 120.171);
    }
    private static final long TIMESTAMP = 1425916861;

    @Mock
    private MoneyConverter moneyConverter;
    @Mock
    private OpenExchangeRatesApi openExchangeRatesApi;
    @InjectMocks
    private MoneyConverterRatesUpdater moneyConverterRatesUpdater;

    @Mock
    private MoneyConverterRatesUpdater.OnRatesUpdateListener listener;

    @Test
    public void shouldGetLatestRatesFromOpenExchangeRates() throws Exception {
        prepareMoneyConverter();

        moneyConverterRatesUpdater.updateRates(listener);

        verify(openExchangeRatesApi).getLatestRates(eq(BASE.getCurrencyCode()), any(OpenExchangeRatesApi.OnLatestRatesCallback.class));
    }

    @Test
    public void shouldUpdateMoneyConverterRates() throws Exception {
        prepareMoneyConverter();
        prepareOnLatestRatesCallback();

        moneyConverterRatesUpdater.updateRates(listener);

        verify(moneyConverter, times(1)).setRates(eq(convertRates(RATES)));
    }

    @Test
    public void shouldNotThrowExceptionIfACurrencyIsNotValid() throws Exception {
        prepareMoneyConverter();
        prepareOnLatestRatesCallback();

        RATES.put("WRONG", 0.43672);

        moneyConverterRatesUpdater.updateRates(listener);

        verify(moneyConverter, times(1)).setRates(eq(convertRates(RATES)));

        RATES.remove("WRONG");
    }

    @Test
    public void shouldNotifyTheListenerWhenTheRatesAreUpdated() throws Exception {
        prepareMoneyConverter();
        prepareOnLatestRatesCallback();

        moneyConverterRatesUpdater.updateRates(listener);

        verify(listener).onRatesUpdated(eq(convertRates(RATES)), eq(TIMESTAMP));
    }

    private void prepareMoneyConverter() {
        when(moneyConverter.getBase()).thenReturn(BASE);
    }

    private void prepareOnLatestRatesCallback() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((OpenExchangeRatesApi.OnLatestRatesCallback) invocation.getArguments()[invocation.getArguments().length-1]).onLatestRates(RATES, TIMESTAMP);
                return null;
            }
        }).when(openExchangeRatesApi).getLatestRates(anyString(), any(OpenExchangeRatesApi.OnLatestRatesCallback.class));
    }

    private static Map<Currency, Double> convertRates(Map<String, Double> rates) {
        final Map<Currency, Double> expectedRates = new HashMap<>(rates.size());

        for (Map.Entry<String, Double> rate : rates.entrySet()) {
            try {
                expectedRates.put(Currency.getInstance(rate.getKey()), rate.getValue());
            } catch (IllegalArgumentException e) {}
        }

        return expectedRates;
    }

}
