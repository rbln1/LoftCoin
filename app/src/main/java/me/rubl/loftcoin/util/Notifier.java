package me.rubl.loftcoin.util;

import androidx.annotation.NonNull;

import io.reactivex.Completable;

public interface Notifier {

    Completable sendMessage(@NonNull String title, @NonNull String message, @NonNull Class<?> receiver);

}
