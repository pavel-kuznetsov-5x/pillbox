package com.cucumber007.pillbox.activities.parameters;

import android.view.ViewGroup;

import com.cucumber007.pillbox.objects.pills.parameters.Recurrence;

public class ParameterRecurrenceActivity extends AbstractParameterActivity {

    @Override
    String getName() {
        return "Recurrence";
    }

    @Override
    protected void addItems(ViewGroup root) {
        root.addView(createParameterItemView(new Recurrence(1, Recurrence.RecurrenceUnit.day)));
        root.addView(createParameterItemView(new Recurrence(2, Recurrence.RecurrenceUnit.day)));
        root.addView(createParameterItemView(new Recurrence(3, Recurrence.RecurrenceUnit.day)));
        root.addView(createOtherParameterItemView(new Recurrence(0, Recurrence.RecurrenceUnit.day)));
    }
}
