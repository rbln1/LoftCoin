package me.rubl.loftcoin.util;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.rubl.loftcoin.data.Wallet;

@Singleton
public class BalanceFormatter implements Formatter<Wallet> {

    @Inject
    BalanceFormatter() {
    }

    @NonNull
    @Override
    public String format(@NonNull Wallet value) {

        final DecimalFormat format = (DecimalFormat) NumberFormat.getCurrencyInstance();
        final DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(value.coin().symbol());
        format.setDecimalFormatSymbols(symbols);

        return format.format(value.balance());
    }
}
