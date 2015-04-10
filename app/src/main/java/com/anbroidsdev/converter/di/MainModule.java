package com.anbroidsdev.converter.di;

import com.anbroidsdev.converter.ConverterApplication;
import com.anbroidsdev.converter.OpenExchangeRatesApi;
import com.anbroidsdev.converter.RetrofitOpenExchangeRatesClient;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by david on 8/4/15.
 */
@Module
public class MainModule {

    private ConverterApplication application;

    public MainModule(ConverterApplication application) {
        this.application = application;
    }

    @Provides
    OpenExchangeRatesApi provideOpenExchangeRatesApi() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("openexchangerates.org/api")
                .build();
        RetrofitOpenExchangeRatesClient.OpenExchangeRates service = restAdapter.create(RetrofitOpenExchangeRatesClient.OpenExchangeRates.class);
        RetrofitOpenExchangeRatesClient client = new RetrofitOpenExchangeRatesClient(service,"YOUR-API-KEY");

        return client;
    }
}
