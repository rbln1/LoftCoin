package me.rubl.loftcoin.ui.currency;

import androidx.lifecycle.ViewModelProvider;

import javax.inject.Singleton;

import dagger.Component;
import me.rubl.loftcoin.BaseComponent;
import me.rubl.loftcoin.util.ViewModelModule;

@Singleton
@Component(
    modules = {
        CurrencyModule.class,
        ViewModelModule.class
    },
    dependencies = {
        BaseComponent.class
    }
)
abstract class CurrencyComponent {

    abstract ViewModelProvider.Factory viewModelFactory();

}
