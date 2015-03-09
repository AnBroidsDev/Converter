package com.anbroidsdev.converter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by david on 9/3/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class RetrofitOpenExchangeRatesClientTest {

    private static final String API_KEY = "API_KEY";

    @Mock
    private RetrofitOpenExchangeRatesClient.OpenExchangeRates openExchangeRates;
    @Mock
    private OpenExchangeRatesApi.OnLatestRatesCallback callback;


    private RetrofitOpenExchangeRatesClient client;

    @Before
    public void setUp() throws Exception {
        client = new RetrofitOpenExchangeRatesClient(openExchangeRates, API_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfOpenExchangeRatesIsNull() throws Exception {
        new RetrofitOpenExchangeRatesClient(null, API_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfApiKeyIsNull() throws Exception {
        new RetrofitOpenExchangeRatesClient(openExchangeRates, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfApiKeyIsEmpty() throws Exception {
        new RetrofitOpenExchangeRatesClient(openExchangeRates, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLatestRatesCallbackIsNull() throws Exception {
        client.getLatestRates("USD", null);
    }

    @Test
    public void shouldGetLatestRatesFromOpenExchangeRates() throws Exception {
        final String base = "USD";

        client.getLatestRates(base, callback);

        verify(openExchangeRates, times(1)).getLatestRates(eq(API_KEY), eq(base), Matchers.<Callback<GetLatestRatesResponse>>any());
    }

    @Test
    public void shouldNotifyTheCallbackWithTheRetrievedRates() throws Exception {
        final String base = "USD";
        final Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.921691);
        rates.put("GBP", 0.66226);
        rates.put("JPY", 121.0982);
        final long timestamp = 1425916861;

        final GetLatestRatesResponse response = new GetLatestRatesResponse(base, rates, timestamp);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<GetLatestRatesResponse>) invocation.getArguments()[invocation.getArguments().length-1]).success(response, null);
                return null;
            }
        }).when(openExchangeRates).getLatestRates(eq(API_KEY), eq(base), Matchers.<Callback<GetLatestRatesResponse>>any());

        client.getLatestRates(base, callback);

        verify(callback).onLatestRates(eq(rates), eq(timestamp));
    }

}
