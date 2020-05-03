package me.rubl.loftcoin.ui.wallets;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.rubl.loftcoin.databinding.ItemWalletBinding;

class WalletsAdapter extends RecyclerView.Adapter<WalletsAdapter.ViewHolder> {

    //TODO: Swap this
    private final int CARDS_COUNT = 10;

    private LayoutInflater inflater;

    @Override
    public int getItemCount() {
        return CARDS_COUNT;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemWalletBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {}

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(@NonNull ItemWalletBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setClipToOutline(true);
        }
    }
}
