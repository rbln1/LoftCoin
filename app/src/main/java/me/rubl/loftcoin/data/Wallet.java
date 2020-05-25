package me.rubl.loftcoin.data;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Wallet {

    public static Wallet create(String id, Coin coin, Double balance) {
        return new AutoValue_Wallet(id, coin, balance);
    }

    public abstract String uid();

    public abstract Coin coin();

    public abstract double balance();

}
