package com.cucumber007.pillbox.activities;

import android.app.Activity;
import android.os.Bundle;

import com.cucumber007.pillbox.R;

import butterknife.ButterKnife;

public class TestActivity extends Activity {
    //Blank activity for experiments

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }
}
