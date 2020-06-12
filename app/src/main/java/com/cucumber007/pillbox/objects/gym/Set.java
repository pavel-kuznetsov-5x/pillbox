package com.cucumber007.pillbox.objects.gym;

import java.util.ArrayList;
import java.util.List;

public class Set implements Comparable<Set> {
    private int id;
    private int orderNum;
    private String title;
    private String description;
    private String duration;
    private String quantity;
    private String reps;
    private String repDuration;
    private String setDuration;
    private String restDuration;
    private List<Media> medias = new ArrayList<>();

    @Override
    public int compareTo(Set set) {
        return orderNum - set.orderNum;
    }

    public int getId() {
        return id;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getReps() {
        return reps;
    }

    public String getRepDuration() {
        return repDuration;
    }

    public String getSetDuration() {
        return setDuration;
    }

    public String getRestDuration() {
        return restDuration;
    }

    public List<Media> getMedias() {
        return medias;
    }
}
