package com.cucumber007.pillbox.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cucumber007.pillbox.R;

public class IconTextView extends FrameLayout {
    //todo other custom views
    //todo complete custom view, layout to params
    private static int res = R.layout.settings_element;

    public IconTextView(Context context) {
        super(context);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static IconTextView create(Drawable icon, String text, Context context) {
        IconTextView view = new IconTextView(context);
        View content = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(res, view, false);
        ((ImageView) content.findViewById(R.id.icon)).setImageDrawable(icon);
        TextView test = (TextView)content.findViewById(R.id.setting_title);
        test.setText(text);
        view.addView(content);
        return view;
    }

    public static IconTextView create(Drawable icon, String text, Context context, OnClickListener listener) {
        IconTextView view = new IconTextView(context);
        View content = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(res, view, false);
        ((ImageView) content.findViewById(R.id.icon)).setImageDrawable(icon);
        ((TextView) content.findViewById(R.id.setting_title)).setText(text);
        view.addView(content);
        view.setOnClickListener(listener);
        return view;
    }
}
