package com.cucumber007.pillbox.objects.gym;


import java.util.ArrayList;
import java.util.List;

public class TrainingDay implements Comparable<TrainingDay> {

    private int id;
    private int orderNum;
    private String title;
    private String description;
    private String duration;
    private int dayNumber;
    private List<TrainingSet> trainingSets = new ArrayList<TrainingSet>();

    @Override
    public int compareTo(TrainingDay trainingDay) {
        return orderNum - trainingDay.orderNum;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<TrainingSet> getTrainingSets() {
        return trainingSets;
    }
}
