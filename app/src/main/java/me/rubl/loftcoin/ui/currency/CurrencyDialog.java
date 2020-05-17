package me.rubl.loftcoin.ui.currency;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import me.rubl.loftcoin.R;
import me.rubl.loftcoin.data.Currency;
import me.rubl.loftcoin.data.CurrencyRepo;
import me.rubl.loftcoin.data.CurrencyRepoImpl;
import me.rubl.loftcoin.databinding.DialogCurrencyBinding;
import me.rubl.loftcoin.util.OnItemClick;

public class CurrencyDialog extends AppCompatDialogFragment {

    private DialogCurrencyBinding binding;

    private CurrencyRepo currencyRepo;

    private CurrencyAdapter adapter;

    private OnItemClick onItemClick;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyRepo = new CurrencyRepoImpl(requireContext());
        adapter = new CurrencyAdapter();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogCurrencyBinding.inflate(requireActivity().getLayoutInflater());
        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.choose_currency)
                .setView(binding.getRoot())
                .create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.dialogCurrencyRecycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.dialogCurrencyRecycler.setAdapter(adapter);
        currencyRepo.availableCurrencies().observe(this, adapter::submitList);
        onItemClick = new OnItemClick(binding.dialogCurrencyRecycler.getContext(), (v) -> {
            final RecyclerView.ViewHolder viewHolder = binding.dialogCurrencyRecycler.findContainingViewHolder(v);
            if (viewHolder != null) {
                final Currency item = adapter.getItem(viewHolder.getAdapterPosition());
                currencyRepo.updateCurrency(item);
            }
            dismissAllowingStateLoss();
        });
        binding.dialogCurrencyRecycler.addOnItemTouchListener(onItemClick);
    }

    @Override
    public void onDestroy() {
        binding.dialogCurrencyRecycler.setAdapter(null);
        super.onDestroy();
    }
}
