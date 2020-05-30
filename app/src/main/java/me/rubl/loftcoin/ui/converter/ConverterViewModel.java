package me.rubl.loftcoin.ui.converter;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import me.rubl.loftcoin.data.Coin;
import me.rubl.loftcoin.data.CoinsRepo;
import me.rubl.loftcoin.data.CurrencyRepo;
import me.rubl.loftcoin.util.RxSchedulers;

class ConverterViewModel extends ViewModel {

    private final Subject<Coin> startCoin = BehaviorSubject.create();

    private final Subject<Coin> endCoin = BehaviorSubject.create();

    private final Subject<String> startCoinValue = BehaviorSubject.create();

    private final Subject<String> endCoinValue = BehaviorSubject.create();

    private final Observable<List<Coin>> topCoins;

    private final RxSchedulers schedulers;

    private final Observable<Double> factor;

    @Inject
    ConverterViewModel(CurrencyRepo currencyRepo, CoinsRepo coinsRepo, RxSchedulers schedulers) {
        this.schedulers = schedulers;

        this.topCoins = currencyRepo
            .currency()
            .switchMap(coinsRepo::topCoins)
            .doOnNext(coins -> startCoin.onNext(coins.get(0)))
            .doOnNext(coins -> endCoin.onNext(coins.get(1)))
            .replay(1)
            .autoConnect();

        factor = startCoin
            .flatMap((sc) -> endCoin
                .map((ec) -> sc.price() / ec.price()))
            .replay(1)
            .autoConnect();
    }

    @NonNull
    Observable<List<Coin>> topCoins() {
        return topCoins.observeOn(schedulers.main());
    }

    @NonNull
    Observable<Coin> startCoin() {
        return startCoin.observeOn(schedulers.main());
    }

    @NonNull
    Observable<Coin> endCoin() {
        return endCoin.observeOn(schedulers.main());
    }

    @NonNull
    Observable<String> startCoinValue() {
        return startCoinValue.observeOn(schedulers.main());
    }

    @NonNull
    Observable<String> endCoinValue() {
        return startCoinValue
            .observeOn(schedulers.cmp())
            .map((s) -> s.isEmpty() ? "0.0" : s)
            .map(Double::parseDouble)
            .flatMap((value) -> factor.map((f) -> value * f))
            .map(v -> String.format(Locale.US, "%.2f", v))
            .map((v) -> "0.00".equals(v) ? "" : v)
            .observeOn(schedulers.main());
    }

    void startCoin(Coin coin) {
        startCoin.onNext(coin);
    }

    void endCoin(Coin coin) {
        endCoin.onNext(coin);
    }

    void startCoinValue(CharSequence text) {
        startCoinValue.onNext(text.toString());
    }

    void endCoinValue(CharSequence text) {
        endCoinValue.onNext(text.toString());
    }

}