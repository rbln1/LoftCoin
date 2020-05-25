package me.rubl.loftcoin.ui.converter;

import androidx.lifecycle.ViewModelProvider;

import javax.inject.Singleton;

import dagger.Component;
import me.rubl.loftcoin.BaseComponent;
import me.rubl.loftcoin.util.ViewModelModule;

@Singleton
@Component(
    modules = {
        ConverterModule.class,
        ViewModelModule.class
    },
    dependencies = {
        BaseComponent.class
    }
)
abstract class ConverterComponent {

    abstract ViewModelProvider.Factory viewModelFactory();

    abstract CoinSheetAdapter coinSheetAdapter();

}
