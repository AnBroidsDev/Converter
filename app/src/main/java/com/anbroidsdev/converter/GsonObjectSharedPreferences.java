package com.anbroidsdev.converter;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class GsonObjectSharedPreferences implements ObjectSharedPreferences {

    private final SharedPreferences delegate;
    private final Editor editor;
    private final Gson gson;

    public GsonObjectSharedPreferences(SharedPreferences delegate, Gson gson) {
        this.delegate = delegate;
        this.editor = new Editor(delegate, gson);
        this.gson = gson;
    }

    public static class Editor implements ObjectSharedPreferences.ObjectEditor {

        private final SharedPreferences delegate;
        private final Gson gson;

        public Editor(SharedPreferences delegate, Gson gson) {
            this.delegate = delegate;
            this.gson = gson;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            delegate.edit().putBoolean(key, value);

            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            delegate.edit().putFloat(key, value);

            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            delegate.edit().putInt(key, value);

            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            delegate.edit().putLong(key, value);

            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            delegate.edit().putString(key, value);

            return this;
        }

        @Override
        public Editor putStringSet(String key, Set<String> values) {
            delegate.edit().putStringSet(key, values);

            return this;
        }

        @Override
        public <T> SharedPreferences.Editor putObject(String key, T object) {
            delegate.edit().putString(key, object != null ? gson.toJson(object) : null);

            return this;
        }

        @Override
        public void apply() {
            delegate.edit().apply();
        }

        @Override
        public Editor clear() {
            delegate.edit().clear();

            return this;
        }

        @Override
        public boolean commit() {
            delegate.edit().commit();

            return true;
        }

        @Override
        public Editor remove(String s) {
            delegate.edit().remove(s);

            return this;
        }
    }

    @Override
    public ObjectEditor edit() {
        return editor;
    }

    @Override
    public Map<String, ?> getAll() {
        return delegate.getAll();
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return delegate.getBoolean(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return delegate.getFloat(key, defValue);
    }

    @Override
    public int getInt(String key, int defValue) {
        return delegate.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return delegate.getLong(key, defValue);
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        return delegate.getString(key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return delegate.getStringSet(key, defValues);
    }

    @Nullable
    @Override
    public <T> T getObject(String key, Class<T> theClass) {
        try {
            final String json = delegate.getString(key, null);

            return gson.fromJson(json, theClass);
        } catch (JsonSyntaxException e) {
            editor.remove(key);
            editor.commit();

            return null;
        }
    }

    @Nullable
    @Override
    public <T> T getObject(String key, Type typeOf) {
        try {
            final String json = delegate.getString(key, null);

            return gson.fromJson(json, typeOf);
        } catch (JsonSyntaxException e) {
            editor.remove(key);
            editor.commit();

            return null;
        }
    }

    @Override
    public boolean contains(String s) {
        return delegate.contains(s);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

}
