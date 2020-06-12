package com.cucumber007.pillbox.objects.pills;


import com.cucumber007.pillbox.objects.pills.parameters.Dosage;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

public class PillboxEvent {
    //Event data (pill consumption), that appears in FragmentReminder list

    private int id;
    private LocalDate date;
    private LocalTime time;
    private Dosage dosage;
    private long medId = -1;
    private int status = -1;
    private String medName;
    private String iconName;
    private int iconColor;

    public static final int STATUS_NONE = 0;
    public static final int STATUS_TAKEN = 1;
    public static final int STATUS_SKIPPED = 2;
    public static final int STATUS_RESCHEDULED = 3;

    public PillboxEvent(long id, long medId, LocalDate date, LocalTime time, Dosage dosage, int status, String medName, String iconName, int iconColor) {
        this.iconColor = iconColor;
        this.id = (int)id;
        this.date = date;
        this.time = time;
        this.dosage = dosage;
        this.medId = medId;
        this.status = status;
        this.medName = medName;
        this.iconName = iconName;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public long getMedId() {
        return medId;
    }

    public int getStatus() {
        return status;
    }

    public void setMedId(int medId) {
        this.medId = medId;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedName() {
        return medName;
    }

    public Dosage getDosage() {
        return dosage;
    }

    public String getIconName() {
        return iconName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIconColor() {
        return iconColor;
    }
}
