package me.rubl.loftcoin.ui.welcome;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import me.rubl.loftcoin.R;
import me.rubl.loftcoin.ui.main.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class WelcomeActivityTest {

    @Test
    public void open_main_if_button_being_pressed() {
        ActivityScenario.launch(WelcomeActivity.class);
        Intents.init();

        onView(withId(R.id.activity_welcome_btn_next)).perform(click());

        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}