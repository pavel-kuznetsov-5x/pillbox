package com.cucumber007.pillbox.models;

import android.content.ContentResolver;
import android.content.Context;

public class ModelManager {

    private static ModelManager instance;

    private ReminderModel reminderModel;
    private PillsModel pillsModel;
    private WaterModel waterModel;
    private GymModel gymModel;
    private WeatherModel weatherModel;
    private UserModel userModel;
    private Context context;
    private final ContentResolver contentResolver;

    private ModelManager(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
        reminderModel = new ReminderModel(context, contentResolver);
        pillsModel = new PillsModel(context, contentResolver);
        waterModel = new WaterModel(context, contentResolver);
        gymModel = new GymModel(context, contentResolver);
        weatherModel = new WeatherModel(context, contentResolver);
        userModel = new UserModel();
    }

    public static ModelManager getInstance(Context context) {
        if (instance == null) {
            instance = new ModelManager(context);
        }
        return instance;
    }

    public PillsModel getPillsModel() {
        return pillsModel;
    }

    public WaterModel getWaterModel() {
        return waterModel;
    }

    public ReminderModel getReminderModel() {
        return reminderModel;
    }


    public GymModel getGymModel() {
        return gymModel;
    }

    public WeatherModel getWeatherModel() {
        return weatherModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }
}