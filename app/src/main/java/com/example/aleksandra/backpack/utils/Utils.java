package com.example.aleksandra.backpack.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.aleksandra.backpack.BackpackApplication;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Utils {

    public static boolean hasNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) BackpackApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static void loadImageWithGlideCircle(Uri url, final ImageView imageView) {
        Glide.with(BackpackApplication.getAppContext())
                .load(url)
                .fitCenter()
                .bitmapTransform(new CropCircleTransformation(BackpackApplication.getAppContext()))
                .into(imageView);
    }

    public static void loadImageWithGlideCircleBlur(Uri url, final ImageView imageView) {
            Glide.with(BackpackApplication.getAppContext())
                .load(url)
                .bitmapTransform(new BlurTransformation(BackpackApplication.getAppContext(), 25),
                        new CropCircleTransformation(BackpackApplication.getAppContext()))
                .into(imageView);
    }

    public static void loadImageWithGlide(Uri url, final ImageView imageView) {
        Glide.with(BackpackApplication.getAppContext())
                .load(url)
                .asBitmap()
                .centerCrop()
                .into(imageView);
    }
}

