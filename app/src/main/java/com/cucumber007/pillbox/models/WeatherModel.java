package com.cucumber007.pillbox.models;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.settings.AbstractSettingActivity;
import com.cucumber007.pillbox.activities.settings.dialogs.SystemDialogActivity;

public class WeatherModel {

    private Context context;
    private final ContentResolver contentResolver;

    private String icon;
    private String weatherDescription = "";
    private double temperatureKelvin = -1;


    public WeatherModel(Context context, ContentResolver contentResolver) {
        this.context = context;
        this.contentResolver = contentResolver;
    }

    public String getIcon() {
        return icon;
    }

    public String getWeatherText() {
        String weatherText = "No weather data";
        if(temperatureKelvin < 0) return weatherText;

        if(context.getSharedPreferences(AbstractSettingActivity.SETTINGS_SHARED_PREFERENCES, Activity.MODE_PRIVATE)
                .getBoolean(SystemDialogActivity.SYSTEM_OPTION, true)) {
            weatherText = ((Math.round(temperatureKelvin - 273.15))) + context.getResources().getString(R.string.celsius) + " " + weatherDescription;
        } else {
            weatherText = ((Math.round(temperatureKelvin*9/5 - 459.67))) + context.getResources().getString(R.string.fahrenheit) + " " + weatherDescription;
        }
        return weatherText;
    }

    public int getTemperatureCelsius() {
        return (int)(Math.round(temperatureKelvin - 273.15));
    }


    public void setTemperature(double temperatureKelvin) {
        this.temperatureKelvin = temperatureKelvin;
    }


    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }
}
