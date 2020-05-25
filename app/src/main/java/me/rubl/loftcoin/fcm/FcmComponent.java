package me.rubl.loftcoin.fcm;

import javax.inject.Singleton;

import dagger.Component;
import me.rubl.loftcoin.BaseComponent;

@Singleton
@Component(
    modules = {
        FcmModule.class
    },
    dependencies = {
        BaseComponent.class
    }
)
abstract class FcmComponent {

    abstract void inject(FcmService service);

}
