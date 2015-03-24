package com.anbroidsdev.converter;

import com.google.common.base.Strings;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public class RetrofitOpenExchangeRatesClient implements OpenExchangeRatesApi {

    public interface OpenExchangeRates {
        @GET("/latest.json")
        void getLatestRates(@Query("app_id") String appId,
                            @Query("base") String base,
                            Callback<GetLatestRatesResponse> callback);
    }

    private final OpenExchangeRates openExchangeRates;
    private final String apiKey;

    public RetrofitOpenExchangeRatesClient(OpenExchangeRates openExchangeRates, String apiKey) {
        if (openExchangeRates == null) {
            throw new IllegalArgumentException("OpenExchangeRates cannot be null");
        }
        if (Strings.isNullOrEmpty(apiKey)) {
            throw new IllegalArgumentException("ApiKey cannot be null or empty");
        }

        this.openExchangeRates = openExchangeRates;
        this.apiKey = apiKey;
    }

    @Override
    public void getLatestRates(final String base, final OnLatestRatesCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be null");
        }

        openExchangeRates.getLatestRates(apiKey, base, new Callback<GetLatestRatesResponse>() {

            @Override
            public void success(GetLatestRatesResponse latestRatesResponse, Response rawResponse) {
                callback.onLatestRates(latestRatesResponse.getRates(), latestRatesResponse.getTimestamp());
            }

            @Override
            public void failure(RetrofitError error) {}

        });
    }

}
