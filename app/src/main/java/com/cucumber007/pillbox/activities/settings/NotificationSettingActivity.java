package com.cucumber007.pillbox.activities.settings;

import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.utils.PillboxNotificationManager;

public class NotificationSettingActivity extends AbstractSettingActivity {

    public static final String WATER_NOTIFICATIONS = "water_notifications";
    public static final String PILLS_NOTIFICATIONS = "pills_notifications";

    @Override
    public void addItems(ViewGroup root) {
        root.addView(createSwitch("Pills", getSettingsSharedPreferences().getBoolean(PILLS_NOTIFICATIONS, true), new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) PillboxNotificationManager.getInstance(context)
                        .createAlarmsForEvents(ModelManager.getInstance(context).getReminderModel().getEvents());
                else PillboxNotificationManager.getInstance(context)
                        .deleteAlarmsForEvents(ModelManager.getInstance(context).getReminderModel().getEvents());

                SharedPreferences.Editor editor = getSettingsSharedPreferences().edit();
                editor.putBoolean(PILLS_NOTIFICATIONS, isChecked);
                editor.apply();
            }
        }));

        root.addView(createSwitch("Waterbalance", getSettingsSharedPreferences().getBoolean(WATER_NOTIFICATIONS, true), new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) PillboxNotificationManager.getInstance(context).createAlarmsForWater();
                else PillboxNotificationManager.getInstance(context).deleteAlarmsForWater();

                SharedPreferences.Editor editor = getSettingsSharedPreferences().edit();
                editor.putBoolean(WATER_NOTIFICATIONS, isChecked);
                editor.apply();
            }
        }));
    }

    @Override
    protected String getSettingName() {
        return "Notifications";
    }
}
