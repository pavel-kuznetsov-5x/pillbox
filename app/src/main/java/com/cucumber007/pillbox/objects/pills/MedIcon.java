package com.cucumber007.pillbox.objects.pills;

import android.view.View;

public class MedIcon {

    private String name;
    private int icon;
    private View view;

    public MedIcon(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public int getIconResource() {
        return icon;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
