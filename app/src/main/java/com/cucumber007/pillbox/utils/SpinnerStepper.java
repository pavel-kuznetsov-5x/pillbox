package com.cucumber007.pillbox.utils;

import java.util.ArrayList;
import java.util.List;

public class SpinnerStepper {
    //Class, that generates steps with certain interval for Spinner

    private int min = 0;
    private int max = 100;
    private int step = 5;
    private List<String> values = new ArrayList<>();

    public SpinnerStepper(int min, int max, int step) {
        init(min, max, step);
    }

    public SpinnerStepper(int min, int max) {
        init(min, max);
    }

    public void init(int min, int max) {
        if(max-min < 50) step = 1;
        else if(max-min < 100) step = 5;
        else if(max-min < 500) step = 25;
        else if(max-min < 1000) step = 50;
        init(min, max, step);
    }

    public void init(int min, int max, int step) {
        this.min = min;
        this.max = max;
        this.step = step;

        values.clear();
        for (int i = min/step; i <= max/step; i++) {
            if(i!=0) values.add(""+(i*step));
        }
    }

    public int getRealValue(int fakeValue) {
        return Integer.parseInt(values.get(fakeValue));
    }

    public List<String> getValues() {
        return values;
    }

    public String[] getValuesArray() {
        String[] toArray = values.toArray(new String[values.size()]);
        return toArray;
    }
}
