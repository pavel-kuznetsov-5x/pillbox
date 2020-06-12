package com.cucumber007.pillbox.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cucumber007.pillbox.objects.calendar.Week;
import com.cucumber007.pillbox.views.CalendarDayView;
import com.cucumber007.pillbox.views.CalendarWeekView;

import org.threeten.bp.LocalDate;

import java.util.HashMap;

public class CalendarViewPagerAdapter extends PagerAdapter {
    //Adapter for Calendar View in Reminder

    private static final int SIZE = 100000;
    private HashMap<Integer, Week> weeks = new HashMap<>(); //key = virtual position
    public static final LocalDate TODAY_DATE = LocalDate.now();

    private int virtualPosition = 0;
    private int lastPosition = SIZE/2;

    //todo tag?
    private CalendarDayView selectedView;
    private LocalDate selectedDate;

    private Context context;
    private LayoutInflater layoutInflater;

    private OnWeekDayChangeListener onDayChangeListener;

    public CalendarViewPagerAdapter(Context context) {
        this.context = context;
        for (int i = -1; i <= 1; i++) {
            weeks.put(i, generateWeek(i));
        }
        this.layoutInflater = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        lastPosition = SIZE/2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int realPosition) {
        CalendarWeekView weekView = new CalendarWeekView(context, getWeekFromVirtualPosition(getVirtualPositionFromReal(realPosition)));
        container.addView(weekView);
        weekView.fill(TODAY_DATE, selectedDate);
        weekView.setTag(getVirtualPositionFromReal(realPosition));

        //todo to viewpager
        weekView.setOnDayClickListener(new CalendarDayView.OnDayClickListener() {
            @Override
            public void onClick(CalendarDayView view, LocalDate day) {
                selectedDate = day;
                view.update(TODAY_DATE, selectedDate);
                if (selectedView != null) {
                    selectedView.update(TODAY_DATE, selectedDate);
                }
                selectedView = view;
                if (onDayChangeListener != null) {
                    onDayChangeListener.onChanged(day);
                }
            }
        });
        return weekView;
    }

    private Week generateWeek(int virtualPosition) {
        Week week = new Week(TODAY_DATE.plusDays(virtualPosition*7), context);
        return week;
    }

    public Week getWeekFromVirtualPosition(int virtualPosition) {
        Week week = weeks.get(virtualPosition);
        if (week == null) {
            week = generateWeek(virtualPosition);
            weeks.put(virtualPosition, week);
        }
        return week;
    }

    private int getVirtualPositionFromReal(int position) {
        return position - getCount()/2;
    }

    public void move(int position) {
        int vp = virtualPosition;

        if(position > lastPosition) {
            //RIGHT
            virtualPosition++;
        } else {
            //LEFT
            virtualPosition--;
        }

        //Log.d("cutag", position + ":" + vp+ " > " + virtualPosition);
        lastPosition = position;
    }

    public String getCurrentMonthText() {
        String text = "";
        text = getWeekFromVirtualPosition(virtualPosition).days.get(3).getDate().getMonth().toString();
        text = text.substring(0,1) + text.substring(1).toLowerCase() + " " + getWeekFromVirtualPosition(virtualPosition).days.get(3).getDate().getYear();
        return text;
    }

    ///////////////////////////////////////////

    @Override
    public int getCount() {
        return SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    public void setOnDayChangeListener(OnWeekDayChangeListener onDayChangeListener) {
        this.onDayChangeListener = onDayChangeListener;
    }

    public int getVirtualPosition() {
        return virtualPosition;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }



    //////////////////////////////////////////////




    public interface OnWeekDayChangeListener {
        void onChanged(LocalDate day);
    }
}
