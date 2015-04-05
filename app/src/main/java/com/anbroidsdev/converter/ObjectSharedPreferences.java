package com.anbroidsdev.converter;

import android.content.SharedPreferences;

import java.lang.reflect.Type;

/**
 * {@inheritDoc}
 */
public interface ObjectSharedPreferences extends SharedPreferences {

    /**
     * Retrieve an Object of a given class from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param theClass Type of the object to be returned.
     *
     * @return Returns the preference value if it exists, or {@code null}.
     */
    public <T> T getObject(String key, Class<T> theClass);

    /**
     * Retrieve an Object of a given class from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param typeOf Type of the object to be returned.
     *
     * @return Returns the preference value if it exists, or {@code null}.
     */
    public <T> T getObject(String key, Type typeOf);

    public interface ObjectEditor extends SharedPreferences.Editor {

        public <T> SharedPreferences.Editor putObject(String key, T object);

    }

    public ObjectEditor edit();

}
