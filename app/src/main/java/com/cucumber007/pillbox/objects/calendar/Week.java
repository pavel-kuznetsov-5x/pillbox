package com.cucumber007.pillbox.objects.calendar;


import android.content.Context;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

public class Week {

    private final Context context;
    public List<Day> days = new ArrayList<>();

    public Week(LocalDateTime date, Context context) {
        this.context = context;
        generate(LocalDate.from(date));
    }

    public Week(LocalDate date, Context context) {
        this.context = context;
        generate(date);
    }

    private void generate(LocalDate date) {
        Month month = date.getMonth();
        int dow = date.getDayOfWeek().getValue(); //1 - 7
        LocalDate start = date.minusDays(dow-1);

        for (int i = 0; i < 7; i++) {
            days.add(new Day(start, start.getMonth().equals(month), context));
            start = start.plusDays(1);
        }
        int a = 5;
    }

    public void updateActive() {
        for (int i = 0; i < days.size(); i++) {
            Day day = days.get(i);
            day.updateEvents();
        }
    }
}
