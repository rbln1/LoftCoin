package me.rubl.loftcoin;

import android.content.Context;

import me.rubl.loftcoin.data.CoinsRepo;
import me.rubl.loftcoin.data.CurrencyRepo;
import me.rubl.loftcoin.util.ImageLoader;


public interface BaseComponent {

    Context context();

    CoinsRepo coinsRepo();
    CurrencyRepo currencyRepo();

    ImageLoader imageLoader();

}
