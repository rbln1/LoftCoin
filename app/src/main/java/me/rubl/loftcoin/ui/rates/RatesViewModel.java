package me.rubl.loftcoin.ui.rates;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import me.rubl.loftcoin.data.Coin;
import me.rubl.loftcoin.data.CoinsRepo;
import me.rubl.loftcoin.data.CurrencyRepo;
import me.rubl.loftcoin.data.SortBy;
import me.rubl.loftcoin.util.RxSchedulers;

class RatesViewModel extends ViewModel {

    private final Subject<Boolean> isRefreshing = BehaviorSubject.create();

    private final Subject<Class<?>> pullToRefresh = BehaviorSubject.createDefault(Void.TYPE);

    private final Subject<SortBy> sortBy = BehaviorSubject.createDefault(SortBy.RANK);

    private final Subject<Throwable> error = PublishSubject.create();

    private final Subject<Class<?>> onRetry = PublishSubject.create();

    private final AtomicBoolean forceUpdate = new AtomicBoolean();

    private final Observable<List<Coin>> coins;

    private final RxSchedulers schedulers;

    private int sortingIndex = 1;

    @Inject
    RatesViewModel(CoinsRepo coinsRepo, CurrencyRepo currencyRepo, RxSchedulers schedulers) {
        this.schedulers = schedulers;

        this.coins = pullToRefresh
            .map((ptr) -> CoinsRepo.Query.builder())
            .switchMap((qb) -> currencyRepo.currency()
                .map((c) -> qb.currency(c.code()))
            )
            .doOnNext((qb) -> forceUpdate.set(true))
            .doOnNext((qb) -> isRefreshing.onNext(true))
            .switchMap((qb) -> sortBy.map(qb::sortBy))
            .map((qb) -> qb.forceUpdate(forceUpdate.getAndSet(false)))
            .map(CoinsRepo.Query.Builder::build)
            .switchMap((q) -> coinsRepo
                .listings(q)
                .doOnError(error::onNext)
                .retryWhen(e -> onRetry)
            )
            .doOnEach((ntf) -> isRefreshing.onNext(false))
            .replay(1)
            .autoConnect();
    }

    @NonNull
    Observable<List<Coin>> coins() {
        return coins.observeOn(schedulers.main());
    }

    @NonNull
    Observable<Boolean> isRefreshing() {
        return isRefreshing.observeOn(schedulers.main());
    }

    @NonNull
    Observable<Throwable> onError() {
        return error.observeOn(schedulers.main());
    }

    final void refresh() {
        pullToRefresh.onNext(Void.TYPE);
    }

    void switchSortingOrder() {
        sortBy.onNext(SortBy.values()[sortingIndex++ % SortBy.values().length]);
    }

    void retry() {
        onRetry.onNext(Void.class);
    }
}
