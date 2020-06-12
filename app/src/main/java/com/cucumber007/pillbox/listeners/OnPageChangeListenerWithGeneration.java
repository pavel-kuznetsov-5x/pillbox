package com.cucumber007.pillbox.listeners;

import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.cucumber007.pillbox.adapters.CalendarViewPagerAdapter;

public class OnPageChangeListenerWithGeneration implements ViewPager.OnPageChangeListener {
    //Listener for Calendar view in FragmentReminder, that generates new week views
    // during page change and sets month text

    private CalendarViewPagerAdapter adapter;
    private TextView monthView;

    public OnPageChangeListenerWithGeneration(CalendarViewPagerAdapter adapter, TextView monthView) {
        this.adapter = adapter;
        this.monthView = monthView;
        monthView.setText(adapter.getCurrentMonthText());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        adapter.move(position);
        monthView.setText(adapter.getCurrentMonthText());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}