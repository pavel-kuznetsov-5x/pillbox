package com.cucumber007.pillbox.activities.settings;

import android.text.Html;
import android.view.ViewGroup;

import com.cucumber007.pillbox.utils.PrivacyPolicy;

public class PrivacyPolicySettingActivity extends AbstractSettingActivity {

    @Override
    protected void addItems(ViewGroup root) {
        root.addView(createTextView(Html.fromHtml(PrivacyPolicy.text)));
    }

    @Override
    protected String getSettingName() {
        return "Privacy Policy";
    }
}
