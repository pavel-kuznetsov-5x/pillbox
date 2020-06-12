package com.cucumber007.pillbox.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.network.CookieCheckCallback;
import com.cucumber007.pillbox.network.RequestManager;
import com.cucumber007.pillbox.network.RequestService;
import com.cucumber007.pillbox.objects.gym.Media;
import com.cucumber007.pillbox.objects.gym.Set;
import com.cucumber007.pillbox.objects.gym.Superset;
import com.cucumber007.pillbox.objects.gym.Training;
import com.cucumber007.pillbox.objects.gym.TrainingDay;
import com.cucumber007.pillbox.objects.gym.TrainingSet;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class TrainingActivity extends AbstractTokenActivity {
    //Activity that displays single training in Gym module

    @BindView(R.id.download_button) Button downloadButton;
    @BindView(R.id.producer_name) TextView trainerName;
    @BindView(R.id.training_title) TextView trainingTitle;
    @BindView(R.id.training_place) TextView trainingPlace;
    @BindView(R.id.training_level) TextView trainingLevel;
    @BindView(R.id.training_duration) TextView trainingDuration;
    @BindView(R.id.price) TextView trainingPrice;
    @BindView(R.id.training_preview) ImageView trainingPreview;
    @BindView(R.id.goal) LinearLayout goals;
    @BindView(R.id.training_description) TextView trainingDescription;
    @BindView(R.id.day_tabs) LinearLayout dayTabs;
    @BindView(R.id.day) LinearLayout dayView;
    @BindView(R.id.days) LinearLayout days;
    @BindView(R.id.buy_button) Button buyButton;
    @BindView(R.id.animation_image) ProgressBar animationView;
    @BindView(R.id.place_label) TextView placeLabel;
    @BindView(R.id.level_label) TextView levelLabel;
    @BindView(R.id.duration_label) TextView durationLabel;
    @BindView(R.id.slide_menu_button) ImageView slideMenuButton;
    @BindView(R.id.training_area) LinearLayout trainingArea;
    @BindView(R.id.share_button) ShareButton shareButton;
    @BindView(R.id.tabHost) TabHost tabHost;
    @BindView(R.id.author_experience) TextView authorExperience;
    @BindView(R.id.author_name) TextView authorName;
    @BindView(R.id.author_rating) TextView authorRating;
    @BindView(R.id.author_avatar) ImageView authorAvatar;
    @BindView(R.id.author_description) TextView authorDescription;


    private Context context = this;
    private int trainingId;
    private Training activeTraining;
    private boolean downloaded;
    private Boolean purchased;

    private View lastTab;

    private int chosenDay = 0;

    private RequestService requestService;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        ButterKnife.bind(this);
        requestService = RequestManager.getInstance().getService();
        callbackManager = CallbackManager.Factory.create();

        setUpTabHost();

        trainingId = getIntent().getIntExtra("training_id", -1);
        purchased = getIntent().getBooleanExtra("is_purchased", false);

        loadTraining();

        slideMenuButton.setImageDrawable(getResources().getDrawable(R.drawable.arrow_left));

        //todo from server
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://microsoft.com"))
                //.setImageUrl(Uri.parse("https://pp.vk.me/c627226/v627226084/1cb9d/0Hxeci7Mbes.jpg"))
                .setImageUrl(Uri.parse("https://pp.vk.me/c627821/v627821698/21e60/A0ndZbAaq3o.jpg"))
                .setContentDescription("Pillbox")
                .build();

        shareButton.setShareContent(content);
        shareButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                buyTraining();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

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
            }
        });

        tabHost.setup();
        tabHost.getTabWidget().setDividerDrawable(null);
        TabHost.TabSpec spec = tabHost.newTabSpec("tag1");

        View tabItem = getLayoutInflater().inflate(R.layout.tab_title, null);
        ((TextView) tabItem.findViewById(R.id.tab_text)).setText("INFO");
        spec.setContent(R.id.tab1);
        spec.setIndicator(tabItem);
        tabHost.addTab(spec);

        /*tabItem = getLayoutInflater().inflate(R.layout.tab_title, null);
        ((TextView) tabItem.findViewById(R.id.tab_text)).setText("REVIEWS");
        spec = tabHost.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator(tabItem);
        tabHost.addTab(spec);*/

        /*tabItem = getLayoutInflater().inflate(R.layout.tab_title, null);
        ((TextView) tabItem.findViewById(R.id.tab_text)).setText("AUTHOR");
        spec = tabHost.newTabSpec("tag3");
        spec.setContent(R.id.tab3);
        spec.setIndicator(tabItem);
        tabHost.addTab(spec);*/

        tabHost.setCurrentTab(0);
        ((TextView) tabHost.getCurrentTabView().findViewById(R.id.tab_text)).setTextColor(getResources().getColor(R.color.black));
        lastTab = tabHost.getCurrentTabView();
    }

    @OnClick(R.id.slide_menu_button)
    public void slideButtonClick() {
        finish();
    }

    private void loadTraining() {
        showLoadAnimation();
        downloaded = ModelManager.getInstance(context).getGymModel().isTrainingDownloaded(trainingId);
        if (downloaded) {
            activeTraining = ModelManager.getInstance(context).getGymModel().getTrainingByGlobalId(trainingId);
            displayTraining();
            hideLoadAnimation();
        } else {
            final AbstractTokenActivity activity = this;
            requestService.getTraining(getToken(), trainingId, purchased ? Training.SCOPE_PURCHASED : Training.SCOPE_HIDDEN)
                    .enqueue(new CookieCheckCallback<Training>() {
                        @Override
                        public void onSuccess(Response<Training> response) {
                            if (response.body() != null) {
                                activeTraining = response.body();
                                displayTraining();
                                hideLoadAnimation();
                            } else hideLoadAnimation();
                        }

                        @Override
                        public void on401() {
                            onSessionOutDate(activity);
                        }

                        @Override
                        public void onFailure(Call<Training> call, Throwable t) {
                            Toast.makeText(context, "Loading error. Please check your connection and try again", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }

                    });

        }

    }

    private void displayTraining() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(activeTraining.getPreviewImageUrl(), trainingPreview);
        if (activeTraining.getProducerData().getAvatarUrl() != null) {
            imageLoader.displayImage(activeTraining.getProducerData().getAvatarUrl(), authorAvatar);
        }

        for (int i = 0; i < activeTraining.getGoalIds().size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.goal, null, false);
            ((TextView) view.findViewById(R.id.text)).setText(activeTraining.getGoalFromId(i));
            goals.addView(view);
        }

        displayTrainingElement(trainingTitle, activeTraining.getTitle());
        displayTrainingElement(trainerName, activeTraining.getProducerName());
        displayTrainingElement(trainingPrice, activeTraining.getPriceString());
        displayTrainingElement(trainingDescription, activeTraining.getDescription());
        displayTrainingElement(trainingDuration, activeTraining.getDuration(), durationLabel);
        displayTrainingElement(trainingLevel, activeTraining.getLevel(), levelLabel);
        displayTrainingElement(trainingPlace, activeTraining.getPlace(), placeLabel);
        displayTrainingElement(authorExperience, activeTraining.getProducerData().getExperience());
        displayTrainingElement(authorName, activeTraining.getProducerData().getFullName());

        if (activeTraining.getProducerData().getRating() == 0)
            displayTrainingElement(authorRating, "This author has no reviews yet");
        else
            displayTrainingElement(authorRating, "" + activeTraining.getProducerData().getRating());
        displayTrainingElement(authorDescription, "" + activeTraining.getProducerData().getDescription());


        if (activeTraining.getPrice() == 0) buyButton.setText("Get for free");
        else buyButton.setText("But it for " + activeTraining.getPriceString());

        showAdditionalData();
    }

    @OnClick(R.id.buy_button)
    public void buy() {
        buyTraining();
    }

    private void buyTraining() {
        requestService.buyTraining(getToken(), trainingId).enqueue(new CookieCheckCallback<Training>() {
            @Override
            public void onSuccess(Response<Training> response) {
                if (response.errorBody() == null) {
                    purchased = true;
                    activeTraining = response.body();
                    showAdditionalData();
                } else {
                    Toast.makeText(context, "Server error. Please try again", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void on401() {

            }

            @Override
            public void onFailure(Call<Training> call, Throwable t) {
                Toast.makeText(context, "Error. Please check your connection and try again", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }

        });
    }

    private void displayDays() {
        activeTraining.sort();
        if (activeTraining.getDays().size() == 0) return;
        dayTabs.setVisibility(View.VISIBLE);
        dayTabs.removeAllViews();

        for (int i = 0; i < activeTraining.getDays().size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.day_tab_title, null, false);
            ((TextView) view.findViewById(R.id.day_text)).setText("Day " + (i + 1));
            final int dayId = i;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayDay(dayId);
                    ((TextView) dayTabs.getChildAt(chosenDay).findViewById(R.id.day_text)).setTextColor(getResources().getColor(R.color.text_light));
                    ((TextView) dayTabs.getChildAt(dayId).findViewById(R.id.day_text)).setTextColor(getResources().getColor(R.color.text_dark));
                    chosenDay = dayId;
                }
            });
            dayTabs.addView(view);
        }

        ((TextView) dayTabs.getChildAt(0).findViewById(R.id.day_text)).setTextColor(getResources().getColor(R.color.black));
        chosenDay = 0;
        displayDay(0);
    }

    private void showAdditionalData() {

        if (activeTraining.isForShare()) {
            shareButton.setVisibility(purchased ? View.GONE : View.VISIBLE);
        } else {
            buyButton.setVisibility(purchased ? View.GONE : View.VISIBLE);
        }

        if (purchased) {
            downloadButton.setVisibility(View.VISIBLE);
            downloadButton.setSelected(downloaded);
            downloadButton.setText(!downloadButton.isSelected() ? "Download training" : "Remove from cache");
        } else {
            downloadButton.setVisibility(View.GONE);
        }

        if (purchased) {
            days.setVisibility(View.VISIBLE);
            displayDays();
        } else {
            days.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.download_button)
    public void download() {
        if (purchased) {
            if (!downloadButton.isSelected()) {
                downloadButton.setSelected(true);
                ModelManager.getInstance(context).getGymModel().saveTraining(activeTraining);
            } else {
                downloadButton.setSelected(false);
                ModelManager.getInstance(context).getGymModel().removeTraining(activeTraining.getId());
            }
            downloadButton.setText(!downloadButton.isSelected() ? "Download training" : "Remove from cache");
        }
    }

    private void displayDay(int index) {
        if (activeTraining.getDays() == null) return;
        if (activeTraining.getDays().size() <= index) return;
        View dayView = getLayoutInflater().inflate(R.layout.training_day, null, false);
        TrainingDay day = activeTraining.getDays().get(index);

        displayTrainingElement((TextView) dayView.findViewById(R.id.day_title), day.getTitle());
        displayTrainingElement((TextView) dayView.findViewById(R.id.day_description), day.getDescription());

        for (int i = 0; i < day.getTrainingSets().size(); i++) {
            TrainingSet trainingSet = activeTraining.getDays().get(index).getTrainingSets().get(i);
            ViewGroup trainingSetView = (ViewGroup) getLayoutInflater().inflate(R.layout.training_training_set, null, false);

            displayTrainingElement((TextView) trainingSetView.findViewById(R.id.training_set_title), (i + 1) + ". " + trainingSet.getTitle());
            displayTrainingElement((TextView) trainingSetView.findViewById(R.id.training_set_description), trainingSet.getDescription());
            displayTrainingElement((TextView) trainingSetView.findViewById(R.id.training_set_duration), trainingSet.getDuration(),
                    (TextView) trainingSetView.findViewById(R.id.duration_label));
            displayTrainingElement((TextView) trainingSetView.findViewById(R.id.training_set_rest_duration), trainingSet.getRestDuration(),
                    (TextView) trainingSetView.findViewById(R.id.rest_duration_label));

            for (int j = 0; j < trainingSet.getSupersets().size(); j++) {
                Superset superset = trainingSet.getSupersets().get(j);
                ViewGroup supersetView = (ViewGroup) getLayoutInflater().inflate(R.layout.training_superset, null, false);

                displayTrainingElement((TextView) supersetView.findViewById(R.id.super_set_title), superset.getTitle());
                displayTrainingElement((TextView) supersetView.findViewById(R.id.super_set_description), superset.getDescription());
                displayTrainingElement((TextView) supersetView.findViewById(R.id.super_set_duration), superset.getDuration(),
                        (TextView) supersetView.findViewById(R.id.duration_label));
                displayTrainingElement((TextView) supersetView.findViewById(R.id.super_set_rest_duration), superset.getRestDuration(),
                        (TextView) supersetView.findViewById(R.id.rest_duration_label));
                displayTrainingElement((TextView) supersetView.findViewById(R.id.super_set_quantity), superset.getQuantity(),
                        (TextView) supersetView.findViewById(R.id.quantity_label));

                if (superset.isFake()) {
                    supersetView.setBackgroundColor(getResources().getColor(R.color.fake_superset_background));
                    supersetView.setPadding(0, supersetView.getPaddingTop(), supersetView.getPaddingRight(), supersetView.getPaddingBottom());
                    for (int m = 0; m < supersetView.getChildCount(); m++) {
                        supersetView.getChildAt(m).setVisibility(View.GONE);
                    }
                }

                for (int k = 0; k < superset.getSets().size(); k++) {
                    Set set = superset.getSets().get(k);
                    ViewGroup setView = (ViewGroup) getLayoutInflater().inflate(R.layout.training_set, null, false);

                    displayTrainingElement((TextView) setView.findViewById(R.id.set_title), set.getTitle());
                    displayTrainingElement((TextView) setView.findViewById(R.id.set_duration), set.getDuration(),
                            (TextView) setView.findViewById(R.id.duration_label));
                    displayTrainingElement((TextView) setView.findViewById(R.id.set_quantity), set.getQuantity(),
                            (TextView) setView.findViewById(R.id.quantity_label));
                    displayTrainingElement((TextView) setView.findViewById(R.id.rest_duration), set.getRestDuration(),
                            (TextView) setView.findViewById(R.id.rest_duration_label));
                    displayTrainingElement((TextView) setView.findViewById(R.id.set_rep_duration), set.getRepDuration(),
                            (TextView) setView.findViewById(R.id.rep_duration_label));
                    displayTrainingElement((TextView) setView.findViewById(R.id.set_reps), set.getReps(),
                            (TextView) setView.findViewById(R.id.reps_label));

                    displayTrainingElement((ExpandableTextView) ButterKnife.findById(setView, R.id.expandable_description), set.getDescription());

                    for (int l = 0; l < set.getMedias().size(); l++) {
                        Media media = set.getMedias().get(l);
                        View mediaView = getLayoutInflater().inflate(R.layout.training_media, null, false);

                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.displayImage(media.getMediaUrl(), ((ImageView) mediaView.findViewById(R.id.media_image)));

                        setView.addView(mediaView);
                    }

                    supersetView.addView(setView);
                }

                trainingSetView.addView(supersetView);
            }

            ((ViewGroup) dayView.findViewById(R.id.training_sets)).addView(trainingSetView);
        }

        this.dayView.removeAllViews();
        this.dayView.addView(dayView);
        hideLoadAnimation();
    }

    private void displayTrainingElement(TextView textView, String text) {
        if (text == null || text.length() == 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void displayTrainingElement(ExpandableTextView textView, String text) {
        if (text == null || text.length() == 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void displayTrainingElement(TextView textView, String text, TextView label) {
        if (text == null || text.length() == 0) {
            textView.setVisibility(View.GONE);
            ((View) textView.getParent()).setVisibility(View.GONE);
            label.setVisibility(View.GONE);
        } else {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);
        }
    }

    //////////////////////////////

    //todo animation activity + loading listeners
    private void showLoadAnimation() {
        animationView.setVisibility(View.VISIBLE);
        trainingArea.setVisibility(View.INVISIBLE);
    }

    private void checkLoadAnimation() {
        if (true) hideLoadAnimation();
    }

    private void hideLoadAnimation() {
        animationView.setVisibility(View.GONE);
        trainingArea.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
