package com.cucumber007.pillbox.objects.calendar;


import android.content.Context;

import com.cucumber007.pillbox.models.ModelManager;

import org.threeten.bp.LocalDate;

public class Day {
    private LocalDate date = LocalDate.now();
    private boolean availableEvent = false;
    private boolean chosen = false;
    private boolean otherMonth = false;
    private Context context;

    public Day(LocalDate date, boolean otherMonth, Context context) {
        this.date = date;
        this.otherMonth = otherMonth;
        this.context = context;
        //todo optimize?
        this.availableEvent = ModelManager.getInstance(context).getReminderModel().isEventsForDay(date);
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isAvailableEvent() {
        return availableEvent;
    }

    public void updateEvents() {
        this.availableEvent = ModelManager.getInstance(context).getReminderModel().isEventsForDay(date);
    }
}
