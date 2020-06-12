package com.cucumber007.pillbox.activities.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.AbstractTokenActivity;
import com.cucumber007.pillbox.activities.MainActivity;
import com.cucumber007.pillbox.adapters.TopRatedPagerAdapter;
import com.cucumber007.pillbox.adapters.TrainingsAdapter;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.network.CookieCheckCallback;
import com.cucumber007.pillbox.network.RequestManager;
import com.cucumber007.pillbox.network.RequestService;
import com.cucumber007.pillbox.objects.gym.Training;
import com.cucumber007.pillbox.objects.net.TrainingsWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class FragmentGym extends Fragment {

    @BindView(R.id.animation_image) ProgressBar animationView;
    @BindView(android.R.id.tabs) TabWidget tabs;
    @BindView(R.id.trainings_list_price) ListView priceTrainingsList;
    @BindView(R.id.trainings_list_new) ListView newTrainingsList;
    @BindView(R.id.trainings_list_top_rated) ListView topRatedTrainingsList;
    @BindView(R.id.trainings_list_purchased) ListView purchasedTrainingsList;
    @BindView(R.id.tabHost) TabHost tabHost;
    @BindView(R.id.purchases_placeholder) TextView purchasesPlaceholder;

    private static final float TAB_FONT_DELTA = 0.5f;

    private Context context;
    //todo custom view
    private View lastTab;

    private TrainingsAdapter priceTrainingsAdapter;
    private TrainingsAdapter newTrainingsAdapter;
    private TrainingsAdapter topRatedTrainingsAdapter;
    private TrainingsAdapter purchasedTrainingsAdapter;

    private ViewPager topPager1;
    private ViewPager topPager2;
    private ViewPager topPager3;

    private boolean trainingsLoaded = false;
    private boolean trainingsLoaded1 = false;
    private boolean trainingsLoaded2 = false;
    private boolean purchasesLoaded = false;

    private RequestService requestService;
    private AbstractTokenActivity activity;

    public static final int FETCH = 5;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gym, null);
        context = container.getContext();
        activity = (AbstractTokenActivity) getActivity();
        requestService = RequestManager.getInstance().getService();
        ButterKnife.bind(this, v);

        setUpTabHost();

        /*View azPager = getActivity().getLayoutInflater().inflate(R.layout.trainings_view_pager, null, false);
        topPager1 = ButterKnife.findById(azPager, R.id.pager);
        View newPager = getActivity().getLayoutInflater().inflate(R.layout.trainings_view_pager, null, false);
        topPager2 = ButterKnife.findById(newPager, R.id.pager);
        View topRatedPager = getActivity().getLayoutInflater().inflate(R.layout.trainings_view_pager, null, false);
        topPager3 = ButterKnife.findById(topRatedPager, R.id.pager);*/

        priceTrainingsAdapter = new TrainingsAdapter(context);
        //priceTrainingsList.addHeaderView(azPager);
        priceTrainingsList.setAdapter(priceTrainingsAdapter);

        newTrainingsAdapter = new TrainingsAdapter(context);
        //newTrainingsList.addHeaderView(newPager);
        newTrainingsList.setAdapter(newTrainingsAdapter);

        topRatedTrainingsAdapter = new TrainingsAdapter(context);
        //topRatedTrainingsList.addHeaderView(topRatedPager);
        topRatedTrainingsList.setAdapter(topRatedTrainingsAdapter);

        purchasedTrainingsAdapter = new TrainingsAdapter(context, true);
        purchasedTrainingsList.setAdapter(purchasedTrainingsAdapter);

        showLoadAnimation();

        return v;
    }

    @OnClick(R.id.slide_menu_button)
    public void openSlideMenu() {
        ((MainActivity) getActivity()).openSlideMenu();
    }

    @OnClick(R.id.dump)
    public void dump() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void showTopRated(List<Training> list) {
        topPager1.setAdapter(new TopRatedPagerAdapter(getActivity(), list));
        topPager2.setAdapter(new TopRatedPagerAdapter(getActivity(), list));
        topPager3.setAdapter(new TopRatedPagerAdapter(getActivity(), list));
    }

    private void loadTrainings() {
        requestService.getTrainings(activity.getToken(), Training.SCOPE_OPEN, Training.SORT_NEWEST, FETCH, 0)
                .enqueue(new CookieCheckCallback<TrainingsWrapper>() {

                    @Override
                    public void onSuccess(Response<TrainingsWrapper> response) {
                        if (response.body() != null) {
                            newTrainingsAdapter.setTrainings(response.body().getTrainings());
                            trainingsLoaded = true;
                        } else {
                            try {
                                Toast.makeText(context, getResources().getString(R.string.server_error) + ". " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        checkLoadAnimation();
                    }


                    @Override
                    public void on401() {
                        onSessionOutDate(activity);
                    }

                    @Override
                    public void onFailure(Call<TrainingsWrapper> call, Throwable t) {
                        handleConnectionError(t);
                    }
                });

        requestService.getTrainings(activity.getToken(), Training.SCOPE_OPEN, Training.SORT_PRICE, FETCH, 0)
                .enqueue(new CookieCheckCallback<TrainingsWrapper>() {
                    @Override
                    public void onSuccess(Response<TrainingsWrapper> response) {
                        if (response.body() != null) {
                            priceTrainingsAdapter.setTrainings(response.body().getTrainings());
                            trainingsLoaded1 = true;
                        }
                        checkLoadAnimation();
                    }


                    @Override
                    public void on401() {
                        onSessionOutDate(activity);
                    }


                    @Override
                    public void onFailure(Call<TrainingsWrapper> call, Throwable t) {
                        handleConnectionError(t);
                    }
                });

        requestService.getTrainings(activity.getToken(), Training.SCOPE_OPEN, Training.SORT_RATING, FETCH, 0)
                .enqueue(new CookieCheckCallback<TrainingsWrapper>() {
                    @Override
                    public void onSuccess(Response<TrainingsWrapper> response) {
                        if (response.body() != null) {
                            topRatedTrainingsAdapter.setTrainings(response.body().getTrainings());
                            //showTopRated(response.body().getTrainings());
                            trainingsLoaded2 = true;
                        }
                        //todo reset token on UNAUTHORIZED
                        checkLoadAnimation();
                    }

                    @Override
                    public void on401() {
                        onSessionOutDate(activity);
                    }

                    @Override
                    public void onFailure(Call<TrainingsWrapper> call, Throwable t) {
                        handleConnectionError(t);
                    }
                });

        requestService.getPurchasedTrainings(activity.getToken(), Training.SCOPE_OPEN)
                .enqueue(new CookieCheckCallback<TrainingsWrapper>() {
                    @Override
                    public void onSuccess(Response<TrainingsWrapper> response) {
                        if (response.body() != null) {
                            List<Training> purchasedTrainings = response.body().getTrainings();
                            HashMap<Integer, Training> downloadedTrainings = ModelManager.getInstance(context).getGymModel()
                                    .getDownloadedTrainingsHashMap();
                            List<Training> trainings = new ArrayList<>();

                            trainings.addAll(downloadedTrainings.values());

                            for (int i = 0; i < purchasedTrainings.size(); i++) {
                                Training training = purchasedTrainings.get(i);
                                if (!downloadedTrainings.containsKey(training.getId())) {
                                    trainings.add(training);
                                }
                            }
                            purchasedTrainingsAdapter.setTrainings(trainings);
                            if (purchasesPlaceholder != null) {
                                purchasesPlaceholder.setVisibility(trainings.size() > 0 ? View.GONE : View.VISIBLE);
                            }
                            purchasesLoaded = true;
                        }
                        checkLoadAnimation();
                    }

                    @Override
                    public void on401() {
                        onSessionOutDate(activity);
                    }

                    @Override
                    public void onFailure(Call<TrainingsWrapper> call, Throwable t) {
                        purchasedTrainingsAdapter.setTrainings(ModelManager.getInstance(context).getGymModel().getDownloadedTrainings());
                    }

                });

    }

    private void showLoadAnimation() {
        animationView.setVisibility(View.VISIBLE);
        tabHost.setClickable(false);
        topRatedTrainingsList.setVisibility(View.GONE);
        newTrainingsList.setVisibility(View.GONE);
        priceTrainingsList.setVisibility(View.GONE);
    }

    private void checkLoadAnimation() {
        if (purchasesLoaded && trainingsLoaded && trainingsLoaded1 && trainingsLoaded2)
            hideLoadAnimation();
    }

    private void hideLoadAnimation() {
        try {
            animationView.setVisibility(View.GONE);
            tabHost.setClickable(true);
            topRatedTrainingsList.setVisibility(View.VISIBLE);
            newTrainingsList.setVisibility(View.VISIBLE);
            priceTrainingsList.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void handleConnectionError(Throwable t) {
        Toast.makeText(context, getResources().getString(R.string.trainings_loading_error), Toast.LENGTH_SHORT).show();
        hideLoadAnimation();
        purchasesPlaceholder.setVisibility(purchasedTrainingsAdapter.getCount() > 0 ? View.GONE : View.VISIBLE);
        Log.d("cutag", "" + "Error loading trainings: " + t.getMessage());
        t.printStackTrace();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTrainings();
    }

    private void setUpTabHost() {

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tag) {
                if (lastTab != null) {
                    //lastTab.findViewById(R.id.tab_choise_indicator).setVisibility(View.INVISIBLE);
                    TextView lastText = ButterKnife.findById(lastTab, R.id.tab_text);
                    lastText.setTextColor(getResources().getColor(R.color.text_light));
                    lastTab.findViewById(R.id.tab_choise_indicator).setVisibility(View.INVISIBLE);
                }
                //tabHost.getCurrentTabView().findViewById(R.id.tab_choise_indicator).setVisibility(View.VISIBLE);
                TextView newText = ButterKnife.findById(tabHost.getCurrentTabView(), R.id.tab_text);
                newText.setTextColor(getResources().getColor(R.color.text_dark));
                tabHost.getCurrentTabView().findViewById(R.id.tab_choise_indicator).setVisibility(View.VISIBLE);
                lastTab = tabHost.getCurrentTabView();

                if (purchasesPlaceholder != null && purchasedTrainingsAdapter != null) {
                    purchasesPlaceholder.setVisibility(purchasedTrainingsAdapter.getCount() > 0 ? View.GONE : View.VISIBLE);
                }
            }
        });


        tabHost.setup();
        tabHost.getTabWidget().setDividerDrawable(null);
        TabHost.TabSpec spec = tabHost.newTabSpec("tag1");

        View tabItem = getActivity().getLayoutInflater().inflate(R.layout.tab_title, null);
        ((TextView) tabItem.findViewById(R.id.tab_text)).setText("NEWEST");
        spec.setContent(R.id.tab1);
        spec.setIndicator(tabItem);
        tabHost.addTab(spec);

        tabItem = getActivity().getLayoutInflater().inflate(R.layout.tab_title, null);
        ((TextView) tabItem.findViewById(R.id.tab_text)).setText("BY PRICE");
        spec = tabHost.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator(tabItem);
        tabHost.addTab(spec);

        tabItem = getActivity().getLayoutInflater().inflate(R.layout.tab_title, null);
        ((TextView) tabItem.findViewById(R.id.tab_text)).setText("TOP RATED");
        spec = tabHost.newTabSpec("tag3");
        spec.setContent(R.id.tab3);
        spec.setIndicator(tabItem);
        tabHost.addTab(spec);

        tabItem = getActivity().getLayoutInflater().inflate(R.layout.tab_title, null);
        ((TextView) tabItem.findViewById(R.id.tab_text)).setText("PURCHASED");
        spec = tabHost.newTabSpec("tag4");
        spec.setContent(R.id.tab4);
        spec.setIndicator(tabItem);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        ((TextView) tabHost.getCurrentTabView().findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
        lastTab = tabHost.getCurrentTabView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("cutag", "" + requestCode + " " + resultCode);
    }


}
