package me.rubl.loftcoin.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentFactory;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import javax.inject.Inject;

import me.rubl.loftcoin.BaseComponent;
import me.rubl.loftcoin.LoftApp;
import me.rubl.loftcoin.R;
import me.rubl.loftcoin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private MainComponent component;

    @Inject FragmentFactory fragmentFactory;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        final BaseComponent baseComponent = ((LoftApp) newBase.getApplicationContext()).getComponent();
        component = DaggerMainComponent.builder().baseComponent(baseComponent).build();
        component.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().setFragmentFactory(fragmentFactory);

        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.activityMainToolbar);
        setContentView(binding.getRoot());

        final NavController navController = Navigation.findNavController(this, R.id.activity_main_host);
        NavigationUI.setupWithNavController(binding.activityMainBottomNav, navController);
        NavigationUI.setupWithNavController(binding.activityMainToolbar, navController,
            new AppBarConfiguration.Builder(binding.activityMainBottomNav.getMenu()).build());
    }
}
