package com.cucumber007.pillbox.objects.water;

public class WaterType {
    private int iconResource;
    private float coef;
    private String name;
    private WaterDose dose;


    public WaterType(String name, float coef, int iconResource, WaterDose dose) {
        this.iconResource = iconResource;
        this.coef = coef;
        this.name = name;
        this.dose = dose;
    }

    public int getIconResource() {
        return iconResource;
    }

    public float getCoef() {
        return coef;
    }

    public String getName() {
        return name;
    }

    public WaterDose getDose() {
        return dose;
    }

    public int getWaterDelta(int dose) {
        return Math.round(dose*coef);
    }
}
