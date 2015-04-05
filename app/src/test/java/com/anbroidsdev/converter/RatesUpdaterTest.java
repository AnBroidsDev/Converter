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
public class RatesUpdaterTest {

    private static final Currency BASE = Currency.getInstance("USD");
    private static final Map<String, Double> RATES = new HashMap<>();
    static {
        RATES.put("EUR", 0.879689294);
        RATES.put("GBP", 0.656183);
        RATES.put("JPY", 120.171);
    }
    private static final Rates CONVERTED_RATES = new Rates(RATES);
    private static final long TIMESTAMP = 1425916861;

    @Mock
    private MoneyConverter moneyConverter;
    @Mock
    private OpenExchangeRatesApi openExchangeRatesApi;
    @Mock
    private SharedPreferencesRatesSaver ratesSaver;
    @InjectMocks
    private RatesUpdater ratesUpdater;

    @Mock
    private RatesUpdater.OnRatesUpdateListener listener;

    @Test
    public void shouldGetLatestRatesFromOpenExchangeRates() throws Exception {
        prepareMoneyConverter();

        ratesUpdater.updateRates(listener);

        verify(openExchangeRatesApi).getLatestRates(eq(BASE.getCurrencyCode()), any(OpenExchangeRatesApi.OnLatestRatesCallback.class));
    }

    @Test
    public void shouldSaveTheRetrievedRates() throws Exception {
        prepareMoneyConverter();
        prepareOnLatestRatesCallback();

        ratesUpdater.updateRates(listener);

        verify(ratesSaver, times(1)).saveRates(eq(CONVERTED_RATES), eq(TIMESTAMP));
    }

    @Test
    public void shouldUpdateMoneyConverterRates() throws Exception {
        prepareMoneyConverter();
        prepareOnLatestRatesCallback();

        ratesUpdater.updateRates(listener);

        verify(moneyConverter, times(1)).setRates(eq(CONVERTED_RATES));
    }

    @Test
    public void shouldNotThrowExceptionIfACurrencyIsNotValid() throws Exception {
        prepareMoneyConverter();
        prepareOnLatestRatesCallback();

        RATES.put("WRONG", 0.43672);

        ratesUpdater.updateRates(listener);

        verify(moneyConverter, times(1)).setRates(eq(CONVERTED_RATES));

        RATES.remove("WRONG");
    }

    @Test
    public void shouldNotifyTheListenerWhenTheRatesAreUpdated() throws Exception {
        prepareMoneyConverter();
        prepareOnLatestRatesCallback();

        ratesUpdater.updateRates(listener);

        verify(listener).onRatesUpdated();
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

}
