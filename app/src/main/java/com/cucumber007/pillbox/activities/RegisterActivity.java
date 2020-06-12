package com.cucumber007.pillbox.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.network.RequestManager;
import com.cucumber007.pillbox.network.UserData;
import com.cucumber007.pillbox.network.UserLoginData;
import com.cucumber007.pillbox.views.ChechkableEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AbstractPortraitActivity {


    @BindView(R.id.login) ChechkableEditText login;
    @BindView(R.id.login_checked) ImageView loginChecked;
    @BindView(R.id.password1) ChechkableEditText password1;
    @BindView(R.id.password_checked1) ImageView passwordChecked1;
    @BindView(R.id.password2) ChechkableEditText password2;
    @BindView(R.id.password_checked2) ImageView passwordChecked2;
    @BindView(R.id.slide_menu_button) ImageButton slideMenuButton;
    @BindView(R.id.buy_button) Button buyButton;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        slideMenuButton.setVisibility(View.GONE);

        buyButton.setVisibility(View.VISIBLE);
        buyButton.setText("Register");

        login.setCheckIndicator(loginChecked);
        login.setChecker(ChechkableEditText.EMAIL_CHECKER);

        ChechkableEditText.Checker regChecker = new ChechkableEditText.RegisterChecker(password1, password2,ChechkableEditText.PASSWORD_CHECKER);
        password1.setCheckIndicator(passwordChecked1);
        password1.setChecker(regChecker);

        password2.setCheckIndicator(passwordChecked2);
        password2.setChecker(regChecker);

    }

    @OnClick(R.id.buy_button)
    public void login() {
        if(password1.isChecked() && password2.isChecked()) {
            RequestManager.getInstance().getService()
                    .register(new UserLoginData(login.getText().toString(), password1.getText().toString()))
                    .enqueue(new Callback<UserData>() {

                        @Override
                        public void onResponse(Call<UserData> call, Response<UserData> response) {
                            if (response.errorBody() == null) {
                                Log.d("cutag", "" + "Register successful");
                                Intent intent = new Intent();
                                final String token = response.headers().get("Set-Cookie").split(";")[0];
                                intent.putExtra("token", token);
                                setResult(SocialLoginActivity.RESULT_LOGIN, intent);
                                finish();
                            } else {
                                String errorText = "";
                                try {
                                    errorText = new JSONObject(response.errorBody().string()).getString("detail");
                                    int s = 45;
                                } catch (IOException e) {
                                    errorText = "";
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    errorText = "";
                                    e.printStackTrace();
                                }
                                Log.d("cutag", "" + errorText);
                                Toast.makeText(context, "Register error. " + errorText, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserData> call, Throwable t) {
                            Log.d("cutag", "" + "Register error");
                            Toast.makeText(context, "Register error. Please check your connection and try again", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }

                    });
        } else {
            Toast.makeText(this, "Incorrect password or passwords isn't equal", Toast.LENGTH_SHORT).show();
        }
    }
}
