package com.cucumber007.pillbox.activities.fragments.water;

/**
 * Created by anton on 06.08.2015.
 */
public class Utils {

    public static float getClosestPOT(float value){
        float pot = 1;
        while (value>pot){
            pot*=2;
        }
        return pot;
    }

    public static float filterNaN(float value){
        return (Float.isNaN(value)?0.f:value);
    }

    public static double filterNaN(double value){
        return (Double.isNaN(value)?0.f:value);
    }
}
