package com.anbroidsdev.converter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import javax.inject.Inject;

public class MainActivity extends ActionBarActivity {

    @Inject
    OpenExchangeRatesApi openExchangeRatesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ConverterApplication)getApplication()).getComponent().inject(this);
    }
}
