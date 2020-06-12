package com.cucumber007.pillbox.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontTextView extends TextView {
    //todo attrs

    public static Typeface proximaSemiboldTypeface;


    public FontTextView(Context context) {
        super(context);
        initFont();
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont();
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont();
    }

    private void initFont() {
        if(proximaSemiboldTypeface == null) proximaSemiboldTypeface = Typeface.createFromAsset(getContext().getAssets(), "ProximaNovaSemibold.otf");
        setTypeface(proximaSemiboldTypeface);
    }
}
