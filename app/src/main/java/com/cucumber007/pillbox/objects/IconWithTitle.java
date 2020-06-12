package com.cucumber007.pillbox.objects;

import android.view.View;

public class IconWithTitle {
    private String title;
    private int iconResource;
    private View.OnClickListener onClickListener;

    public IconWithTitle(String title, int iconResource) {
        this.title = title;
        this.iconResource = iconResource;
    }

    public IconWithTitle(String title, int iconResource, View.OnClickListener onClickListener) {
        this.title = title;
        this.iconResource = iconResource;
        this.onClickListener = onClickListener;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResource() {
        return iconResource;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
