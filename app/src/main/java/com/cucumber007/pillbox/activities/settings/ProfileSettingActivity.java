package com.cucumber007.pillbox.activities.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.settings.dialogs.PhysicalDialogActivity;
import com.cucumber007.pillbox.activities.settings.dialogs.SexDialogActivity;
import com.cucumber007.pillbox.activities.settings.dialogs.SystemDialogActivity;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.network.RequestManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSettingActivity extends AbstractSettingActivity {

    public static final String PROFILE_AGE = "profile_age";
    public static final String PROFILE_SEX = "profile_sex";
    public static final String PROFILE_WEIGHT = "profile_weight";
    public static final String PROFILE_HEIGHT = "profile_height";
    public static final String PROFILE_SYSTEM = "profile_system";

    private View sexChoser;
    private Boolean startSex;
    public static final int REQUEST_SEX = 0;
    private View physicalChoser;
    private int startPhysical;
    public static final int REQUEST_PHYSICAL = 1;
    private View systemChoser;
    private Boolean startSystem;
    public static final int REQUEST_SYSTEM = 2;

    @Override
    public void addItems(ViewGroup root) {

        root.addView(createNumber("Age", "", getSettingsSharedPreferences().getInt(PROFILE_AGE, 25), new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = getSettingsSharedPreferences().edit();
                try {
                    editor.putInt(PROFILE_AGE, Integer.parseInt(s.toString()));
                    editor.apply();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    editor.remove(PROFILE_AGE);
                    editor.apply();
                }
            }
        }));

        //workaround
        startSex = getSettingsSharedPreferences().getBoolean(SexDialogActivity.SEX_OPTION, true);
        String startSexString = startSex ?
                SexDialogActivity.MALE : SexDialogActivity.FEMALE;


        sexChoser = createChoser("Sex", startSexString,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent = new Intent(context, SexDialogActivity.class);
                        intent.putExtra(SexDialogActivity.SEX_OPTION, startSex);
                        startActivityForResult(intent, REQUEST_SEX);
                    }
                }
        );
        root.addView(sexChoser);

        root.addView(createNumber("Weight", "kg", getSettingsSharedPreferences().getInt(PROFILE_WEIGHT, startSex ? 75 : 60), new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = getSettingsSharedPreferences().edit();
                try {
                    editor.putInt(PROFILE_WEIGHT, Integer.parseInt(s.toString()));
                    editor.apply();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    editor.remove(PROFILE_WEIGHT);
                    editor.apply();
                }
            }
        }));

        /*root.addView(createNumber("Height", "cm", getSettingsSharedPreferences().getInt(PROFILE_HEIGHT, 175), new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = getSettingsSharedPreferences().edit();
                try {
                    editor.putInt(PROFILE_HEIGHT, Integer.parseInt(s.toString()));
                    editor.apply();
                } catch (NumberFormatException e) {
                    editor.remove(PROFILE_HEIGHT);
                    editor.apply();
                    e.printStackTrace();
                }
            }
        }));*/

        startPhysical = getSettingsSharedPreferences().getInt(PhysicalDialogActivity.PHYSICAL_OPTION, PhysicalDialogActivity.LOW);
        String startPhysicalString = "";
        switch (startPhysical) {
            case PhysicalDialogActivity.LOW: startPhysicalString = PhysicalDialogActivity.LOW_LABEL; break;
            case PhysicalDialogActivity.MEDIUM: startPhysicalString = PhysicalDialogActivity.MEDIUM_LABEL; break;
            case PhysicalDialogActivity.HIGH: startPhysicalString = PhysicalDialogActivity.HIGH_LABEL; break;
        }
        physicalChoser = createChoser("Physical activity intensity", startPhysicalString,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent = new Intent(context, PhysicalDialogActivity.class);
                        intent.putExtra(PhysicalDialogActivity.PHYSICAL_OPTION, startPhysical);
                        startActivityForResult(intent, REQUEST_PHYSICAL);
                    }
                }
        );
        root.addView(physicalChoser);

        startSystem = getSettingsSharedPreferences().getBoolean(SystemDialogActivity.SYSTEM_OPTION, true);
        String startSystemString = startSystem ?
                SystemDialogActivity.METRIC : SystemDialogActivity.IMPERIAL;

        systemChoser = createChoser("System of measurement", startSystemString,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final Intent intent = new Intent(context, SystemDialogActivity.class);
                                            intent.putExtra(SystemDialogActivity.SYSTEM_OPTION, startSystem);
                                            startActivityForResult(intent, REQUEST_SYSTEM);
                                        }
                                    }
        );
        root.addView(systemChoser);

        root.addView(createButton("Clear purchases", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestManager.getInstance().getService().removePurchases(getToken()).enqueue(new Callback<Object>() {

                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }));

        root.addView(createButton("Clear cache", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelManager.getInstance(context).getGymModel().clearTrainingsCache();
            }
        }));

        root.addView(createButton("Logout", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                finish();
            }
        }));

    }

    @Override
    protected String getSettingName() {
        return "Profile data";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            switch (requestCode) {
                case REQUEST_SEX:
                    boolean result = data.getBooleanExtra(SexDialogActivity.SEX_OPTION, true);
                    SharedPreferences.Editor editor = getSettingsSharedPreferences().edit();
                    editor.putBoolean(SexDialogActivity.SEX_OPTION, result);
                    editor.apply();

                    ((TextView) sexChoser.findViewById(R.id.value)).setText(result ? SexDialogActivity.MALE : SexDialogActivity.FEMALE);
                    startSex = result;
                    break;
                case REQUEST_SYSTEM:
                    boolean result1 = data.getBooleanExtra(SystemDialogActivity.SYSTEM_OPTION, true);
                    SharedPreferences.Editor editor1 = getSettingsSharedPreferences().edit();
                    editor1.putBoolean(SystemDialogActivity.SYSTEM_OPTION, result1);
                    editor1.apply();

                    ((TextView) systemChoser.findViewById(R.id.value)).setText(result1 ? SystemDialogActivity.METRIC : SystemDialogActivity.IMPERIAL);
                    startSystem = result1;
                    break;
                case REQUEST_PHYSICAL:
                    int result2 = data.getIntExtra(PhysicalDialogActivity.PHYSICAL_OPTION, 0);
                    SharedPreferences.Editor editor2 = getSettingsSharedPreferences().edit();
                    editor2.putInt(PhysicalDialogActivity.PHYSICAL_OPTION, result2);
                    editor2.apply();

                    String text = "";
                    switch (result2) {
                        case PhysicalDialogActivity.LOW: text = PhysicalDialogActivity.LOW_LABEL; break;
                        case PhysicalDialogActivity.MEDIUM: text = PhysicalDialogActivity.MEDIUM_LABEL; break;
                        case PhysicalDialogActivity.HIGH: text = PhysicalDialogActivity.HIGH_LABEL; break;
                    }
                    ((TextView) physicalChoser.findViewById(R.id.value)).setText(text);
                    startPhysical = result2;
                    break;
            }

        }
    }
}
