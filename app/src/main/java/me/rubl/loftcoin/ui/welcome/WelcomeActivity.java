package me.rubl.loftcoin.ui.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import me.rubl.loftcoin.databinding.ActivityWelcomeBinding;
import me.rubl.loftcoin.ui.main.MainActivity;
import me.rubl.loftcoin.widget.CircleIndicator;

public class WelcomeActivity extends AppCompatActivity {

    public static final String KEY_SHOW_WELCOME = "show_welcome";

    private ActivityWelcomeBinding binding;
    private SnapHelper snapHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.activityWelcomeRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false)
        );
        binding.activityWelcomeRecyclerView.addItemDecoration(new CircleIndicator(this));
        binding.activityWelcomeRecyclerView.setAdapter(new WelcomeAdapter());
        binding.activityWelcomeRecyclerView.setHasFixedSize(true);

        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.activityWelcomeRecyclerView);

        binding.activityWelcomeBtnNext.setOnClickListener(v -> {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit().putBoolean(KEY_SHOW_WELCOME, false).apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        snapHelper.attachToRecyclerView(null);
        binding.activityWelcomeRecyclerView.setAdapter(null);
        super.onDestroy();
    }
}
