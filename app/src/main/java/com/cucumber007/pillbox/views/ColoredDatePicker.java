package com.cucumber007.pillbox.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.cucumber007.pillbox.R;

import java.lang.reflect.Field;

public class ColoredDatePicker extends DatePicker {

    public ColoredDatePicker(Context context) {
        super(context);
        init();
    }

    public ColoredDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColoredDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        try {
            Field[] datePickerFields = Class.forName("android.widget.DatePicker").getDeclaredFields();
            for (Field field : datePickerFields) {
                if ("mSpinners".equals(field.getName())) {
                    field.setAccessible(true);
                    Object spinnersObj = new Object();
                    spinnersObj = field.get(this);
                    LinearLayout mSpinners = (LinearLayout) spinnersObj;
                    NumberPicker monthPicker = (NumberPicker) mSpinners.getChildAt(0);
                    NumberPicker dayPicker = (NumberPicker) mSpinners.getChildAt(1);
                    NumberPicker yearPicker = (NumberPicker) mSpinners.getChildAt(2);
                    setDividerColor(monthPicker);
                    setDividerColor(dayPicker);
                    setDividerColor(yearPicker);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setDividerColor(NumberPicker picker) {
        Field[] numberPickerFields = NumberPicker.class.getDeclaredFields();
        for (Field field : numberPickerFields) {
            if (field.getName().equals("mSelectionDivider")) {
                field.setAccessible(true);
                try {
                    field.set(picker, getResources().getDrawable(R.color.active_red));

                } catch (IllegalArgumentException e) {
                    Log.v("cutag", "Illegal Argument Exception");
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    Log.v("cutag", "Resources NotFound");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.v("cutag", "Illegal Access Exception");
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
