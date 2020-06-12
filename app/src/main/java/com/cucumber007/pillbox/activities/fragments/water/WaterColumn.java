package com.cucumber007.pillbox.activities.fragments.water;

/**
 * Created by anton on 04.08.2015.
 */
public class WaterColumn {
    public float targetHeight;
    public float height;
    public float speed;

    public WaterColumn(float height, float targetHeight, float speed){
        this.targetHeight = targetHeight;
        this.height = height;
        this.speed = speed;
    }

    public void update(float dampening, float tension){
        float x = targetHeight - height;
        speed += tension * x - speed * dampening;
        height += speed;
        height = Utils.filterNaN(height);
        speed = Utils.filterNaN(speed);
    }
}
