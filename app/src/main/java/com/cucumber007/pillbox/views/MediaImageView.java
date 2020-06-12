package com.cucumber007.pillbox.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class MediaImageView extends ImageView {

    public MediaImageView(Context context) {
        super(context);
    }

    public MediaImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float height = getDrawable().getIntrinsicHeight()* View.MeasureSpec.getSize(widthMeasureSpec)/getDrawable().getIntrinsicWidth();
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int)height, MeasureSpec.EXACTLY));
    }
}
