package com.cucumber007.pillbox.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.cucumber007.pillbox.R;

import org.threeten.bp.LocalTime;


public class TimeDialog extends Dialog {

    private LocalTime time = LocalTime.of(8,0);
    private int timeIndex = -1;
    private TimePicker picker;

    public TimeDialog(Context context, LocalTime time) {
        super(context, R.style.DialogSlideAnim);
        this.time = time;
        init();
    }

    public TimeDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    public TimeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    protected void init() {
        View content = getLayoutInflater().inflate(R.layout.parameter_dialog_time, null, false);
        picker = ((TimePicker) content.findViewById(R.id.dialog_time_picker));
        picker.setIs24HourView(true);
        picker.setCurrentMinute(time.getMinute());
        picker.setCurrentHour(time.getHour());
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        content.findViewById(R.id.dialog_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        setContentView(content);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(null);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(true);
    }

    public int getTimeIndex() {
        return timeIndex;
    }

    public void setOnTimeChangedListener(TimePicker.OnTimeChangedListener listener) {
        picker.setOnTimeChangedListener(listener);
    }

    public void setTimeIndex(int timeIndex) {
        this.timeIndex = timeIndex;
    }
}
