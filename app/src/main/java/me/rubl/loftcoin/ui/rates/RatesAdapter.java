package me.rubl.loftcoin.ui.rates;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import me.rubl.loftcoin.BuildConfig;
import me.rubl.loftcoin.R;
import me.rubl.loftcoin.data.Coin;
import me.rubl.loftcoin.databinding.ItemRateBinding;
import me.rubl.loftcoin.util.CircleViewOutlineProvider;
import me.rubl.loftcoin.util.Formatter;
import me.rubl.loftcoin.util.ImageLoader;
import me.rubl.loftcoin.util.PicassoImageLoader;

public class RatesAdapter extends ListAdapter<Coin, RatesAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private int colorPositive = Color.GREEN;
    private int colorNegative = Color.RED;
    private int colorEvenLine = Color.DKGRAY;
    private int colorOddLine = Color.GRAY;
    private Formatter<Double> priceFormatter;
    private Formatter<Double> percentFormatter;
    private ImageLoader imageLoader;

    protected RatesAdapter(Formatter<Double> priceFormatter, Formatter<Double> percentFormatter, ImageLoader imageLoader) {
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
    public void onBindViewHolder(@NonNull RatesAdapter.ViewHolder holder, int position) {
        final Coin coin = getItem(position);

        holder.binding.itemRateSymbol.setText(coin.symbol());
        holder.binding.itemRatePrice.setText(priceFormatter.format(coin.price()));
        holder.binding.itemRateChange.setText(percentFormatter.format(coin.change24h()));

        if (coin.change24h() > 0) holder.binding.itemRateChange.setTextColor(colorPositive);
        else holder.binding.itemRateChange.setTextColor(colorNegative);

        if (position % 2 == 0) holder.binding.getRoot().setBackgroundColor(colorOddLine);
        else holder.binding.getRoot().setBackgroundColor(colorEvenLine);

        imageLoader.load(BuildConfig.IMG_ENDPOINT + coin.id() + ".png", holder.binding.itemRateImage);
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
