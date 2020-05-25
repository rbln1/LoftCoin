package me.rubl.loftcoin.fcm;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import me.rubl.loftcoin.BaseComponent;
import me.rubl.loftcoin.LoftApp;
import me.rubl.loftcoin.R;
import me.rubl.loftcoin.ui.main.MainActivity;
import me.rubl.loftcoin.util.Notifier;

public class FcmService extends FirebaseMessagingService {

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject Notifier notifier;

    @Override
    public void onCreate() {
        super.onCreate();
        final BaseComponent baseComponent = ((LoftApp) getApplication()).getComponent();
        DaggerFcmComponent.builder().baseComponent(baseComponent).build().inject(this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        Timber.d("%s", remoteMessage.getNotification());

        final RemoteMessage.Notification notification = remoteMessage.getNotification();

        if (notification != null) {
            disposable.add(notifier.sendMessage(
                Objects.toString(notification.getTitle(), getString(R.string.default_channel_name)),
                Objects.toString(notification.getBody(), "Somethings wrong!"),
                MainActivity.class
            ).subscribe());
        }

//        component.notifier().sendMessage(
//                "Hello, LoftCoin",
//                "Test message",
//                MainActivity.class
//        ).subscribe();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
