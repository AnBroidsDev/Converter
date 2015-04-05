package com.anbroidsdev.converter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SharedPreferencesRatesSaverTest {

    private static final String KEY_RATES = "rates";
    private static final String KEY_TIMESTAMP = "timestamp";

    private static final Rates RATES = new Rates();
    static {
        RATES.put(new Rate(Currency.getInstance("EUR"), 0.879689294));
        RATES.put(new Rate(Currency.getInstance("GBP"), 0.656183));
        RATES.put(new Rate(Currency.getInstance("JPY"), 120.171));
    }
    private static final long TIMESTAMP = 1425916861;

    @Mock
    private ObjectSharedPreferences sharedPreferences;
    @Mock
    private ObjectSharedPreferences.ObjectEditor sharedPreferencesEditor;

    private SharedPreferencesRatesSaver ratesSaver;

    @Before
    public void setUp() throws Exception {
        doReturn(RATES).when(sharedPreferences).getObject(eq(KEY_RATES), eq(Rates.class));
        doReturn(TIMESTAMP).when(sharedPreferences).getLong(eq(KEY_TIMESTAMP), eq(0L));
        doReturn(sharedPreferencesEditor).when(sharedPreferences).edit();

        ratesSaver = new SharedPreferencesRatesSaver(sharedPreferences);
    }

    @Test
    public void shouldRestoreLastSavedRatesDuringInitialization() throws Exception {
        assertEquals(RATES, ratesSaver.getLastSavedRates());
        assertEquals(TIMESTAMP, ratesSaver.getLastSavedRatesTimestamp());
    }

    @Test
    public void shouldSaveRatesInsideTheSharedPreferences() throws Exception {
        ratesSaver.saveRates(RATES, TIMESTAMP);

        verify(sharedPreferencesEditor, times(1)).putObject(eq(KEY_RATES), eq(RATES));
        verify(sharedPreferencesEditor, times(1)).putLong(eq(KEY_TIMESTAMP), eq(TIMESTAMP));
        assertEquals(RATES, ratesSaver.getLastSavedRates());
        assertEquals(TIMESTAMP, ratesSaver.getLastSavedRatesTimestamp());
    }

    @Test
    public void shouldClearSavedRates() throws Exception {
        ratesSaver.clear();

        assertNull(ratesSaver.getLastSavedRates());
        assertEquals(0, ratesSaver.getLastSavedRatesTimestamp());
    }

}
