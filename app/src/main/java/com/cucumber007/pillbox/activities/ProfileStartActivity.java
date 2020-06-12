package com.cucumber007.pillbox.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cucumber007.pillbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileStartActivity extends AppCompatActivity {

    @BindView(R.id.weight_text) TextView weightText;
    @BindView(R.id.weight_seekbar) SeekBar weightSeekbar;

    @BindView(R.id.height_text) TextView heightText;
    @BindView(R.id.height_seekbar) SeekBar heightSeekbar;

    @BindView(R.id.slide_menu_button) ImageButton slideMenuButton;
    @BindView(R.id.buy_button) Button buyButton;

    public static final int MIN_WEIGHT = 30;
    public static final int MAX_WEIGHT = 150;
    public static final int MIN_HEIGHT = 1200;
    public static final int MAX_HEIGHT = 2000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_start);
        ButterKnife.bind(this);

        weightText.setText(Math.round(MIN_WEIGHT + (MAX_WEIGHT - MIN_WEIGHT) * (float) weightSeekbar.getProgress() / 100) + "kg");

        weightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weightText.setText(Math.round(MIN_WEIGHT + (MAX_WEIGHT - MIN_WEIGHT) * (float) progress / 100) + "kg");
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        heightText.setText(Math.round((MIN_HEIGHT + (MAX_HEIGHT - MIN_HEIGHT) * (float) heightSeekbar.getProgress() / 100) / 10) / (float) 100
                                   + "m");

        heightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                heightText.setText(
                        Math.round((MIN_HEIGHT + (MAX_HEIGHT - MIN_HEIGHT) * (float) progress / 100) / 10) / (float) 100
                                + "m");
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        slideMenuButton.setVisibility(View.GONE);
        buyButton.setText("Next step");
        buyButton.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.buy_button)
    void next() {
        finish();
    }
}
