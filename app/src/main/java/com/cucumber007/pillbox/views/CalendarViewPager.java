package com.cucumber007.pillbox.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.cucumber007.pillbox.adapters.CalendarViewPagerAdapter;

public class CalendarViewPager extends ViewPager {

    public CalendarViewPager(Context context) {
        super(context);
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void refreshFrontView() {
        try {
            for (int i = -1; i <= 1; i++) {
                CalendarViewPagerAdapter adapter = (CalendarViewPagerAdapter) getAdapter();
                adapter.getWeekFromVirtualPosition(adapter.getVirtualPosition() + i).updateActive();

                int tag = adapter.getVirtualPosition() + i;
                CalendarWeekView view = ((CalendarWeekView) findViewWithTag(tag));
                view.updateActive();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
