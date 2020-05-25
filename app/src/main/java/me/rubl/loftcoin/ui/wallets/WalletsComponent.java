package me.rubl.loftcoin.ui.wallets;

import androidx.lifecycle.ViewModelProvider;

import javax.inject.Singleton;

import dagger.Component;
import me.rubl.loftcoin.BaseComponent;
import me.rubl.loftcoin.util.ViewModelModule;

@Singleton
@Component(
    modules = {
        WalletsModule.class,
        ViewModelModule.class
    },
    dependencies = {
        BaseComponent.class
    }
)
abstract class WalletsComponent {

    abstract ViewModelProvider.Factory viewModelFactory();

    abstract WalletsAdapter walletsAdapter();

    abstract TransactionsAdapter transactionsAdapter();
}
