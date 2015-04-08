package com.anbroidsdev.converter;

import android.app.Application;

import com.anbroidsdev.converter.di.DaggerRootComponent;
import com.anbroidsdev.converter.di.MainModule;
import com.anbroidsdev.converter.di.RootComponent;

/**
 * Created by david on 8/4/15.
 */
public class ConverterApplication extends Application {

    private RootComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDependencyInjector();
    }

    private void initializeDependencyInjector() {
        component = DaggerRootComponent.builder()
                                        .mainModule(new MainModule(this))
                                        .build();
        component.inject(this);
    }


    public void inject(MainActivity activity) {
        component.inject(activity);
    }

    public RootComponent getComponent() {
        return component;
    }
}
