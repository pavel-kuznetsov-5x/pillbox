package com.cucumber007.pillbox.activities.parameters;

import android.view.ViewGroup;

import com.cucumber007.pillbox.objects.pills.parameters.Period;

public class ParameterPeriodActivity extends AbstractParameterActivity {

    @Override
    String getName() {
        return "Period presets";
    }

    @Override
    protected void addItems(ViewGroup root) {
        root.addView(createDivider("every X hours"));
        root.addView(createParameterItemView(new Period(1)));
        root.addView(createParameterItemView(new Period(2)));
        root.addView(createParameterItemView(new Period(3)));
        root.addView(createCompositeParameterItemView(new Period(0)));
    }

}
