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

public class SexDialogActivity extends Activity {

    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    public static final String SEX_OPTION = "chosen_sex";


    @BindView(R.id.male_chosen) ImageView maleChosen;
    @BindView(R.id.male) RelativeLayout male;
    @BindView(R.id.female_chosen) ImageView femaleChosen;
    @BindView(R.id.female) RelativeLayout female;

    private View chosenIndicator;
    private boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_sex_dialog);
        //god forgive me for this workaround hell
        ButterKnife.bind(this);

        result = getIntent().getBooleanExtra(SEX_OPTION, true);
        if(result) choise(maleChosen);
        else choise(femaleChosen);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = true;
                Intent intent = new Intent();
                intent.putExtra(SEX_OPTION, result);
                setResult(0, intent);
                choise(maleChosen);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = false;
                Intent intent = new Intent();
                intent.putExtra(SEX_OPTION, result);
                setResult(0, intent);
                choise(femaleChosen);
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
