package com.cucumber007.pillbox.activities.settings.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cucumber007.pillbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhysicalDialogActivity extends Activity {

    public static final int LOW = 0;
    public static final int MEDIUM = 1;
    public static final int HIGH = 2;
    public static final String LOW_LABEL = "Low";
    public static final String MEDIUM_LABEL = "Medium";
    public static final String HIGH_LABEL = "Hard";
    public static final String PHYSICAL_OPTION = "chosen_physical";

    @BindView(R.id.low_chosen) ImageView lowChosen;
    @BindView(R.id.low) RelativeLayout low;
    @BindView(R.id.medium_chosen) ImageView mediumChosen;
    @BindView(R.id.medium) RelativeLayout medium;
    @BindView(R.id.high_chosen) ImageView highChosen;
    @BindView(R.id.high) RelativeLayout high;


    private View chosenIndicator;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_physical_dialog);
        //god forgive me for this workaround hell
        ButterKnife.bind(this);

        result = getIntent().getIntExtra(PHYSICAL_OPTION, 0);
        switch (result) {
            case PhysicalDialogActivity.LOW:
                choise(lowChosen);
                break;
            case PhysicalDialogActivity.MEDIUM:
                choise(mediumChosen);
                break;
            case PhysicalDialogActivity.HIGH:
                choise(highChosen);
                break;
        }

        low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = LOW;
                Intent intent = new Intent();
                intent.putExtra(PHYSICAL_OPTION, result);
                setResult(0, intent);
                choise(lowChosen);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = MEDIUM;
                Intent intent = new Intent();
                intent.putExtra(PHYSICAL_OPTION, result);
                setResult(0, intent);
                choise(mediumChosen);
            }
        });

        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = HIGH;
                Intent intent = new Intent();
                intent.putExtra(PHYSICAL_OPTION, result);
                setResult(0, intent);
                choise(highChosen);
            }
        });

    }

    private void choise(View indicator) {
        if (chosenIndicator != null) {
            chosenIndicator.setVisibility(View.INVISIBLE);
        }
        indicator.setVisibility(View.VISIBLE);
        chosenIndicator = indicator;
    }


    @Override
    public void onBackPressed() {
        setResult(0);
        super.onBackPressed();
    }


    @OnClick(R.id.confirm_button)
    public void click() {
        finish();
    }
}
