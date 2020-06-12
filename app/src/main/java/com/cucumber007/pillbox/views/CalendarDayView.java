package com.cucumber007.pillbox.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.objects.calendar.Day;

import org.threeten.bp.LocalDate;


public class CalendarDayView extends RelativeLayout {

    private Day day;
    private CalendarDayView view = this;

    private boolean active = false;

    private boolean today = false;
    private boolean checked = false;
    private String number;
    private boolean holiday = false;
    private ImageView backgroundView;

    private ImageView activeView;
    private TextView numberView;
    private static int checked_background = R.drawable.calendar_chosen;
    private static int today_background = R.drawable.calendar_today;
    private static int active_dark = R.drawable.calendar_active_dark;
    //private static int active_light = R.drawable.calendar_active_light;

    private OnDayClickListener onDayClickListener;

    public CalendarDayView(Context context) {
        super(context);
    }

    public CalendarDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static CalendarDayView create(Day day, ViewGroup parent) {
        CalendarDayView instance = (CalendarDayView) ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.calendar_day, parent, false);
        instance.init(day);
        instance.setTag(day.getDate());
        return instance;
    }

    private void init(Day day) {
        this.day = day;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        backgroundView = (ImageView) findViewById(R.id.day_background);
        activeView = (ImageView) findViewById(R.id.active);
        numberView = (TextView) findViewById(R.id.number);

        activeView.setImageResource(active_dark);
    }

    public void update(LocalDate todayDate, LocalDate selectedDate) {
        today = todayDate.equals(day.getDate());
        if(selectedDate == null) checked = false;
        else checked = selectedDate.equals(day.getDate());
        updateActive();

        if(checked) {
            backgroundView.setImageResource(checked_background);
            numberView.setTextColor(getResources().getColor(R.color.reminder_calendar_chosen));
        }
        else {
            numberView.setTextColor(getResources().getColor(R.color.text_light));
            if(today) {
                backgroundView.setImageResource(today_background);
                numberView.setTextColor(getResources().getColor(R.color.reminder_calendar_today));
            } else backgroundView.setImageResource(R.color.transparent);
        }

        numberView.setText(day.getDate().getDayOfMonth() + "");
    }

    public void updateActive() {
        active = day.isAvailableEvent();
        activeView.setVisibility(active ? VISIBLE : INVISIBLE);
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setNumber(int number) {
        this.number = number+"";
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public void setOnDayClickListener(final OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDayClickListener.onClick(view, day.getDate());
            }
        });
    }

    public interface OnDayClickListener {
        void onClick(CalendarDayView view, LocalDate day);
    }
}
