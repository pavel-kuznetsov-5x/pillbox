package com.cucumber007.pillbox.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.settings.AbstractSettingActivity;
import com.cucumber007.pillbox.activities.settings.dialogs.SystemDialogActivity;
import com.cucumber007.pillbox.models.WaterModel;
import com.cucumber007.pillbox.objects.water.WaterDose;
import com.cucumber007.pillbox.objects.water.WaterType;
import com.cucumber007.pillbox.views.MyAnimatedExpandableListView;

import org.threeten.bp.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class WaterListAdapter extends MyAnimatedExpandableListView.AnimatedExpandableListAdapter {
    //Adapter for list of drinks in WaterAddActivity

    private Activity activity;
    private LayoutInflater inflater;

    private List<WaterType> items;
    private LocalDate day;

    private boolean ml = true;


    public WaterListAdapter(Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(this.activity);
        day = LocalDate.now();

        ml = activity.getSharedPreferences(AbstractSettingActivity.SETTINGS_SHARED_PREFERENCES, Activity.MODE_PRIVATE)
                .getBoolean(SystemDialogActivity.SYSTEM_OPTION, true);
    }


    public void setData(List<WaterType> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    @Override
    public WaterDose getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).getDose();
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getRealChildView(final int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
        DoseHolder holder;
        //if (convertView == null) {
        convertView = inflater.inflate(R.layout.water_dose, parent, false);
        holder = new DoseHolder(convertView);
        convertView.setTag(holder);
        //} else {
        //    holder = (DoseHolder) convertView.getTag();
        //}
        final WaterDose dose = getChild(groupPosition, childPosition);
        for (int i = 0; i < holder.doseTexts.size(); i++) {
            TextView doseView = holder.doseTexts.get(i);
            ViewGroup doseLayout = holder.doseLayouts.get(i);


            if (i < dose.getValues().length) {
                //todo normal oz doses
                doseView.setText(ml ? dose.getTag(i) : Math.round(dose.getValues()[i]* WaterModel.OZ*100)/100+" oz" );
                final int index = i;
                doseLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        int value = getGroup(groupPosition).getWaterDelta(dose.getValues()[index]);
                        intent.putExtra("value", value);
                        activity.setResult(Activity.RESULT_OK, intent);
                        if(value < 0)
                            Toast.makeText(activity, "This drink negatively impact on your water balance.", Toast.LENGTH_LONG).show();
                        activity.finish();
                    }
                });
            } else doseView.setVisibility(View.GONE);
        }


        return convertView;
    }


    @Override
    public int getRealChildrenCount(int groupPosition) {
        return 1;
    }


    @Override
    public WaterType getGroup(int groupPosition) {
        return items.get(groupPosition);
    }


    @Override
    public int getGroupCount() {
        return items.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        TypeHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.water_type, parent, false);
            holder = new TypeHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (TypeHolder) convertView.getTag();
        }
        WaterType type = getGroup(groupPosition);
        holder.icon.setImageResource(type.getIconResource());
        holder.name.setText(type.getName());

        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }


    static class TypeHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;


        TypeHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    static class DoseHolder {
        @BindViews({R.id.dose1_text, R.id.dose2_text, R.id.dose3_text}) List<TextView> doseTexts;
        @BindViews({R.id.dose1, R.id.dose2, R.id.dose3}) List<LinearLayout> doseLayouts;


        DoseHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
