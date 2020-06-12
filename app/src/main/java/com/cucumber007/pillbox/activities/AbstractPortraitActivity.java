package com.cucumber007.pillbox.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.cucumber007.pillbox.PillboxApplication;
import com.facebook.appevents.AppEventsLogger;

public abstract class AbstractPortraitActivity extends Activity {
    //Used to restrict app to work in portrait mode only

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        PillboxApplication application = (PillboxApplication) getApplication();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }



}
