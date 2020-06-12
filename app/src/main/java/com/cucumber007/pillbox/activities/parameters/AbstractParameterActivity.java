package com.cucumber007.pillbox.activities.parameters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.AbstractPortraitActivity;
import com.cucumber007.pillbox.objects.pills.parameters.AbstractMedParameter;
import com.cucumber007.pillbox.utils.SpinnerStepper;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class AbstractParameterActivity extends AbstractPortraitActivity {
    //Base activity, used to edit pills parameters

    protected Context context = this;

    public static final int DEFAULT_MAX_VALUE = 24;
    public static final int DEFAULT_MIN_VALUE = 1;
    @BindView(R.id.parameter_name) TextView parameterName;
    @BindView(R.id.parameter_root) LinearLayout parameterRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        ButterKnife.bind(this);
        parameterName.setText(getName().toUpperCase());
        addItems(getRoot());
    }

    protected abstract void addItems(ViewGroup root);

    protected ViewGroup getRoot() {
        return parameterRoot;
    }

    abstract String getName();

    ////////////////////////////////

    protected Dialog createOtherDialog(final AbstractMedParameter parameter) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);

        final View content = getLayoutInflater().inflate(R.layout.parameter_dialog_other, null, false);
        final NumberPicker valuePicker = (NumberPicker) content.findViewById(R.id.valuePicker);
        final NumberPicker unitPicker = (NumberPicker) content.findViewById(R.id.unitPicker);

        final SpinnerStepper stepper = new SpinnerStepper(
                parameter.getUnitValues()[0].getMinValue(),
                parameter.getUnitValues()[0].getMaxValue());
        valuePicker.setDisplayedValues(stepper.getValuesArray());
        valuePicker.setMinValue(0);
        valuePicker.setMaxValue(stepper.getValuesArray().length-1);
        //valuePicker.setWrapSelectorWheel(false);
        valuePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        unitPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                AbstractMedParameter.Unit unit = parameter.getUnitValues()[newVal];
                stepper.init(unit.getMinValue(), unit.getMaxValue());
                valuePicker.setMinValue(0);
                valuePicker.setMaxValue(0);
                valuePicker.setDisplayedValues(stepper.getValuesArray());
                valuePicker.setMaxValue(stepper.getValuesArray().length - 1);
            }
        });
        unitPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        unitPicker.setDisplayedValues(parameter.getValues());
        unitPicker.setMinValue(0);
        unitPicker.setMaxValue(parameter.getUnit().getValues().length - 1);
        unitPicker.setWrapSelectorWheel(false);


        content.findViewById(R.id.dialog_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("value", stepper.getRealValue(valuePicker.getValue()));
                intent.putExtra("unit", unitPicker.getValue());
                intent.putExtra("tag", parameter.getTag());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        dialog.setContentView(content);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(null);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    protected Dialog createCompositeDialog(final AbstractMedParameter parameter, final int unitId) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);

        final View content = getLayoutInflater().inflate(R.layout.parameter_dialog_composite, null, false);
        final NumberPicker valuePicker = (NumberPicker) content.findViewById(R.id.valuePicker);

        valuePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final SpinnerStepper stepper = new SpinnerStepper(
                parameter.getUnitValues()[0].getMinValue(),
                parameter.getUnitValues()[0].getMaxValue());
        valuePicker.setDisplayedValues(stepper.getValuesArray());
        valuePicker.setMinValue(0);
        valuePicker.setMaxValue(stepper.getValuesArray().length - 1);
        //valuePicker.setWrapSelectorWheel(false);

        final String unitName = parameter.getUnit().getValues()[unitId];
        ((TextView) content.findViewById(R.id.unitName)).setText(unitName);

        content.findViewById(R.id.dialog_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("value", stepper.getRealValue(valuePicker.getValue()));
                intent.putExtra("unit", unitId);
                intent.putExtra("tag", parameter.getTag());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        dialog.setContentView(content);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(null);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    ///////////////////////////////

    protected View createParameterItemView(final AbstractMedParameter parameter) {
        return createParameterItemView(parameter, parameter.getTag());

    }

    protected View createParameterItemView(final AbstractMedParameter parameter, String title) {
        View view = getLayoutInflater().inflate(R.layout.parameter_item, null, false);

        ((TextView) view.findViewById(R.id.parameter_item_name)).setText(title);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("value", parameter.getValue());
                intent.putExtra("unit", parameter.getUnit().getId());
                intent.putExtra("tag", parameter.getTag());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        return view;
    }

    protected View createCompositeParameterItemView(final AbstractMedParameter parameter) {
        return createCompositeParameterItemView(parameter, parameter.generateTag(0, parameter.getUnit()));
    }

    protected View createCompositeParameterItemView(final AbstractMedParameter parameter, String title) {

        final Dialog dialog = createCompositeDialog(parameter, parameter.getUnit().getId());

        View view = getLayoutInflater().inflate(R.layout.parameter_composite_item, null, false);
        final TextView parameterName = ((TextView) view.findViewById(R.id.parameter_composite_item_name));

        parameterName.setText(title);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        return view;
    }

    protected View createOtherParameterItemView(final AbstractMedParameter parameter) {

        final Dialog dialog = createOtherDialog(parameter);

        View view = getLayoutInflater().inflate(R.layout.parameter_composite_item, null, false);
        final TextView parameterName = ((TextView) view.findViewById(R.id.parameter_composite_item_name));

        parameterName.setText("Other");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        return view;
    }

    protected View createDivider(String title) {
        View view = getLayoutInflater().inflate(R.layout.parameter_divider, null, false);
        ((TextView) view.findViewById(R.id.parameter_divider_title)).setText(title);
        return view;
    }

    public static String wrapInt(int number) {
        if (number < 10) return "0" + number;
        else return "" + number;
    }
}
