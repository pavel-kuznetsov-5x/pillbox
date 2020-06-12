package com.cucumber007.pillbox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.objects.IconWithTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsAdapter extends BaseAdapter {

    private final Context context;
    private final IconWithTitle[] icons;

    public SettingsAdapter(Context context, IconWithTitle[] icons) {
        this.context = context;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int position) {
        return icons[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.settings_element, null, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.settingTitle.setText(icons[position].getTitle());
        viewHolder.icon.setImageResource(icons[position].getIconResource());
        if (icons[position].getOnClickListener() != null) {
            view.setOnClickListener(icons[position].getOnClickListener());
        }

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.icon) ImageView icon;
        @BindView(R.id.setting_title) TextView settingTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
