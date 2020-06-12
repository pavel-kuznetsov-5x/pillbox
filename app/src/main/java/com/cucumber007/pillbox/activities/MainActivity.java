package com.cucumber007.pillbox.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.fragments.FragmentGym;
import com.cucumber007.pillbox.activities.fragments.FragmentPills;
import com.cucumber007.pillbox.activities.fragments.FragmentReminder;
import com.cucumber007.pillbox.activities.fragments.FragmentSettings;
import com.cucumber007.pillbox.activities.fragments.FragmentWaterbalance;
import com.cucumber007.pillbox.adapters.SlideMenuAdapter;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.models.WeatherModel;
import com.cucumber007.pillbox.network.RequestManager;
import com.cucumber007.pillbox.objects.IconWithTitle;
import com.cucumber007.pillbox.utils.PillboxNotificationManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AbstractTokenActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.slide_menu_weather_text) TextView slideMenuWeatherText;
    @BindView(R.id.slide_pillbox_info) TextView slidePillboxInfo;
    @BindView(R.id.slide_waterbalance_info) TextView slideWaterbalanceInfo;
    @BindView(R.id.slide_menu_weather_icon) ImageView slideMenuWeatherIcon;
    @BindView(R.id.slide_menu_list_view) ListView slideMenuListView;
    @BindView(R.id.slide_menu_date) TextView slideMenuDate;
    @BindView(R.id.slide_menu_title) TextView slideMenuTitle;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    @BindString(R.string.open_weather_api_key) String openWeatherApiKey;

    private Context context = this;
    private SlideMenuAdapter slideMenuAdapter;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private FragmentWaterbalance waterFragment;
    private GoogleApiClient mGoogleApiClient;
    private LocalDateTime lastWeatherUpdate;

    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        onFirstLaunch();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
            .defaultDisplayImageOptions(defaultOptions)
            .build();
        ImageLoader.getInstance().init(config);

        buildGoogleApiClient();

        updateSlideMenuTimeTag();

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerOpened(View drawerView) {
                slidePillboxInfo.setText(ModelManager.getInstance(context).getPillsModel().getUntakenPillsForTodayQuantity() + " meds");
                slideWaterbalanceInfo.setText(ModelManager.getInstance(context).getWaterModel().getWaterIntegerPercent() + "%");
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        List<IconWithTitle> slideMenuItems = new ArrayList<>();
        slideMenuItems.add(new IconWithTitle("Reminder", R.drawable.slide_menu_icon_reminder));
        slideMenuItems.add(new IconWithTitle("Pills", R.drawable.slide_menu_icon_pills));
        slideMenuItems.add(new IconWithTitle("Water", R.drawable.slide_menu_icon_water));
        slideMenuItems.add(new IconWithTitle("Gym", R.drawable.slide_menu_icon_gym));
        slideMenuItems.add(new IconWithTitle("Settings", R.drawable.slide_menu_icon_settings));

        slideMenuAdapter = new SlideMenuAdapter(this, slideMenuItems, new SlideMenuAdapter.OnSlideItemClickListener() {
            @Override
            public void onClick(View v, int id) {
                switchFragment(id);
            }
        });
        slideMenuListView.setAdapter(slideMenuAdapter);
        slideMenuListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        Fragment reminderFragment = new FragmentReminder();
        Fragment pillsFragment = new FragmentPills();
        waterFragment = new FragmentWaterbalance();
        Fragment gymFragment = new FragmentGym();
        Fragment settingsFragment = new FragmentSettings();

        fragmentList.add(reminderFragment);
        fragmentList.add(pillsFragment);
        fragmentList.add(waterFragment);
        fragmentList.add(gymFragment);
        fragmentList.add(settingsFragment);

        //todo string placeholders
        slidePillboxInfo.setText(ModelManager.getInstance(context).getPillsModel().getUntakenPillsForTodayQuantity() + " pills");
        slideWaterbalanceInfo.setText(ModelManager.getInstance(context).getWaterModel().getWaterIntegerPercent() + "%");

        if (getIntent().getStringExtra("fragment") != null)
            if (getIntent().getStringExtra("fragment").equals("water")) switchFragment(2);
            else switchFragment(0);
        else switchFragment(0);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
        updateSlideMenuTimeTag();

        if(mGoogleApiClient.isConnected()) {
            if (lastWeatherUpdate != null) {
                if(lastWeatherUpdate.until(LocalDateTime.now(), ChronoUnit.MINUTES) > 3*60) {
                    LocationRequest mLocationRequest = new LocationRequest();
                    mLocationRequest.setInterval(5000);
                    mLocationRequest.setFastestInterval(1000);
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            if (location != null) {
                                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                                requestWeather(location);
                            }
                        }
                    });
                }
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayWeather();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //todo other activities
    }

    private void updateSlideMenuTimeTag() {
        LocalTime time = LocalTime.now();
        String plus = "night";
        int hour = time.getHour();
        if(hour>=6) plus = "morning";
        if(hour>=12) plus = "day";
        if(hour>=18) plus = "evening";
        slideMenuTitle.setText("Good " + plus);

        slideMenuDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("LLLL, dd").withLocale(Locale.getDefault())));
    }

    public void switchFragment(int position) {
        slideMenuListView.setItemChecked(position, true);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.screen_view, fragmentList.get(position));
        fragmentTransaction.commit();
        closeSlideMenu();
    }

    public void openSlideMenu() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    public void closeSlideMenu() {
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                    requestWeather(location);
                }
            }
        });

    }

    private void requestWeather(Location location) {
        RequestManager.getInstance().requestWeather(location, openWeatherApiKey, new RequestManager.RequestCallbackExecutable() {
            @Override
            public void execute(String parameter) {
                saveWeatherFromJSON(parameter);
                displayWeather();
                lastWeatherUpdate = LocalDateTime.now();
                Log.d("cutag", "" + "Weather request");
            }
        });
    }

    private void displayWeather() {
        WeatherModel weatherModel = ModelManager.getInstance(context).getWeatherModel();
        int icon = getResources().getIdentifier("weather_" + weatherModel.getIcon(), "drawable", getPackageName());
        slideMenuWeatherText.setText(weatherModel.getWeatherText());
        if (icon != 0)
            slideMenuWeatherIcon.setImageDrawable(getResources().getDrawable(icon));
    }

    private void saveWeatherFromJSON(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            ModelManager.getInstance(context).getWeatherModel().setTemperature(json.getJSONObject("main").getDouble("temp"));
            ModelManager.getInstance(context).getWeatherModel().setWeatherDescription(json.getJSONArray("weather").getJSONObject(0).getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void onFirstLaunch() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("first_launch", true)) {
            startActivityForResult(new Intent(this, ProfileStartActivity.class), 0);
            PillboxNotificationManager.getInstance(this).rescheduleAlarms();
            sharedPreferences.edit().putBoolean("first_launch", false).apply();
        }
        //startActivityForResult(new Intent(this, ProfileStartActivity.class), 0);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeSlideMenu();
        } else {
            if (doubleBackToExitPressedOnce) {
                //super.onBackPressed();
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click Back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("cutag", "Gapi connection suspended: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("cutag", "Gapi connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
