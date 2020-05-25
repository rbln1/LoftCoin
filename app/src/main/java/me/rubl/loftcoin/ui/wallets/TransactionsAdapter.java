package me.rubl.loftcoin.ui.wallets;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import javax.inject.Inject;

import me.rubl.loftcoin.R;
import me.rubl.loftcoin.data.Transaction;
import me.rubl.loftcoin.databinding.ItemTransactionBinding;
import me.rubl.loftcoin.util.PriceFormatter;

public class TransactionsAdapter extends ListAdapter<Transaction, TransactionsAdapter.ViewHolder> {

    private final PriceFormatter priceFormatter;

    private LayoutInflater inflater;

    private int colorPositive = Color.GREEN;
    private int colorNegative = Color.RED;

    @Inject
    TransactionsAdapter(PriceFormatter priceFormatter) {
        super(new DiffUtil.ItemCallback<Transaction>() {
            @Override
            public boolean areItemsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
                return Objects.equals(oldItem.uid(), newItem.uid());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
        this.priceFormatter = priceFormatter;
    }

    @NonNull
    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionsAdapter.ViewHolder(ItemTransactionBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.ViewHolder holder, int position) {
        final Transaction transaction = getItem(position);

        holder.binding.itemTransactionAmount1.setText(priceFormatter.format(transaction.amount()));
        final double fiatAmount = transaction.amount() * transaction.coin().price();
        holder.binding.itemTransactionAmount2.setText(priceFormatter.format(transaction.coin().currencyCode(), fiatAmount));
        holder.binding.itemTransactionTimestamp.setText(DateFormat.getDateFormat(inflater.getContext()).format(transaction.createdAt()));

        if (transaction.amount() > 0) holder.binding.itemTransactionAmount2.setTextColor(colorPositive);
        else holder.binding.itemTransactionAmount2.setTextColor(colorNegative);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());

        Context context = recyclerView.getContext();

        TypedValue typedValue = new TypedValue();

        // Set transaction changes text field color
        context.getTheme().resolveAttribute(R.attr.textPositive, typedValue, true);
        colorPositive = typedValue.data;
        context.getTheme().resolveAttribute(R.attr.textNegative, typedValue, true);
        colorNegative = typedValue.data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemTransactionBinding binding;

        ViewHolder(@NonNull ItemTransactionBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setClipToOutline(true);
            this.binding = binding;
        }
    }
}