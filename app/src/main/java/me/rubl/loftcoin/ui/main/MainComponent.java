package me.rubl.loftcoin.ui.main;

import javax.inject.Singleton;

import dagger.Component;
import me.rubl.loftcoin.BaseComponent;

@Singleton
@Component(
    modules = {
        MainModule.class
    },
    dependencies = {
        BaseComponent.class
    }
)
abstract class MainComponent {

    abstract void inject(MainActivity activity);

}
