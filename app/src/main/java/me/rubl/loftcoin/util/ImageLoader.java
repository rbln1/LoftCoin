package me.rubl.loftcoin.util;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public interface ImageLoader {

    void load(String url, ImageView view);
}
