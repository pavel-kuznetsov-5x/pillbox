package com.cucumber007.pillbox.objects.pills;


import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.IconChoiseActivity;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.views.TimeDialog;

import org.threeten.bp.LocalTime;

import java.util.List;

public class MedPresenter {
    //Class, that connects views of MedActivity and Med data.

    private final PillboxEventsFactory eventsFactory;
    private Context context;
    private Med med;
    private TextView nameView;

    private ImageView iconView;
    private TextView dosageView;
    private TextView dailyUsageView;
    private TextView recurrenceView;
    private TextView durationView;
    private TextView startDateView;
    private TextView summaryView;
    private ViewGroup timeToTakeView;
    private Dialog dialog;
    private List<PillboxEvent> events;

    private MedSummary summary;

    private boolean changed = false;

    public MedPresenter(Context context, Med med) {
        this.med = med;
        this.context = context;
        if(med.getIconColor() == Med.DEFAULT_ICON_COLOR) med.setIconColor(context.getResources().getColor(R.color.pill_orange));
        eventsFactory = new PillboxEventsFactory(med);
        if(getMed().hasId()) events = loadEvents();
        else events = generateEvents();
        summary = new MedSummary(events);
    }

    public void update() {
        if(changed) events = generateEvents();

        setTextIfNotNull(nameView, med.getName());
        if (iconView != null) {
            iconView.setImageResource(IconChoiseActivity.MED_ICONS.get(med.getIcon()).getIconResource());
            iconView.setImageDrawable(IconChoiseActivity.createTintedDrawable(
                    context.getResources().getDrawable(IconChoiseActivity.MED_ICONS.get(med.getIcon()).getIconResource()),
                    med.getIconColor()
            ));
        }
        setTextIfNotNull(dosageView, med.getDosage().getTag());
        setTextIfNotNull(dailyUsageView, med.getDailyUsage().getTag());
        setTextIfNotNull(recurrenceView, med.getRecurrence().getTag());
        setTextIfNotNull(durationView, med.getDuration().getTag());
        setTextIfNotNull(startDateView, med.getStartDate().getTag()+", "
                +med.getTimeToTake().getStartTime().toString() + " - "
                +med.getTimeToTake().getEndTime().toString()
        );
        setTextIfNotNull(summaryView, generateSummary());
        fillTimeView(timeToTakeView);

    }

    private List<PillboxEvent> generateEvents() {
        return eventsFactory.createEvents(context);
    }

    private List<PillboxEvent> loadEvents() {
        return ModelManager.getInstance(context).getReminderModel().getEventsByMed(med.getId());
    }

    private String generateSummary() {
        String res = "";
        if(events.size() == 0) res += "0 doses\n";
        else res += summary.getQuantity() + " doses till " + events.get(events.size()-1).getDate() + "\n";
        if(summary.getTaken() >= 0) {
            res += "Taken: "+ summary.getTaken() + "\n";
            res += "Skipped: "+ summary.getSkipped() + "\n";
            if(summary.getLeft() <= 0)
                res += "Course completed\n";
            else res += "\n";//"Left: "+ (summary.getLeft(events.size())) + " doses\n";
        }
        return res;
    }

    public void setTextIfNotNull(TextView view, String text) {
        if (view != null) {
            view.setText(text);
        }
    }

    private void fillTimeView(ViewGroup view) {
        view.removeAllViews();

        List<LocalTime> times = med.getTimeToTake().getTimeList();
        int rows = (int) Math.ceil((float)times.size() / 3);

        for (int i = 0; i < rows; i++) {

            ViewGroup rowView = (ViewGroup) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.parameter_time_row, view, false);

            for (int j = 0; j < 3; j++) {

                View timeView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.parameter_time_element, rowView, false);

                if(i*3+j < times.size()) {
                    LocalTime time = times.get(i * 3 + j);
                    ((TextView) timeView.findViewById(R.id.time_value)).setText(time.toString());

                    final int index = i*3+j;

                    timeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String text = (String)((TextView)view.findViewById(R.id.time_value)).getText();
                            int hours = Integer.parseInt(text.split(":")[0]);
                            int minutes = Integer.parseInt(text.split(":")[1]);

                            ((TimePicker) dialog.findViewById(R.id.dialog_time_picker)).setCurrentHour(hours);
                            ((TimePicker) dialog.findViewById(R.id.dialog_time_picker)).setCurrentMinute(minutes);
                            ((TimeDialog) dialog).setTimeIndex(index);
                            dialog.show();
                        }
                    });

                }
                else {
                    ((TextView) timeView.findViewById(R.id.time_value)).setText("");
                    ((ImageView) timeView.findViewById(R.id.time_underline)).setImageDrawable(null);
                }
                rowView.addView(timeView);
            }
            view.addView(rowView);
        }

    }

    ///////////////////////////////

    public void setNameView(TextView nameView) {
        this.nameView = nameView;
        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                med.setName(editable.toString());
            }
        });
    }

    public void setIconView(ImageView iconView) {
        this.iconView = iconView;
    }

    public void setDosageView(TextView dosageView) {
        this.dosageView = dosageView;
    }

    public void setDailyUsageView(TextView dailyUsageView) {
        this.dailyUsageView = dailyUsageView;
    }

    public void setRecurrenceView(TextView recurrenceView) {
        this.recurrenceView = recurrenceView;
    }

    public void setDurationView(TextView durationView) {
        this.durationView = durationView;
    }

    public void setStartDateView(TextView startDateView) {
        this.startDateView = startDateView;
    }

    public void setTimeToTakeView(ViewGroup timeToTakeView, Dialog dialog) {
        this.timeToTakeView = timeToTakeView;
        this.dialog = dialog;
    }

    public void setSummaryView(TextView summaryView) {
        this.summaryView = summaryView;
    }

    public List<PillboxEvent> getEvents() {
        return events;
    }

    public Med getMed() {
        return med;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    ///////////////////////////////

    public class MedSummary {
        private int quantity;
        private int taken;
        private int skipped;

        public MedSummary(int quantity, int taken, int skipped) {
            this.quantity = quantity;
            this.taken = taken;
            this.skipped = skipped;
        }

        public MedSummary(List<PillboxEvent> pillboxEvents) {
            quantity = pillboxEvents.size();
            for (int i = 0; i < pillboxEvents.size(); i++) {
                PillboxEvent event =  pillboxEvents.get(i);
                if(event.getStatus() == PillboxEvent.STATUS_TAKEN) taken++;
                if(event.getStatus() == PillboxEvent.STATUS_SKIPPED) skipped++;
            }
        }

        public int getQuantity() {
            return quantity;
        }

        public int getTaken() {
            return taken;
        }

        public int getSkipped() {
            return skipped;
        }

        public int getLeft() {return  quantity - taken - skipped;}

        public int getLeft(int quantity) {return  quantity - taken - skipped;}
    }
}
