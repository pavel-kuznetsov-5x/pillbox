package com.cucumber007.pillbox.activities.parameters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.objects.pills.PillboxEventsFactory;
import com.cucumber007.pillbox.objects.pills.parameters.AbstractMedParameter;
import com.cucumber007.pillbox.objects.pills.parameters.StartDate;
import com.cucumber007.pillbox.views.TimeDialog;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;

public class ParameterStartDateActivity extends AbstractParameterActivity {

    private LocalDate date = LocalDate.now()  ;
    private LocalTime startTime = PillboxEventsFactory.DAY_START;
    private LocalTime endTime = PillboxEventsFactory.DAY_END;

    private TextView dateView;
    private TextView startTimeView;
    private TextView endTimeView;

    private View chosen;

    @Override
    String getName() {
        return "Start date";
    }

    @Override
    protected void addItems(ViewGroup root) {
        View button = findViewById(R.id.button_submit);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("value", (int) LocalDateTime.of(date, startTime).toEpochSecond(ZoneOffset.UTC));
                intent.putExtra("end", (int) endTime.toSecondOfDay());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        root.addView(createParameterItemView(new StartDate(LocalDate.now().plusDays(0)), "Today"));
        root.addView(createParameterItemView(new StartDate(LocalDate.now().plusDays(1)), "Tomorrow"));
        root.addView(createParameterItemView(new StartDate(LocalDate.now().plusDays((7 - LocalDate.now().getDayOfWeek().getValue()) + 1)), "Next monday"));

        View view = createDatePickerItemView();
        dateView = (TextView)view.findViewById(R.id.additional_value);
        root.addView(view);

        ///////////////////////////////////////////////////////////

        LocalDate oth = (LocalDate)getIntent().getSerializableExtra("date");
        int controlIn = getIntent().getIntExtra("control", -1);
        if (oth != null) {
            date = oth;
        }
        startTime = LocalTime.ofSecondOfDay(getIntent().getIntExtra("start", startTime.toSecondOfDay()));
        endTime = LocalTime.ofSecondOfDay(getIntent().getIntExtra("end", endTime.toSecondOfDay()));

        final TimeDialog startDialog = new TimeDialog(context, startTime);
        startDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                TimeDialog dialog = (TimeDialog) dialogInterface;
                LocalTime time = LocalTime.parse(
                        AbstractParameterActivity.wrapInt(
                                ((TimePicker) dialog.findViewById(R.id.dialog_time_picker)).getCurrentHour()) + ":" +
                                AbstractParameterActivity.wrapInt(
                                        ((TimePicker) dialog.findViewById(R.id.dialog_time_picker)).getCurrentMinute()));
                if (!time.isBefore(endTime)) {
                    Toast.makeText(context, "Start time shouldn't be after end time", Toast.LENGTH_SHORT).show();
                } else if (time.getHour() >= 22) {
                    Toast.makeText(context, "Start time shouldn't be more than 22:00", Toast.LENGTH_SHORT).show();
                } else startTime = time;
                updateText();
            }
        });
        startDialog.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                minute = 0;
                view.setCurrentMinute(minute);
            }
        });
        View startView = createTimePickerItemView("Pick start time", startDialog);
        startTimeView = (TextView) startView.findViewById(R.id.additional_value);
        root.addView(startView);



        final TimeDialog endDialog = new TimeDialog(context, endTime);
        endDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                TimeDialog dialog = (TimeDialog) dialogInterface;
                LocalTime time = LocalTime.parse(
                        AbstractParameterActivity.wrapInt(
                                ((TimePicker) dialog.findViewById(R.id.dialog_time_picker)).getCurrentHour()) + ":" +
                                AbstractParameterActivity.wrapInt(
                                        ((TimePicker) dialog.findViewById(R.id.dialog_time_picker)).getCurrentMinute()));
                if (!time.isAfter(startTime)) {
                    Toast.makeText(context, "End time shouldn't be before start time", Toast.LENGTH_SHORT).show();
                } else endTime = time;
                updateText();
            }
        });
        endDialog.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                minute = 0;
                view.setCurrentMinute(minute);
            }
        });
        View endView = createTimePickerItemView("Pick end time", endDialog);
        endTimeView = (TextView) endView.findViewById(R.id.additional_value);
        root.addView(endView);

        updateText();
    }

    @Override
    protected View createParameterItemView(final AbstractMedParameter parameter, String title) {
        View view = super.createParameterItemView(parameter, title);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosen != null) {
                    chosen.findViewById(R.id.parameter_check).setVisibility(View.GONE);
                }
                chosen = v;
                v.findViewById(R.id.parameter_check).setVisibility(View.VISIBLE);
                date = ((StartDate) parameter).getDate();
                updateText();
            }
        });
        return view;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateText();
    }

    private void updateText() {
        try {
            dateView.setText(date.toString());
            startTimeView.setText(startTime.toString());
            endTimeView.setText(endTime.toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected View createDatePickerItemView() {
        final Dialog dialog = createDateDialog();
        View view = getLayoutInflater().inflate(R.layout.parameter_date_picker_item, null, false);

        ((TextView) view.findViewById(R.id.parameter_composite_item_name)).setText("Pick date");
        view.findViewById(R.id.additional_value).setVisibility(View.VISIBLE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        return view;
    }

    protected Dialog createDateDialog() {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);

        final View content = getLayoutInflater().inflate(R.layout.parameter_dialog_date, null, false);

        final DatePicker datePicker = (DatePicker)content.findViewById(R.id.parameter_date_picker);

        datePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        content.findViewById(R.id.dialog_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = LocalDate.parse(datePicker.getYear() + "-" + wrapInt(datePicker.getMonth()+1) + "-" + wrapInt(datePicker.getDayOfMonth()));
                if (chosen != null) {
                    chosen.findViewById(R.id.parameter_check).setVisibility(View.GONE);
                }
                updateText();
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

    protected View createTimePickerItemView(String name, final TimeDialog timeDialog) {
        View view = getLayoutInflater().inflate(R.layout.parameter_date_picker_item, null, false);

        ((TextView) view.findViewById(R.id.parameter_composite_item_name)).setText(name);
        view.findViewById(R.id.additional_value).setVisibility(View.VISIBLE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog.show();
            }
        });
        return view;
    }







}
