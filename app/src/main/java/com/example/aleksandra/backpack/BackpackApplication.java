package com.example.aleksandra.backpack;

import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;

@SuppressWarnings("ALL")
public class BackpackApplication extends MultiDexApplication {

    private static BackpackApplication applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
    }

    public static BackpackApplication getAppContext() {
        return applicationContext;
    }

    public static Resources getProjectResources() {
        return applicationContext.getResources();
    }

    public static String getAppString(int stringId) {
        return applicationContext.getResources().getString(stringId);
    }
}
