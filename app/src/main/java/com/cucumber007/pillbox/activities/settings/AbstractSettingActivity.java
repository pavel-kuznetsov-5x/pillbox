package com.cucumber007.pillbox.activities.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.AbstractTokenActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class AbstractSettingActivity extends AbstractTokenActivity {

    @BindView(R.id.root) ViewGroup root;
    @BindView(R.id.title) TextView title;

    protected Context context = this;

    public static final String SETTINGS_SHARED_PREFERENCES = "pillbox_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        addItems(getRoot());
        title.setText(getSettingName());
    }

    protected abstract void addItems(ViewGroup root);

    protected abstract String getSettingName();

    protected ViewGroup getRoot() {
        return root;
    }

    protected View createSwitch(String name, boolean value, CompoundButton.OnCheckedChangeListener listener) {
        View swView = getLayoutInflater().inflate(R.layout.setting_switch, null, false);
        SwitchCompat sw = ((SwitchCompat) swView.findViewById(R.id.switch_unit));
        sw.setText(name);
        sw.setOnCheckedChangeListener(listener);
        sw.setChecked(value);
        return swView;
    }

    //workaround
    protected View createChoser(String name, final String startValue, View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.setting_choser, null, false);
        ((TextView) view.findViewById(R.id.name)).setText(name);
        ((TextView) view.findViewById(R.id.value)).setText(startValue);
        view.setOnClickListener(listener);
        return view;
    }

    protected View createSwitch(String name, boolean value, String onText, String offText, CompoundButton.OnCheckedChangeListener listener) {
        View swView = getLayoutInflater().inflate(R.layout.setting_switch, null, false);
        Switch sw = ((Switch) swView.findViewById(R.id.switch_unit));
        sw.setText(name);
        sw.setOnCheckedChangeListener(listener);
        sw.setChecked(value);
        sw.setTextOn(onText);
        sw.setTextOff(offText);
        return swView;
    }

    protected View createButton(String name, View.OnClickListener listener) {
        View sw = getLayoutInflater().inflate(R.layout.setting_button, null, false);
        ((TextView) sw.findViewById(R.id.name)).setText(name);
        sw.setOnClickListener(listener);
        return sw;
    }

    protected View createNumber(String name, String unit, int defaultValue, TextWatcher watcher) {
        View sw = getLayoutInflater().inflate(R.layout.setting_number, null, false);
        ((TextView) sw.findViewById(R.id.name)).setText(name);
        ((TextView) sw.findViewById(R.id.unit)).setText(unit);
        ((EditText) sw.findViewById(R.id.value)).setText(String.valueOf(defaultValue));
        ((EditText) sw.findViewById(R.id.value)).addTextChangedListener(watcher);
        return sw;
    }

    protected View createTextView(CharSequence text) {
        View view = getLayoutInflater().inflate(R.layout.setting_text, null, false);
        ((TextView) view.findViewById(R.id.text)).setText(text);
        return view;
    }

    protected SharedPreferences getSettingsSharedPreferences() {
        return this.getSharedPreferences(SETTINGS_SHARED_PREFERENCES, MODE_PRIVATE);
    }
}
