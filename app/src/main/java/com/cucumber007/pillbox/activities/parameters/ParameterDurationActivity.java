package com.cucumber007.pillbox.activities.parameters;

import android.view.ViewGroup;

import com.cucumber007.pillbox.objects.pills.parameters.Duration;

public class ParameterDurationActivity extends AbstractParameterActivity {

    @Override
    String getName() {
        return "Duration";
    }

    @Override
    protected void addItems(ViewGroup root) {
        root.addView(createParameterItemView(new Duration(1, Duration.DurationUnit.day)));
        root.addView(createParameterItemView(new Duration(10, Duration.DurationUnit.day)));
        root.addView(createParameterItemView(new Duration(1, Duration.DurationUnit.week)));
        root.addView(createParameterItemView(new Duration(1, Duration.DurationUnit.month)));
        root.addView(createOtherParameterItemView(new Duration(0, Duration.DurationUnit.day)));
    }

}
