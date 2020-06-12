package com.cucumber007.pillbox;

import android.app.Application;

import com.cucumber007.pillbox.utils.FontsOverride;
import com.google.android.gms.analytics.Tracker;

import java.util.Locale;

public class PillboxApplication extends Application
{
    private Tracker mTracker;

    @Override
    public void onCreate()
    {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "ProximaNovaRegular.otf");
        Locale.setDefault(Locale.US);

//        LogUtil.setDebugMode(true);
//        HttpLogUtil.setDebugMode(true);
//        LogUtil.setContext(getApplicationContext());
    }


}
