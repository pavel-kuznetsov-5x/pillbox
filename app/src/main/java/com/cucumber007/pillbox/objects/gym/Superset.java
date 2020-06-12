package com.cucumber007.pillbox.objects.gym;

import java.util.ArrayList;
import java.util.List;

public class Superset implements Comparable<Superset> {
    private int id;
    private boolean isFake;
    private int orderNum;
    private String title;
    private String description;
    private String duration;
    private String quantity;
    private String restDuration;
    private List<Set> sets = new ArrayList<Set>();

    @Override
    public int compareTo(Superset superset) {
        return orderNum - superset.orderNum;
    }

    public int getId() {
        return id;
    }

    public boolean isFake() {
        return isFake;
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

    public String getRestDuration() {
        return restDuration;
    }

    public List<Set> getSets() {
        return sets;
    }
}
