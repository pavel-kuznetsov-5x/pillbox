package com.cucumber007.pillbox.models;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;

import com.cucumber007.pillbox.activities.settings.AbstractSettingActivity;
import com.cucumber007.pillbox.activities.settings.ProfileSettingActivity;
import com.cucumber007.pillbox.activities.settings.dialogs.PhysicalDialogActivity;
import com.cucumber007.pillbox.activities.settings.dialogs.SexDialogActivity;

import org.threeten.bp.LocalDate;

public class WaterModel {
    private Context context;
    private final ContentResolver contentResolver;
    private SharedPreferences sharedPreferences;
    private static final String WATER_PREFERENCES = "pillbox_water_prefs";

    private Integer waterLevelMl = null;

    public static final float OZ = 0.033f;

    public WaterModel(Context context, ContentResolver contentResolver) {
        this.context = context;
        this.contentResolver = contentResolver;
        sharedPreferences = context.getSharedPreferences(WATER_PREFERENCES, Activity.MODE_PRIVATE);
    }

    public float getWaterPercent() {
        if (getWaterLimitMl() == 0) return 0;
        return (float)getWaterLevelMl() / getWaterLimitMl();
    }

    public int getWaterIntegerPercent() {
        if (getWaterLimitMl() == 0) return 0;
        return getWaterLevelMl() * 100 / getWaterLimitMl();
    }

    public void loadWaterLevel() {
        //todo check time zones
        String date = sharedPreferences.getString("date", LocalDate.now().toString());
        if(LocalDate.parse(date).isBefore(LocalDate.now())) {
            saveWaterLevel(0);
            waterLevelMl = 0;
        } else waterLevelMl = sharedPreferences.getInt("water_level", 0);
    }

    public void saveWaterLevel(int value) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt("water_level", value);
        ed.putString("date", LocalDate.now().toString());
        ed.apply();
    }

    public void addWater(int value) {
        waterLevelMl += value;
        if(waterLevelMl < 0) waterLevelMl = 0;
        saveWaterLevel(waterLevelMl);
    }


    public int getWaterLevelMl() {
        if(waterLevelMl == null) {
            loadWaterLevel();
        }
        return waterLevelMl;
    }

    public int getWaterLevelOz() {
        return (int)(getWaterLevelMl()*OZ);
    }


    public int getWaterLimitMl() {
        SharedPreferences shp = context.getSharedPreferences(AbstractSettingActivity.SETTINGS_SHARED_PREFERENCES,
                AbstractSettingActivity.MODE_PRIVATE);
        int age = shp.getInt(ProfileSettingActivity.PROFILE_AGE, 25);
        boolean sex = shp.getBoolean(SexDialogActivity.SEX_OPTION, true);
        int weight = shp.getInt(ProfileSettingActivity.PROFILE_WEIGHT, sex ? 75 : 60);
        int temp = ModelManager.getInstance(context).getWeatherModel().getTemperatureCelsius();
        int phys = shp.getInt(PhysicalDialogActivity.PHYSICAL_OPTION, 0);

        int water = (int)((0.0318*weight + 0.395*phys + (temp > 25 ? (temp-25)/10.0 : 0))*1000);

        return water;
    }

    public int getWaterLimitOz() {
        return (int)(getWaterLimitMl()*OZ);
    }
}


