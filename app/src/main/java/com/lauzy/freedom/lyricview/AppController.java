package com.lauzy.freedom.lyricview;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by @Saeed on 7/30/2018.
 */

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private static AppController instance;


    public static AppController getInstance() {
        return instance;
    }

    public static boolean hasNetwork() {
        return instance.isNetworkConnected();
    }

    public static DiskLruCache getDiskCache(Context context) {
        try {
            return DiskLruCache.open(context.getCacheDir(), 1, 1, 50 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }
        Toasty.Config.getInstance().setToastTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/by.ttf")).apply();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/by.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}