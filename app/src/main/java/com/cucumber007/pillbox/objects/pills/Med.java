package com.cucumber007.pillbox.objects.pills;

import android.content.ContentValues;
import android.graphics.Color;

import com.cucumber007.pillbox.database.tables.MedsTable;
import com.cucumber007.pillbox.objects.pills.parameters.DailyUsage;
import com.cucumber007.pillbox.objects.pills.parameters.Dosage;
import com.cucumber007.pillbox.objects.pills.parameters.Duration;
import com.cucumber007.pillbox.objects.pills.parameters.Recurrence;
import com.cucumber007.pillbox.objects.pills.parameters.StartDate;
import com.cucumber007.pillbox.objects.pills.parameters.TimeToTake;

import org.threeten.bp.LocalDate;

public class Med {
    //Class that contains all data about single med

    private long id;
    private String name;
    private String icon;
    private int iconColor;
    private Dosage dosage;
    private Recurrence recurrence;
    private Duration duration;
    private StartDate startDate;
    private TimeToTake timeToTake;

    public static final String DEFAULT_NAME = "";
    public static final String DEFAULT_ICON = "med_icon_tablet";
    public static final int DEFAULT_ICON_COLOR = Color.RED;
    public static final Dosage DEFAULT_DOSAGE = new Dosage(1, Dosage.DosageUnit.tablet);
    public static final DailyUsage DEFAULT_DAILY_USAGE = new DailyUsage(3, DailyUsage.DailyUsageUnit.daily);
    public static final Recurrence DEFAULT_RECURRENCE = new Recurrence(1, Recurrence.RecurrenceUnit.day);
    public static final Duration DEFAULT_DURATION = new Duration(3, Duration.DurationUnit.day);
    public static final StartDate DEFAULT_START_DATE = new StartDate(LocalDate.now());
    public static final TimeToTake DEFAULT_TIME_TO_TAKE = new TimeToTake(DEFAULT_DAILY_USAGE);


    public Med(long id, String name, String icon, int iconColor, Dosage dosage,
               Recurrence recurrence, Duration duration, StartDate startDate, TimeToTake timeToTake) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.iconColor = iconColor;
        this.dosage = dosage;
        this.recurrence = recurrence;
        this.duration = duration;
        this.startDate = startDate;
        this.timeToTake = timeToTake;
    }

    public int getId() {
        return (int)id;
    }

    public static Med getDefaultMed() {
        return new Med(-1, DEFAULT_NAME, DEFAULT_ICON, DEFAULT_ICON_COLOR, DEFAULT_DOSAGE,
                DEFAULT_RECURRENCE, DEFAULT_DURATION, DEFAULT_START_DATE, DEFAULT_TIME_TO_TAKE);
    }

    public boolean hasId() {
        return id != -1;
    }

    public void setDailyUsage(DailyUsage dailyUsage) {
        this.timeToTake = new TimeToTake(dailyUsage, getTimeToTake());
    }

    /////////////////////////////////////

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public Dosage getDosage() {
        return dosage;
    }

    public DailyUsage getDailyUsage() {
        return timeToTake.getDailyUsage();
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public Duration getDuration() {
        return duration;
    }

    public StartDate getStartDate() {
        return startDate;
    }

    public TimeToTake getTimeToTake() {
        return timeToTake;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setDosage(Dosage dosage) {
        this.dosage = dosage;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartDate(StartDate startDate) {
        this.startDate = startDate;
    }

    public void setTimeToTake(TimeToTake timeToTake) {
        this.timeToTake = timeToTake;
    }

    public int getIconColor() {
        return iconColor;
    }

    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MedsTable.COLUMN_NAME, getName());
        values.put(MedsTable.COLUMN_ICON, getIcon());
        values.put(MedsTable.COLUMN_ICON_COLOR, getIconColor());
        values.put(MedsTable.COLUMN_DOSAGE_VALUE, getDosage().getValue());
        values.put(MedsTable.COLUMN_DOSAGE_UNIT_ID, getDosage().getUnit().getId());
        values.put(MedsTable.COLUMN_DAILY_USAGE_VALUE, getDailyUsage().getValue());
        values.put(MedsTable.COLUMN_DAILY_USAGE_UNIT_ID, getDailyUsage().getUnit().getId());
        values.put(MedsTable.COLUMN_RECURRENCE_VALUE, getRecurrence().getValue());
        values.put(MedsTable.COLUMN_RECURRENCE_UNIT_ID, getRecurrence().getUnit().getId());
        values.put(MedsTable.COLUMN_DURATION_VALUE, getDuration().getValue());
        values.put(MedsTable.COLUMN_DURATION_UNIT_ID, getDuration().getUnit().getId());
        values.put(MedsTable.COLUMN_START_DATE_EPOCH_DAYS, getStartDate().getValue());
        values.put(MedsTable.COLUMN_START_TIME_SECONDS, getTimeToTake().getStartTime().toSecondOfDay());
        values.put(MedsTable.COLUMN_END_TIME_SECONDS, getTimeToTake().getEndTime().toSecondOfDay());
        values.put(MedsTable.COLUMN_TIME_TO_TAKE_LIST, getTimeToTake().getSecondsStringOutput());
        return values;
    }

}
