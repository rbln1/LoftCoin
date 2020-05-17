package me.rubl.loftcoin.ui.rates;

import androidx.lifecycle.ViewModelProvider;

import javax.inject.Singleton;

import dagger.Component;
import me.rubl.loftcoin.BaseComponent;
import me.rubl.loftcoin.util.ViewModelModule;

@Singleton
@Component(
    modules = {
        RatesModule.class,
        ViewModelModule.class
    },
    dependencies = {
        BaseComponent.class
    }
)
abstract class RatesComponent {

    abstract ViewModelProvider.Factory viewModelFactory();

    abstract RatesAdapter ratesAdapter();

}
