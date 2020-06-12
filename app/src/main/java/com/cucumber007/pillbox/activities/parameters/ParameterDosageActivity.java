package com.cucumber007.pillbox.activities.parameters;

import android.view.ViewGroup;

import com.cucumber007.pillbox.objects.pills.parameters.Dosage;

public class ParameterDosageActivity extends AbstractParameterActivity {

    @Override
    String getName() {
        return "Dosage";
    }

    @Override
    protected void addItems(ViewGroup root) {
        root.addView(createParameterItemView(new Dosage(1, Dosage.DosageUnit.tablet)));
        root.addView(createParameterItemView(new Dosage(2, Dosage.DosageUnit.tablet)));
        root.addView(createCompositeParameterItemView(new Dosage(1, Dosage.DosageUnit.tablet)));
        //root.addView(createDivider(""));
        root.addView(createParameterItemView(new Dosage(5, Dosage.DosageUnit.mg)));
        root.addView(createParameterItemView(new Dosage(10, Dosage.DosageUnit.mg)));
        root.addView(createOtherParameterItemView(new Dosage(1, Dosage.DosageUnit.mg)));
    }

}
