package me.rubl.loftcoin.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.rubl.loftcoin.R;

public class CurrencyRepoImpl implements CurrencyRepo {

    private static final String KEY_CURRENCY = "currency";

    private final Map<String, Currency> availableCurrencies = new HashMap<>();

    private final Context context;

    private SharedPreferences prefs;

    public CurrencyRepoImpl(@NonNull Context context) {
        this.context = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        availableCurrencies.put("USD", Currency.create("$", "USD", context.getString(R.string.usd)));
        availableCurrencies.put("EUR", Currency.create("€", "EUR", context.getString(R.string.eur)));
        availableCurrencies.put("RUB", Currency.create("₽", "RUB", context.getString(R.string.rub)));
    }

    @NonNull
    @Override
    public LiveData<List<Currency>> availableCurrencies() {
        final MutableLiveData<List<Currency>> liveData = new MutableLiveData<>();
        liveData.setValue(new ArrayList<>(availableCurrencies.values()));
        return liveData;
    }

    @NonNull
    @Override
    public LiveData<Currency> currency() {
        return new CurrencyLiveData();
    }

    @Override
    public void updateCurrency(@NonNull Currency currency) {
        prefs.edit().putString(KEY_CURRENCY, currency.code()).apply();
    }

    private static class AllCurrenciesLiveData extends LiveData<List<Currency>> {

        private final Context context;

        AllCurrenciesLiveData(Context context) {
            this.context = context;
        }
    }

    private class CurrencyLiveData extends LiveData<Currency> implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        protected void onActive() {
            prefs.registerOnSharedPreferenceChangeListener(this);
            setValue(availableCurrencies.get(prefs.getString(KEY_CURRENCY, "USD")));
        }

        @Override
        protected void onInactive() {
            prefs.unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            setValue(availableCurrencies.get(prefs.getString(key, "USD")));
        }
    }
}
