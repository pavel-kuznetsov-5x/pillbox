package com.cucumber007.pillbox.utils;

import android.util.Log;

public class ProfileTimer {

    long time;

    public ProfileTimer() {
        time = System.currentTimeMillis();
        Log.d("cutag", "" + System.currentTimeMillis());
    }

    public void logDelta(int id) {
        long now = System.currentTimeMillis();
        Log.d("cutag", id+": " + (System.currentTimeMillis()-time));
        time = now;
    }
}
