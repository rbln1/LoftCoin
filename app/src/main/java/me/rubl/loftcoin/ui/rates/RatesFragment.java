package me.rubl.loftcoin.ui.rates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import me.rubl.loftcoin.R;
import me.rubl.loftcoin.data.Coin;
import me.rubl.loftcoin.data.CurrencyRepo;
import me.rubl.loftcoin.data.CurrencyRepoImpl;
import me.rubl.loftcoin.databinding.FragmentRatesBinding;
import me.rubl.loftcoin.util.PercentFormatter;
import me.rubl.loftcoin.util.PicassoImageLoader;
import me.rubl.loftcoin.util.PriceFormatter;
import timber.log.Timber;

public class RatesFragment extends Fragment{

    private FragmentRatesBinding binding;

    private RatesAdapter adapter;

    private RatesViewModel viewModel;

    private CurrencyRepo currencyRepo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RatesViewModel.class);
        adapter = new RatesAdapter(new PriceFormatter(), new PercentFormatter(), new PicassoImageLoader());
        currencyRepo = new CurrencyRepoImpl(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fragment contain menu
        setHasOptionsMenu(true);

        binding = FragmentRatesBinding.bind(view);

        binding.fragmentRatesRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.fragmentRatesRecycler.swapAdapter(adapter, false);
        binding.fragmentRatesRecycler.setHasFixedSize(true);

        binding.fragmentRatesRefresher.setOnRefreshListener(() -> {
            viewModel.refresh();
        });

        viewModel.coins().observe(getViewLifecycleOwner(), adapter::submitList );
        viewModel.isRefreshing().observe(getViewLifecycleOwner(), binding.fragmentRatesRefresher::setRefreshing);

        currencyRepo.currency().observe(getViewLifecycleOwner(), (currency -> {
            Timber.d("%s", currency);
        }));
    }

    @Override
    public void onDestroyView() {
        binding.fragmentRatesRecycler.swapAdapter(null, false);

        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_rates, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.currency_dialog) {
            NavHostFragment.findNavController(this)
                .navigate(R.id.currency_dialog);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}