package me.rubl.loftcoin.ui.converter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import javax.inject.Inject;

import me.rubl.loftcoin.BuildConfig;
import me.rubl.loftcoin.data.Coin;
import me.rubl.loftcoin.databinding.ItemCoinsSheetBinding;
import me.rubl.loftcoin.util.ImageLoader;
import me.rubl.loftcoin.widget.CircleViewOutlineProvider;

public class CoinSheetAdapter extends ListAdapter<Coin, CoinSheetAdapter.ViewHolder> {


    private final ImageLoader imageLoader;
    private LayoutInflater inflater;

    @Inject
    CoinSheetAdapter(ImageLoader imageLoader) {
        super(new DiffUtil.ItemCallback<Coin>() {
            @Override
            public boolean areItemsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return oldItem.id() == newItem.id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
        this.imageLoader = imageLoader;
    }

    @Override
    public  Coin getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCoinsSheetBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Coin coin = getItem(position);

        holder.binding.itemCoinsSheetName.setText(coin.symbol() + " | " + coin.name());
        imageLoader.load(BuildConfig.IMG_ENDPOINT + coin.id() + ".png")
                .into(holder.binding.itemCoinsSheetLogo);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemCoinsSheetBinding binding;

        ViewHolder(@NonNull ItemCoinsSheetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            CircleViewOutlineProvider.apply(binding.itemCoinsSheetLogo);
        }
    }
}
