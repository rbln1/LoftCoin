package me.rubl.loftcoin;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import me.rubl.loftcoin.data.DataModule;
import me.rubl.loftcoin.util.UtilModule;

@Singleton
@Component(
    modules = {
        AppModule.class,
        DataModule.class,
        UtilModule.class
    }
)
abstract class AppComponent implements BaseComponent {

    @Component.Builder
    static abstract class Builder {

        @BindsInstance
        abstract Builder application(Application app);

        abstract AppComponent build();
    }

}
