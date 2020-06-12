package com.cucumber007.pillbox.objects.pills;


import com.cucumber007.pillbox.objects.pills.parameters.Dosage;

public class PillboxEventHolder implements Comparable {
    private long id;
    private long medId;
    private String name;
    private String date;
    private String food = "";
    private int iconResourceId;
    private int status;
    private int iconColor;
    private PillboxEvent event;
    private Dosage dosage;

    public PillboxEventHolder(PillboxEvent event, int iconResourceId, int iconColor) {
        this.event = event;
        this.iconColor = iconColor;
        this.id = event.getId();
        this.name = event.getMedName();
        this.date = event.getTime().toString();
        this.status = event.getStatus();
        this.iconResourceId = iconResourceId;
        this.medId = event.getMedId();
        this.dosage = event.getDosage();
    }

    @Override
    public int compareTo(Object another) {
        return event.getTime().toSecondOfDay() - ((PillboxEventHolder)another).getEvent().getTime().toSecondOfDay() ;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getFood() {
        return food;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public int getStatus() {
        return status;
    }

    public long getMedId() {
        return medId;
    }

    public int getIconColor() {
        return iconColor;
    }

    public PillboxEvent getEvent() {
        return event;
    }

    public Dosage getDosage() {
        return dosage;
    }
}
