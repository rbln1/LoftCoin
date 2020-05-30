package me.rubl.loftcoin.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.intercepting.SingleActivityFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.rubl.loftcoin.ui.main.MainActivity;
import me.rubl.loftcoin.ui.welcome.WelcomeActivity;

@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {

    private SharedPreferences preferences;

    private TestIdling testIdling;

    @Rule
    public final ActivityTestRule<SplashActivity> rule = new ActivityTestRule<>(
            new SingleActivityFactory<SplashActivity>(SplashActivity.class) {
        @Override
        protected SplashActivity create(Intent intent) {
            final SplashActivity activity = new SplashActivity();

            activity.idling = testIdling;

            return activity;
        }
    }, false, false);

    @Before
    public void setUp() throws Exception {

        final Context context = ApplicationProvider.getApplicationContext();

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        testIdling = new TestIdling();

        IdlingRegistry.getInstance().register(testIdling.resource);
    }

    @Test
    public void open_welcome_if_first_run() {

        preferences.edit().putBoolean(WelcomeActivity.KEY_SHOW_WELCOME, true).apply();

        rule.launchActivity(new Intent());

        Intents.init();
        Intents.intended(IntentMatchers.hasComponent(WelcomeActivity.class.getName()));
    }

    @Test
    public void open_main_if_start_working_being_clicked() {

        preferences.edit().putBoolean(WelcomeActivity.KEY_SHOW_WELCOME, false).apply();

        rule.launchActivity(new Intent());

        Intents.init();
        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(testIdling.resource);
        Intents.release();
    }

    private static class TestIdling implements SplashIdling {

        final CountingIdlingResource resource = new CountingIdlingResource("splash");

        @Override
        public void busy() {
            resource.increment();
        }

        @Override
        public void idle() {
            resource.decrement();
        }
    }
}