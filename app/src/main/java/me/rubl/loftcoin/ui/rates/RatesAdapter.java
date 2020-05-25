package me.rubl.loftcoin.ui.rates;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import me.rubl.loftcoin.BuildConfig;
import me.rubl.loftcoin.R;
import me.rubl.loftcoin.data.Coin;
import me.rubl.loftcoin.databinding.ItemRateBinding;
import me.rubl.loftcoin.widget.CircleViewOutlineProvider;
import me.rubl.loftcoin.util.ImageLoader;
import me.rubl.loftcoin.util.PercentFormatter;
import me.rubl.loftcoin.util.PriceFormatter;

public class RatesAdapter extends ListAdapter<Coin, RatesAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private int colorPositive = Color.GREEN;
    private int colorNegative = Color.RED;
    private int colorEvenLine = Color.DKGRAY;
    private int colorOddLine = Color.GRAY;
    private PriceFormatter priceFormatter;
    private PercentFormatter percentFormatter;
    private ImageLoader imageLoader;

    @Inject
    RatesAdapter(PriceFormatter priceFormatter, PercentFormatter percentFormatter, ImageLoader imageLoader) {
        super(new DiffUtil.ItemCallback<Coin>() {

            @Override
            public boolean areItemsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return oldItem.id() == newItem.id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return Objects.equals(oldItem, newItem);
            }

            @Override
            public Object getChangePayload(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return newItem ;
            }
        });
        this.priceFormatter = priceFormatter;
        this.percentFormatter = percentFormatter;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public RatesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRateBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {

        if (payloads.isEmpty()) {
             onBindViewHolder(holder, position);
        } else {
            Coin coin = (Coin) payloads.get(0);
            holder.binding.itemRatePrice.setText(priceFormatter.format(coin.currencyCode(), coin.price()));
            holder.binding.itemRateChange.setText(percentFormatter.format(coin.change24h()));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RatesAdapter.ViewHolder holder, int position) {
        final Coin coin = getItem(position);

        holder.binding.itemRateSymbol.setText(coin.symbol());
        holder.binding.itemRatePrice.setText(priceFormatter.format(coin.currencyCode(), coin.price()));
        holder.binding.itemRateChange.setText(percentFormatter.format(coin.change24h()));

        if (coin.change24h() > 0) holder.binding.itemRateChange.setTextColor(colorPositive);
        else holder.binding.itemRateChange.setTextColor(colorNegative);

        if (position % 2 == 0) holder.binding.getRoot().setBackgroundColor(colorOddLine);
        else holder.binding.getRoot().setBackgroundColor(colorEvenLine);

        imageLoader.load(BuildConfig.IMG_ENDPOINT + coin.id() + ".png")
            .into(holder.binding.itemRateImage);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        Context context = recyclerView.getContext();
        inflater = LayoutInflater.from(context);


        TypedValue typedValue = new TypedValue();

        // Set row background color
        context.getTheme().resolveAttribute(R.attr.evenLineBackground, typedValue, true);
        colorEvenLine = typedValue.data;
        context.getTheme().resolveAttribute(R.attr.oddLineBackground, typedValue, true);
        colorOddLine = typedValue.data;

        // Set change24h text field color
        context.getTheme().resolveAttribute(R.attr.textPositive, typedValue, true);
        colorPositive = typedValue.data;
        context.getTheme().resolveAttribute(R.attr.textNegative, typedValue, true);
        colorNegative = typedValue.data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemRateBinding binding;

        ViewHolder(@NonNull ItemRateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            CircleViewOutlineProvider.apply(binding.itemRateImage);
        }
    }
}
