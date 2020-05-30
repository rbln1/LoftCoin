package me.rubl.loftcoin.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.VisibleForTesting;
import androidx.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.rubl.loftcoin.R;
import me.rubl.loftcoin.ui.main.MainActivity;
import me.rubl.loftcoin.ui.welcome.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler();

    private Runnable goNextScreen;

    @VisibleForTesting
    SplashIdling idling = new NoopIdling();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preferences.getBoolean(WelcomeActivity.KEY_SHOW_WELCOME, true)){
            goNextScreen = () -> {
                startActivity(new Intent(this, WelcomeActivity.class));
                idling.idle();
            };
        } else {
            goNextScreen = () -> {
                startActivity(new Intent(this, MainActivity.class));
                idling.idle();
            };
        }

        handler.postDelayed(goNextScreen, 1500);
        idling.busy();
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(goNextScreen);
        super.onStop();
    }

    private static class NoopIdling implements SplashIdling {

        @Override
        public void busy() {

        }

        @Override
        public void idle() {

        }
    }
}
