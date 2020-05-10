package me.rubl.loftcoin.util;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import me.rubl.loftcoin.BuildConfig;

public class PicassoImageLoader implements ImageLoader {

    @Override
    public void load(String url, ImageView view) {
        Picasso.get().load(url).into(view);
    }
}
