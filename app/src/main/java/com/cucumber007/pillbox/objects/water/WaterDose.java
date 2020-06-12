package com.cucumber007.pillbox.objects.water;

public class WaterDose {
    private int[] values;
    private String unit = " ml";

    public WaterDose(int[] values) {
        this.values = values;
    }

    public String getTag(int i) {
        return values[i] + " "+unit;
    }

    public int[] getValues() {
        return values;
    }
}
