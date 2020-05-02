package me.rubl.loftcoin.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.rubl.loftcoin.R;
import me.rubl.loftcoin.ui.main.MainActivity;
import me.rubl.loftcoin.ui.welcome.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler();

    private Runnable goNextScreen;

    private SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preferences.getBoolean(WelcomeActivity.KEY_SHOW_WELCOME, true)){
            goNextScreen = () -> startActivity(new Intent(this, WelcomeActivity.class));
        } else {
            goNextScreen = () -> startActivity(new Intent(this, MainActivity.class));
        }

        handler.postDelayed(goNextScreen, 1500);
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(goNextScreen);
        super.onStop();
    }
}
