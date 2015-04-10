package com.anbroidsdev.converter.di;

import com.anbroidsdev.converter.ConverterApplication;
import com.anbroidsdev.converter.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by david on 8/4/15.
 */
@Singleton
@Component(modules = MainModule.class)
public interface RootComponent {

    void inject(MainActivity activity);
    void inject(ConverterApplication application);
}