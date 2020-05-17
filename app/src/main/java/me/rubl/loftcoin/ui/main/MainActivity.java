package me.rubl.loftcoin.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import me.rubl.loftcoin.R;
import me.rubl.loftcoin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.activityMainToolbar);
        setContentView(binding.getRoot());

        final NavController navController = Navigation.findNavController(this, R.id.activity_main_host);
        NavigationUI.setupWithNavController(binding.activityMainBottomNav, navController);
        NavigationUI.setupWithNavController(binding.activityMainToolbar, navController,
                new AppBarConfiguration.Builder(binding.activityMainBottomNav.getMenu()).build());
    }
}
