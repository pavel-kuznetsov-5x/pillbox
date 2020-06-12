package com.cucumber007.pillbox.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RecoverySystem;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.parameters.AbstractParameterActivity;
import com.cucumber007.pillbox.activities.parameters.ParameterDailyUsageActivity;
import com.cucumber007.pillbox.activities.parameters.ParameterDosageActivity;
import com.cucumber007.pillbox.activities.parameters.ParameterDurationActivity;
import com.cucumber007.pillbox.activities.parameters.ParameterPeriodActivity;
import com.cucumber007.pillbox.activities.parameters.ParameterRecurrenceActivity;
import com.cucumber007.pillbox.activities.parameters.ParameterStartDateActivity;
import com.cucumber007.pillbox.activities.settings.AbstractSettingActivity;
import com.cucumber007.pillbox.activities.settings.NotificationSettingActivity;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.objects.MedParameterException;
import com.cucumber007.pillbox.objects.pills.Med;
import com.cucumber007.pillbox.objects.pills.MedPresenter;
import com.cucumber007.pillbox.objects.pills.PillboxEvent;
import com.cucumber007.pillbox.objects.pills.parameters.DailyUsage;
import com.cucumber007.pillbox.objects.pills.parameters.Dosage;
import com.cucumber007.pillbox.objects.pills.parameters.Duration;
import com.cucumber007.pillbox.objects.pills.parameters.Period;
import com.cucumber007.pillbox.objects.pills.parameters.Recurrence;
import com.cucumber007.pillbox.objects.pills.parameters.StartDate;
import com.cucumber007.pillbox.utils.PillboxNotificationManager;
import com.cucumber007.pillbox.views.TimeDialog;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MedActivity extends AbstractPortraitActivity implements DialogInterface.OnCancelListener {
    //Activity to create/edit separate pill

    @BindView(R.id.activity_med_title) TextView activityMedTitle;
    @BindView(R.id.med_icon_change) ImageButton medIconView;
    @BindView(R.id.med_name_edit) EditText medNameView;
    @BindView(R.id.dosage_parameter) ViewGroup dosageParameter;
    @BindView(R.id.daily_usage_parameter) ViewGroup dailyUsageParameter;
    @BindView(R.id.recurrence_parameter) ViewGroup recurrenceParameter;
    @BindView(R.id.duration_parameter) ViewGroup durationParameter;
    @BindView(R.id.start_date_parameter) ViewGroup startDateParameter;
    @BindView(R.id.period_parameter) ViewGroup periodParameter;
    @BindView(R.id.time_layout) ViewGroup timeLayout;
    @BindView(R.id.summary_text) TextView summary;
    @BindView(R.id.animation_image) View animationView;
    @BindView(R.id.test) ViewGroup root;

    private Context context = this;

    private MedPresenter medAssociation;

    private Dialog dialog;

    private List<PillboxEvent> events;

    private Intent timeIntent;
    private boolean loading = false;

    public static final int RESULT_CODE_ICON = 1;
    public static final int RESULT_CODE_DOSAGE = 2;
    public static final int RESULT_CODE_DAILY_USAGE = 3;
    public static final int RESULT_CODE_RECURRENCE = 4;
    public static final int RESULT_CODE_DURATION = 5;
    public static final int RESULT_CODE_START_DATE_TIME = 6;
    public static final int RESULT_CODE_PERIOD = 7;

    int control = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med);
        ButterKnife.bind(this);

        //todo why this method runs 2 times?

        dialog = createTimeDialog(this);

        final int medId = getIntent().getIntExtra("med_id", -1);
        if (medId != -1) {
            medAssociation = new MedPresenter(this, ModelManager.getInstance(context).getPillsModel().getMed(medId));
            medAssociation.setChanged(true);
            activityMedTitle.setText("Edit med");
        } else {
            medAssociation = new MedPresenter(this, Med.getDefaultMed());
        }


        //////Parameters/////////////////////

        medNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                medAssociation.setChanged(true);
            }
        });

        medAssociation.setNameView(medNameView);

        medIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IconChoiseActivity.class);
                intent.putExtra("icon", medAssociation.getMed().getIcon());
                startActivityForResult(intent, 1);
            }
        });
        medAssociation.setIconView(medIconView);

        final TextView dosageValueView = setUpParameterView(
                dosageParameter,
                getString(R.string.parameter_dosage),
                RESULT_CODE_DOSAGE,
                new Intent(this, ParameterDosageActivity.class)
        );
        medAssociation.setDosageView(dosageValueView);

        final TextView dailyUsageValueView = setUpParameterView(
                dailyUsageParameter,
                getString(R.string.parameter_daily_usage),
                RESULT_CODE_DAILY_USAGE,
                new Intent(this, ParameterDailyUsageActivity.class)
        );
        medAssociation.setDailyUsageView(dailyUsageValueView);

        final TextView recurrenceValueView = setUpParameterView(
                recurrenceParameter,
                getString(R.string.parameter_recurrence),
                RESULT_CODE_RECURRENCE,
                new Intent(this, ParameterRecurrenceActivity.class)
        );
        medAssociation.setRecurrenceView(recurrenceValueView);

        final TextView durationValueView = setUpParameterView(
                durationParameter,
                getString(R.string.parameter_duration),
                RESULT_CODE_DURATION,
                new Intent(this, ParameterDurationActivity.class)
        );
        medAssociation.setDurationView(durationValueView);


        timeIntent = new Intent(this, ParameterStartDateActivity.class);
        final TextView startDateValueView = setUpParameterView(
                startDateParameter,
                getString(R.string.parameter_start),
                RESULT_CODE_START_DATE_TIME,
                timeIntent,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*timeIntent.putExtra("start", medAssociation.getMed().getTimeToTake().getStartTime().toSecondOfDay());
                        timeIntent.putExtra("end", medAssociation.getMed().getTimeToTake().getEndTime().toSecondOfDay());
                        LocalDate oth = medAssociation.getMed().getStartDate().getDate();
                        timeIntent.putExtra("date", oth);
                        timeIntent.putExtra("control", control);*/
                    }
                }
        );
        medAssociation.setStartDateView(startDateValueView);

        final TextView periodValueView = setUpParameterView(
                periodParameter,
                getString(R.string.parameter_period),
                RESULT_CODE_PERIOD,
                new Intent(this, ParameterPeriodActivity.class)
        );

        medAssociation.setTimeToTakeView(timeLayout, dialog);

        medAssociation.setSummaryView(summary);

        ///////////////////////////////

        medAssociation.update();
        int a = 5;
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeIntent.putExtra("start", medAssociation.getMed().getTimeToTake().getStartTime().toSecondOfDay());
        timeIntent.putExtra("end", medAssociation.getMed().getTimeToTake().getEndTime().toSecondOfDay());
        LocalDate oth = medAssociation.getMed().getStartDate().getDate();
        timeIntent.putExtra("date", oth);
        timeIntent.putExtra("control", control);
    }

    @OnClick(R.id.confirm_button)
    public void confirm() {
        showLoadAnimation();
        loading = true;
        if(medAssociation.isChanged()) new MedTask().execute();
        else finishAction();
    }

    private class MedTask extends AsyncTask {
        private Handler mHandler;
        private ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = ButterKnife.findById(animationView, R.id.progressBar);
            mHandler = new Handler();
        }

        private void setProgress(final int progress) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (progressBar != null) {
                        progressBar.setProgress(progress);
                    }
                }
            });

        }

        @Override
        protected Object doInBackground(final Object[] params) {
            setProgress(0);

            int createdId = -1;

            if (medAssociation.getMed().hasId()) {
                PillboxNotificationManager.getInstance(context)
                        .deleteAlarmsForEvents(ModelManager.getInstance(context).getReminderModel().getEventsByMed(medAssociation.getMed().getId()), new RecoverySystem.ProgressListener() {
                            @Override
                            public void onProgress(int progress) {
                                setProgress(20*progress/100);
                            }
                        });

                ModelManager.getInstance(context).getPillsModel().editMed(medAssociation.getMed());
            }
            else {
                createdId = ModelManager.getInstance(context).getPillsModel().createMed(medAssociation.getMed());
                medAssociation.getMed().setId(createdId);
                setProgress(20);
            }

            List<PillboxEvent> events = ModelManager.getInstance(context).getReminderModel().createPillboxEvents(
                    medAssociation.getMed().getId(),
                    medAssociation.getMed().getName(),
                    medAssociation.getEvents(),
                    new RecoverySystem.ProgressListener() {
                        @Override
                        public void onProgress(int progress) {
                            setProgress(20 + 75 * progress / 100);
                        }
                    });

            if(context.getSharedPreferences(AbstractSettingActivity.SETTINGS_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                    .getBoolean(NotificationSettingActivity.PILLS_NOTIFICATIONS, true)) {
                PillboxNotificationManager.getInstance(context).createAlarmsForEvents(events);
            }
            setProgress(100);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            finishAction();
        }
    }

    private void finishAction() {
        hideLoadAnimation();
        setResult(RESULT_OK);
        finish();
    }

    private TextView setUpParameterView(View parameterView, String parameterName, final int requestCode, final Intent intent) {
        ((TextView) parameterView.findViewById(R.id.parameter_name))
                .setText(parameterName);
        parameterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!loading) startActivityForResult(intent, requestCode);
            }
        });
        final TextView valueView = ((TextView) parameterView.findViewById(R.id.parameter_value));
        return valueView;
    }

    private TextView setUpParameterView(View parameterView, String parameterName, final int requestCode, final Intent intent, final View.OnClickListener listener) {
        ((TextView) parameterView.findViewById(R.id.parameter_name))
                .setText(parameterName);
        parameterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loading) startActivityForResult(intent, requestCode);
                listener.onClick(view);
            }
        });
        final TextView valueView = ((TextView) parameterView.findViewById(R.id.parameter_value));
        return valueView;
    }

    protected Dialog createTimeDialog(DialogInterface.OnCancelListener listener) {
        final TimeDialog dialog = new TimeDialog(context, R.style.DialogSlideAnim);
        dialog.setOnCancelListener(listener);

        View content = getLayoutInflater().inflate(R.layout.parameter_dialog_time, null, false);
        ((TimePicker) content.findViewById(R.id.dialog_time_picker)).setIs24HourView(true);

        content.findViewById(R.id.dialog_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.setContentView(content);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(null);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        TimeDialog dialog = (TimeDialog) dialogInterface;
        medAssociation.getMed().getTimeToTake().changeElement(dialog.getTimeIndex(), LocalTime.parse(
                AbstractParameterActivity.wrapInt(
                        ((TimePicker) dialog.findViewById(R.id.dialog_time_picker)).getCurrentHour()) + ":" +
                        AbstractParameterActivity.wrapInt(
                                ((TimePicker) dialog.findViewById(R.id.dialog_time_picker)).getCurrentMinute())
        ));
        medAssociation.update();
        //toto to med association
        medAssociation.setChanged(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    private void showLoadAnimation() {
        animationView.setVisibility(View.VISIBLE);
    }

    private void checkLoadAnimation() {
        if (true) hideLoadAnimation();
    }

    private void hideLoadAnimation() {
        animationView.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            switch (requestCode) {
                case RESULT_CODE_ICON:
                    medAssociation.getMed().setIcon(data.getStringExtra("icon"));
                    medAssociation.getMed().setIconColor(data.getIntExtra("color", medAssociation.getMed().getIconColor()));
                    break;
                case RESULT_CODE_DOSAGE:
                    medAssociation.getMed().setDosage(new Dosage(data.getIntExtra("value", 1), data.getIntExtra("unit", 0)));
                    break;
                case RESULT_CODE_DAILY_USAGE:
                    medAssociation.getMed().setDailyUsage(new DailyUsage(data.getIntExtra("value", 1), data.getIntExtra("unit", 0)));
                    break;
                case RESULT_CODE_RECURRENCE:
                    medAssociation.getMed().setRecurrence(new Recurrence(data.getIntExtra("value", 1), data.getIntExtra("unit", 0)));
                    break;
                case RESULT_CODE_DURATION:
                    medAssociation.getMed().setDuration(new Duration(data.getIntExtra("value", 1), data.getIntExtra("unit", 0)));
                    break;
                case RESULT_CODE_START_DATE_TIME:
                    control++;
                    LocalDateTime dateTime = LocalDateTime.ofEpochSecond(data.getIntExtra("value", 0), 0, ZoneOffset.UTC);
                    medAssociation.getMed().setStartDate(new StartDate(dateTime.toLocalDate()));
                    medAssociation.getMed().getTimeToTake().setStartTime(dateTime.toLocalTime());
                    medAssociation.getMed().getTimeToTake().setEndTime(LocalTime.ofSecondOfDay(data.getIntExtra("end", LocalTime.of(22,0).toSecondOfDay())));
                    break;
                case RESULT_CODE_PERIOD:
                    try {
                        medAssociation.getMed().getTimeToTake().setPeriod(new Period(data.getIntExtra("value", 0)));
                    } catch (MedParameterException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            medAssociation.update();
            medAssociation.setChanged(true);
        }
    }




}
