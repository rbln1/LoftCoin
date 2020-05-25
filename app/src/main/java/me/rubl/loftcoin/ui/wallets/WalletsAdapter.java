package me.rubl.loftcoin.ui.wallets;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import javax.inject.Inject;

import me.rubl.loftcoin.BuildConfig;
import me.rubl.loftcoin.data.Wallet;
import me.rubl.loftcoin.databinding.ItemWalletBinding;
import me.rubl.loftcoin.util.BalanceFormatter;
import me.rubl.loftcoin.widget.CircleViewOutlineProvider;
import me.rubl.loftcoin.util.ImageLoader;
import me.rubl.loftcoin.util.PriceFormatter;

class WalletsAdapter extends ListAdapter<Wallet, WalletsAdapter.ViewHolder> {

    private final PriceFormatter priceFormatter;

    private final BalanceFormatter balanceFormatter;

    private final ImageLoader imageLoader;

    private LayoutInflater inflater;

    @Inject
    WalletsAdapter(PriceFormatter priceFormatter, BalanceFormatter balanceFormatter, ImageLoader imageLoader) {
        super(new DiffUtil.ItemCallback<Wallet>() {
            @Override
            public boolean areItemsTheSame(@NonNull Wallet oldItem, @NonNull Wallet newItem) {
                return Objects.equals(oldItem.uid(), newItem.uid());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Wallet oldItem, @NonNull Wallet newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
        this.priceFormatter = priceFormatter;
        this.balanceFormatter = balanceFormatter;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemWalletBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Wallet wallet = getItem(position);

        holder.binding.itemWalletSymbol.setText(wallet.coin().symbol());
        holder.binding.itemWalletBalance1.setText(balanceFormatter.format(wallet));

        final double balance = wallet.balance() * wallet.coin().price();
        holder.binding.itemWalletBalance2.setText(priceFormatter.format(wallet.coin().currencyCode(), balance));
        imageLoader
            .load(BuildConfig.IMG_ENDPOINT + wallet.coin().id() + ".png")
            .into(holder.binding.itemWalletImage);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemWalletBinding binding;

        ViewHolder(@NonNull ItemWalletBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setClipToOutline(true);
            CircleViewOutlineProvider.apply(binding.itemWalletImage);
            this.binding = binding;
        }
    }
}
