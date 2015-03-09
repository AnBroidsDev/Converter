package com.anbroidsdev.converter;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created by david on 9/3/15.
 */
public class GetLatestRatesResponseParserTest {

    private static final String RESPONSE = "{\n" +
            "  \"disclaimer\": \"disclaimer\",\n" +
            "  \"timestamp\": 1425916861,\n" +
            "  \"base\": \"USD\",\n" +
            "  \"rates\": {\n" +
            "    \"EUR\": 0.921691,\n" +
            "    \"GBP\": 0.66226,\n" +
            "    \"JPY\": 121.0982\n" +
            "  }\n" +
            "}";

    @Test
    public void shouldParseSuccessResponse() throws Exception {
        final Map<String, Double> expectedRates = new HashMap<>();
        expectedRates.put("EUR", 0.921691);
        expectedRates.put("GBP", 0.66226);
        expectedRates.put("JPY", 121.0982);
        final GetLatestRatesResponse expectedResponse = new GetLatestRatesResponse("USD", expectedRates, 1425916861);
        final GetLatestRatesResponse response = new Gson().fromJson(RESPONSE, GetLatestRatesResponse.class);

        assertEquals(expectedResponse, response);
    }

}
