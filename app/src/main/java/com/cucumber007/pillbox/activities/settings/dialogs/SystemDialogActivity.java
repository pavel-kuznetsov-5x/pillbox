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

public class SystemDialogActivity extends Activity {

    public static final String METRIC = "Metric";
    public static final String IMPERIAL = "Imperial";
    public static final String SYSTEM_OPTION = "chosen_system";


    @BindView(R.id.male_chosen) ImageView metricChosen;
    @BindView(R.id.male) RelativeLayout metric;
    @BindView(R.id.female_chosen) ImageView imperialChosen;
    @BindView(R.id.female) RelativeLayout imperial;

    private View chosenIndicator;
    private boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_system_dialog);
        //god forgive me for this workaround hell
        ButterKnife.bind(this);

        result = getIntent().getBooleanExtra(SYSTEM_OPTION, true);
        if(result) choise(metricChosen);
        else choise(imperialChosen);

        metric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = true;
                Intent intent = new Intent();
                intent.putExtra(SYSTEM_OPTION, result);
                setResult(0, intent);
                choise(metricChosen);
            }
        });

        imperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = false;
                Intent intent = new Intent();
                intent.putExtra(SYSTEM_OPTION, result);
                setResult(0, intent);
                choise(imperialChosen);
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
