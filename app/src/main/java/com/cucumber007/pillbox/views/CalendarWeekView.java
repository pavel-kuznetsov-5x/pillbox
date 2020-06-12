package com.cucumber007.pillbox.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.objects.calendar.Day;
import com.cucumber007.pillbox.objects.calendar.Week;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarWeekView extends LinearLayout {

    @BindView(R.id.day_names_holder) LinearLayout dayNamesHolder;
    @BindView(R.id.days_holder) LinearLayout daysHolder;

    private List<CalendarDayView> days = new ArrayList<>();

    private String[] dayNames = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};

    private Week week;
    private LayoutInflater layoutInflater;
    private Context context;
    private CalendarDayView.OnDayClickListener onDayClickListener;

    public CalendarWeekView(Context context, Week week) {
        super(context);
        this.context = context;
        this.layoutInflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        //LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(layoutInflater.inflate(R.layout.calendar_week, null));
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        ButterKnife.bind(this);
        this.week = week;
        init(week);
    }

    private void init(Week week) {
        for (int i = 0; i < 5; i++) {
            CalendarDayView day = CalendarDayView.create(week.days.get(i), daysHolder);
            daysHolder.addView(day);
            days.add(day);
        }
        for (int i = 0; i < 2; i++) {
            CalendarDayView day = CalendarDayView.create(week.days.get(5+i), daysHolder);
            day.setHoliday(true);
            daysHolder.addView(day);
            days.add(day);
        }

        for (int i = 0; i < 7; i++) {
            Day weekday = week.days.get(i);
            CalendarDayView calendarDay = days.get(i);
            //calendarDay.setOnClickListener(new OnDayClickListener(weekday.getDate()));

            View dayName = layoutInflater.inflate(R.layout.day_name,dayNamesHolder, false);
            dayNamesHolder.addView(dayName);
            ((TextView) dayName.findViewById(R.id.name)).setText(dayNames[i]);
        }
    }

    public void fill(LocalDate today, LocalDate selectedDate) {
        for (int i = 0; i < days.size(); i++) {
            CalendarDayView calendarDay = days.get(i);
            calendarDay.update(today, selectedDate);
        }

    }

    public void updateActive() {
        for (int i = 0; i < days.size(); i++) {
            CalendarDayView dayView = days.get(i);
            dayView.updateActive();
        }
    }

    public void setOnDayClickListener(CalendarDayView.OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
        for (int i = 0; i < days.size(); i++) {
            CalendarDayView dayView = days.get(i);
            dayView.setOnDayClickListener(onDayClickListener);
        }
    }




}
