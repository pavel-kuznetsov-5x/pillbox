package com.cucumber007.pillbox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.adapters.WaterListAdapter;
import com.cucumber007.pillbox.objects.water.WaterDose;
import com.cucumber007.pillbox.objects.water.WaterType;
import com.cucumber007.pillbox.views.MyAnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WaterAddActivity extends AbstractPortraitActivity {
    //Activity to choose drink and add it's water to Water module

    private static List<WaterType> waterTypes = new ArrayList<>();
    private int lastExpandedPosition = -1;

    /*static {
        waterTypes.add(new WaterType("Water",   1, R.drawable.water_icon_water, new WaterDose(new int[] {100,300,1000})));
        waterTypes.add(new WaterType("Tea",     0.6f, R.drawable.water_icon_tea, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Coffee",  -0.25f, R.drawable.water_icon_coffee, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Milk",    0.8f, R.drawable.water_icon_milk, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Juice",   0.7f, R.drawable.water_icon_juice, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Soda",    0.4f, R.drawable.water_icon_soda, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Cola",    0.3f, R.drawable.water_icon_coke, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Vodka",   -4, R.drawable.water_icon_vodka, new WaterDose(new int[] {50,100,500})));
        waterTypes.add(new WaterType("Beer",    -0.5f, R.drawable.water_icon_beer, new WaterDose(new int[] {300,500,1000})));
        waterTypes.add(new WaterType("Wine",    -1.5f, R.drawable.water_icon_wine, new WaterDose(new int[] {100,300,500})));
    }*/

    static {
        waterTypes.add(new WaterType("Water",   1, R.drawable.water_icon_water, new WaterDose(new int[] {100,300,1000})));
        waterTypes.add(new WaterType("Tea",     0.8f, R.drawable.water_icon_tea, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Coffee",  0.2f, R.drawable.water_icon_coffee, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Milk",    0.7f, R.drawable.water_icon_milk, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Juice",   0.6f, R.drawable.water_icon_juice, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Soda",    0.6f, R.drawable.water_icon_soda, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Cola",    0.6f, R.drawable.water_icon_coke, new WaterDose(new int[] {100,200,500})));
        waterTypes.add(new WaterType("Vodka",   -3, R.drawable.water_icon_vodka, new WaterDose(new int[] {50,100,500})));
        waterTypes.add(new WaterType("Beer",    -0.2f, R.drawable.water_icon_beer, new WaterDose(new int[] {300,500,1000})));
        waterTypes.add(new WaterType("Wine",    -1f, R.drawable.water_icon_wine, new WaterDose(new int[] {100,300,500})));
    }

    @BindView(R.id.water_list_view) MyAnimatedExpandableListView waterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_add);
        ButterKnife.bind(this);

        WaterListAdapter waterAdapter = new WaterListAdapter(this);
        waterAdapter.setData(waterTypes);

        waterListView.setGroupIndicator(null);
        waterListView.setChildIndicator(null);
        waterListView.setDivider(getResources().getDrawable(R.color.transparent));
        waterListView.setDividerHeight(0);
        waterListView.setAdapter(waterAdapter);

        waterListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (waterListView.isGroupExpanded(groupPosition)) {
                    waterListView.collapseGroupWithAnimation(groupPosition);
                } else {
                    waterListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        waterListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    waterListView.collapseGroupWithAnimation(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

    }

    private TextView setUpWaterTypeView(View parameterView, String parameterName, int drawable, final Intent intent) {
        ((ImageView) parameterView.findViewById(R.id.icon))
                .setImageDrawable(getResources().getDrawable(drawable));
        ((TextView) parameterView.findViewById(R.id.name))
                .setText(parameterName);
        parameterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent, 0);
            }
        });
        final TextView valueView = ((TextView) parameterView.findViewById(R.id.parameter_value));
        return valueView;
    }

}
