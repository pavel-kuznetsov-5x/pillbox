package com.cucumber007.pillbox.activities.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.MainActivity;
import com.cucumber007.pillbox.activities.settings.NotificationSettingActivity;
import com.cucumber007.pillbox.activities.settings.PrivacyPolicySettingActivity;
import com.cucumber007.pillbox.activities.settings.ProfileSettingActivity;
import com.cucumber007.pillbox.adapters.SettingsAdapter;
import com.cucumber007.pillbox.objects.IconWithTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSettings extends Fragment {


    @BindView(R.id.settings_list_view) ListView settingsListView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = (ViewGroup) inflater.inflate(R.layout.fragment_settings, null);
        ButterKnife.bind(this, v);

        IconWithTitle[] settings = {
                new IconWithTitle("Profile data", R.drawable.settings_profile, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), ProfileSettingActivity.class));
                    }
                }),
                new IconWithTitle("Notifications", R.drawable.settings_notifications, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), NotificationSettingActivity.class));
                    }
                }),
                new IconWithTitle("Info", R.drawable.settings_info, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), PrivacyPolicySettingActivity.class));
                    }
                })
        };

        settingsListView.setAdapter(new SettingsAdapter(getActivity(), settings));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick(R.id.slide_menu_button)
    public void openSlideMenu() {
        ((MainActivity) getActivity()).openSlideMenu();
    }

}
