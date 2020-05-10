package me.rubl.loftcoin;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import me.rubl.loftcoin.util.DebugTree;
import timber.log.Timber;

public class LoftApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
            Timber.plant(new DebugTree());
        }
    }
}
