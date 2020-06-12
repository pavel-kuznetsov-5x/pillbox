package com.cucumber007.pillbox.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.network.RequestManager;
import com.cucumber007.pillbox.views.ChechkableEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class LoginActivity extends AbstractPortraitActivity {
    //Activity for email/password login

    private Context context = this;

    @BindView(R.id.login) ChechkableEditText login;
    @BindView(R.id.login_checked) ImageView loginChecked;
    @BindView(R.id.password) ChechkableEditText password;
    @BindView(R.id.password_checked2) ImageView passwordChecked;
    @BindView(R.id.slide_menu_button) ImageButton slideMenuButton;
    @BindView(R.id.buy_button) Button buyButton;
    @BindView(R.id.animation_image) View animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        slideMenuButton.setVisibility(View.GONE);

        buyButton.setVisibility(View.VISIBLE);
        buyButton.setText("Log in");

        login.setCheckIndicator(loginChecked);
        login.setChecker(ChechkableEditText.EMAIL_CHECKER);

        password.setCheckIndicator(passwordChecked);
        password.setChecker(ChechkableEditText.PASSWORD_CHECKER);

    }

    //todo rename button
    @OnClick(R.id.buy_button)
    public void login() {
        showLoadAnimation();

        ModelManager.getInstance(this).getUserModel()
                .login(login.getText().toString(), password.getText().toString())
                .onErrorResumeNext(throwable -> {
                    hideLoadAnimation();
                    RequestManager.handleError(throwable);
                    return Observable.never();
                })
                .subscribe(userData -> {
                    Log.d("cutag", "" + "Login successful");
                    Intent intent = new Intent();
                    intent.putExtra("token", userData.getToken());
                    setResult(SocialLoginActivity.RESULT_LOGIN, intent);
                    finish();
                });

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


}
