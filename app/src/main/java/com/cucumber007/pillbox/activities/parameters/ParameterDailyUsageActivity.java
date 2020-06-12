package com.cucumber007.pillbox.activities.parameters;

import android.view.ViewGroup;

import com.cucumber007.pillbox.objects.pills.parameters.DailyUsage;

public class ParameterDailyUsageActivity extends AbstractParameterActivity {

    @Override
    String getName() {
        //todo android strings
        return "Daily usage";
    }

    @Override
    protected void addItems(ViewGroup root) {
        root.addView(createDivider("X times a day"));
        root.addView(createParameterItemView(new DailyUsage(1, DailyUsage.DailyUsageUnit.daily)));
        root.addView(createParameterItemView(new DailyUsage(2, DailyUsage.DailyUsageUnit.daily)));
        root.addView(createParameterItemView(new DailyUsage(3, DailyUsage.DailyUsageUnit.daily)));
        root.addView(createCompositeParameterItemView(new DailyUsage(3, DailyUsage.DailyUsageUnit.daily)));

        /*root.addView(createDivider("X times in 24h"));
        root.addView(createParameterItemView(new DailyUsage(1, DailyUsage.DailyUsageUnit.h24)));
        root.addView(createParameterItemView(new DailyUsage(2, DailyUsage.DailyUsageUnit.h24)));
        root.addView(createParameterItemView(new DailyUsage(3, DailyUsage.DailyUsageUnit.h24)));
        root.addView(createCompositeParameterItemView(new DailyUsage(3, DailyUsage.DailyUsageUnit.h24)));*/

        //root.addView(createDivider(""));
        //root.addView(createOtherParameterItemView(new DailyUsage(0, DailyUsage.DailyUsageUnit.daily)));
    }

}
