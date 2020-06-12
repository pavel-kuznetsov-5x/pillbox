package com.cucumber007.pillbox.objects.gym;

import java.util.ArrayList;
import java.util.List;

public class TrainingSet implements Comparable<TrainingSet> {
    private int id;
    private int orderNum;
    private String title;
    private String description;
    private String duration;
    private String restDuration;
    private List<Superset> superSets = new ArrayList<Superset>();

    @Override
    public int compareTo(TrainingSet trainingSet) {
        return orderNum - trainingSet.orderNum;
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

    public String getRestDuration() {
        return restDuration;
    }

    public List<Superset> getSupersets() {
        return superSets;
    }
}
