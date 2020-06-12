package com.cucumber007.pillbox.views;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class ChechkableEditText extends EditText {

    private View checkIndicator;
    public static Typeface proximaTypeface;

    private Checker checker;
    private boolean checked = false;

    public static Checker EMAIL_CHECKER = new Checker() {
        @Override
        public boolean check(String text) {
            boolean res = android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
            return res;
        }
    };

    public static Checker PASSWORD_CHECKER = new Checker() {
        @Override
        public boolean check(String text) {
            return text.length() > 0  ;
        }
    };

    public static class RegisterChecker implements Checker {

        private ChechkableEditText partner1;
        private ChechkableEditText partner2;
        private Checker checker;

        public RegisterChecker(ChechkableEditText partner1, ChechkableEditText partner2, Checker checker) {
            this.partner1 = partner1;
            this.partner2 = partner2;
            this.checker = checker;
        }

        @Override
        public boolean check(String text) {
            boolean res = false;
            if(checker.check(text))
                if(partner1.getText().toString().equals(partner2.getText().toString()))
                    res = true;

            partner1.displayCheck(res);
            partner1.setChecked(res);
            partner2.displayCheck(res);
            partner2.setChecked(res);
            return res;
        }
    }

    public ChechkableEditText(Context context) {
        super(context);
        init();
    }

    public ChechkableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChechkableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    setText(result);
                    setSelection(result.length());
                }
                applyCheck(result);
            }
        });
        initFont();
    }

    private void initFont() {
        if(proximaTypeface == null) proximaTypeface = Typeface.createFromAsset(getContext().getAssets(), "ProximaNovaRegular.otf");
        setTypeface(proximaTypeface);
    }


    private void applyCheck(String text) {
        if (text != null && checker != null) {
            checked = checker.check(text);
        } else checked = false;

        displayCheck(checked);
    }

    public void displayCheck(boolean checked) {
        if (checkIndicator != null && checker != null) {
            if (checked)
                checkIndicator.setVisibility(VISIBLE);
            else checkIndicator.setVisibility(GONE);
        }
    }

    public void update() {
        applyCheck(getText().toString());
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
        applyCheck(getText().toString());
    }


    public void setCheckIndicator(View checkIndicator) {
        this.checkIndicator = checkIndicator;
        applyCheck(getText().toString());
    }

    public View getCheckIndicator() {
        return checkIndicator;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public interface Checker {
        boolean check(String text);
    }
}
