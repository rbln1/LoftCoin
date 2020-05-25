package me.rubl.loftcoin.ui.wallets;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import me.rubl.loftcoin.data.Coin;
import me.rubl.loftcoin.data.CurrencyRepo;
import me.rubl.loftcoin.data.Transaction;
import me.rubl.loftcoin.data.Wallet;
import me.rubl.loftcoin.data.WalletsRepo;
import me.rubl.loftcoin.util.RxSchedulers;

class WalletsViewModel extends ViewModel {

    private final Subject<Integer> walletPosition = BehaviorSubject.createDefault(0);

    private final Observable<List<Wallet>> wallets;

    private final Observable<List<Transaction>> transactions;

    private final WalletsRepo walletsRepo;
    private final CurrencyRepo currencyRepo;

    private final RxSchedulers schedulers;

    @Inject
    WalletsViewModel(WalletsRepo walletsRepo, CurrencyRepo currencyRepo, RxSchedulers schedulers) {
        this.walletsRepo = walletsRepo;
        this.currencyRepo = currencyRepo;
        this.schedulers = schedulers;

        wallets = currencyRepo.currency()
                .switchMap(walletsRepo::wallets)
                // cache() = replay() + autoconnect()
                .replay(1)
                .autoConnect();

        transactions =  wallets
                .switchMap((wallets) -> walletPosition
                    .map(wallets::get)
                )
                .switchMap(walletsRepo::transactions)
                .replay(1)
                .autoConnect();
    }

    @NonNull
    Observable<List<Wallet>> wallets() {
        return wallets.observeOn(schedulers.main());
    }

    @NonNull
    Observable<List<Transaction>> transactions() {
        return transactions.observeOn(schedulers.main());
    }

    @NonNull
    Completable addWallet() {
        return wallets
            .switchMapSingle((list) -> Observable
                .fromIterable(list)
                .map(Wallet::coin)
                .map(Coin::id)
                .toList()
            )
            .switchMapCompletable((ids) -> currencyRepo
                .currency()
                .firstOrError()
                .map((currency) -> walletsRepo.addWallet(currency, ids))
                .ignoreElement()
            )
            .observeOn(schedulers.main());
    }

    void changeWallet(int position) {
        walletPosition.onNext(position);
    }
}
