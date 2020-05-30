package me.rubl.loftcoin.ui.currency;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import javax.inject.Inject;

import me.rubl.loftcoin.BaseComponent;
import me.rubl.loftcoin.databinding.DialogCurrencyBinding;
import me.rubl.loftcoin.widget.OnItemClick;

public class CurrencyDialog extends AppCompatDialogFragment {

    private final CurrencyComponent component;

    private DialogCurrencyBinding binding;

    private CurrencyViewModel viewModel;

    private CurrencyAdapter adapter;

    private OnItemClick onItemClick;

    @Inject
    CurrencyDialog(BaseComponent baseComponent) {
        component = DaggerCurrencyComponent.builder()
            .baseComponent(baseComponent)
            .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(
            this,
            component.viewModelFactory()).get(CurrencyViewModel.class
        );
        adapter = new CurrencyAdapter();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogCurrencyBinding.inflate(requireActivity().getLayoutInflater());
        return new MaterialAlertDialogBuilder(requireActivity())
            .setView(binding.getRoot())
            .create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.dialogCurrencyRecycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.dialogCurrencyRecycler.setAdapter(adapter);
        viewModel.allCurrencies().observe(this, adapter::submitList);
        onItemClick = new OnItemClick((v) -> {
            final RecyclerView.ViewHolder viewHolder = binding.dialogCurrencyRecycler.findContainingViewHolder(v);
            if (viewHolder != null) {
                viewModel.updateCurrency(adapter.getItem(viewHolder.getAdapterPosition()));
            }
            dismissAllowingStateLoss();
        });
        binding.dialogCurrencyRecycler.addOnItemTouchListener(onItemClick);
    }

    @Override
    public void onDestroy() {
        binding.dialogCurrencyRecycler.removeOnItemTouchListener(onItemClick);
        binding.dialogCurrencyRecycler.setAdapter(null);
        super.onDestroy();
    }
}
