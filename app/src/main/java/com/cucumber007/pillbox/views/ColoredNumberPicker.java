package com.cucumber007.pillbox.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import com.cucumber007.pillbox.R;

import java.lang.reflect.Field;

public class ColoredNumberPicker extends NumberPicker {

    public ColoredNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        Class<?> numberPickerClass = null;
        try {
            numberPickerClass = Class.forName("android.widget.NumberPicker");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Field selectionDivider = null;
        try {
            selectionDivider = numberPickerClass.getDeclaredField("mSelectionDivider");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            selectionDivider.setAccessible(true);
            selectionDivider.set(this, getResources().getDrawable(R.color.active_red));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
