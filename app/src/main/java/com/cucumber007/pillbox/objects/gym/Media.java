package com.cucumber007.pillbox.objects.gym;

public class Media implements Comparable<Media> {
    private int id;
    private int orderNum = 0;
    private int typeId;
    private String mediaUrl;

    @Override
    public int compareTo(Media o) {
        return orderNum - o.orderNum;
    }

    public int getId() {
        return id;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }
}
