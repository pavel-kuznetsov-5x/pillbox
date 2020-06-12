package com.cucumber007.pillbox.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.cucumber007.pillbox.models.ModelManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

public abstract class AbstractTokenActivity extends AbstractPortraitActivity {
    //Activity that handles server side token (cookie)

    public static final int RESULT_EXIT = 1001;

    private static final String SHARED_PREFERENCES = "pillbox_prefs";
    private static int REQUEST_CODE_LOGIN = 1000;
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        checkToken();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkToken();
    }

    public String getToken() {
        if (token == null) {
            String sharedToken = this.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).getString("token", null);
            if (sharedToken == null) {
                return null;
            } else {
                token = sharedToken;
                return sharedToken;
            }
        } else return token;
    }

    public void removeToken() {
        token = null;
        writeToken(token);
    }

    public void checkToken() {
        if(getToken() == null) {
            Intent intent = new Intent(this, SocialLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_EXIT) finish();

        if(requestCode == REQUEST_CODE_LOGIN) {
            try {
                token = data.getStringExtra("token");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            writeToken(token);
        }
    }

    private void writeToken(String token) {
        SharedPreferences.Editor editor = this.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit();
        if (token == null) {
            editor.remove("token");
        } else editor.putString("token", token);
        editor.apply();
    }

    public void logout() {
        ModelManager.getInstance(this).getWaterModel().saveWaterLevel(0);
        removeToken();
        LoginManager.getInstance().logOut();
    }

}
