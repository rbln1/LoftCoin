package me.rubl.loftcoin;

import android.content.Context;

import me.rubl.loftcoin.data.CoinsRepo;
import me.rubl.loftcoin.data.CurrencyRepo;
import me.rubl.loftcoin.data.WalletsRepo;
import me.rubl.loftcoin.util.ImageLoader;
import me.rubl.loftcoin.util.Notifier;
import me.rubl.loftcoin.util.RxSchedulers;


public interface BaseComponent {

    Context context();

    CoinsRepo coinsRepo();

    CurrencyRepo currencyRepo();

    WalletsRepo walletsRepo();

    ImageLoader imageLoader();

    RxSchedulers schedulers();

    Notifier notifier();
}
