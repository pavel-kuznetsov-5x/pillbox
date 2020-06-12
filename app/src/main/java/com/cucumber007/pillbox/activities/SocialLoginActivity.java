package com.cucumber007.pillbox.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.network.RequestManager;
import com.cucumber007.pillbox.network.UserSocialLoginData;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class SocialLoginActivity extends AbstractPortraitActivity {
    //Activity for login/register through social networks

    @BindView(R.id.slide_menu_button) ImageButton slideMenuButton;
    @BindView(R.id.buy_button) Button buyButton;
    @BindView(R.id.left_button) Button leftButton;
    @BindView(R.id.facebook_button) View facebookButton;
    @BindView(R.id.google_button) View googleButton;
    @BindView(R.id.animation_image) View animationView;

    private Context context = this;
    private boolean doubleBackToExitPressedOnce = false;
    private CallbackManager callbackManager;

    public static final int RESULT_LOGIN = 2;
    public static final int REQUEST_PERMISSION = 1005;
    static final int REQUEST_CODE_PICK_ACCOUNT = 1002;

    //workaround
    private String email;

    String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);
        ButterKnife.bind(this);
        //TODO why oncreate run multiple times?

        slideMenuButton.setVisibility(View.GONE);
        leftButton.setVisibility(View.VISIBLE);
        leftButton.setText("Register");

        buyButton.setVisibility(View.VISIBLE);
        buyButton.setText("Log in by email");

        callbackManager = CallbackManager.Factory.create();

        //todo handle facebook already logged in
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showLoadAnimation();

                ModelManager.getInstance(context).getUserModel()
                        .loginSocial(loginResult.getAccessToken().getToken(), UserSocialLoginData.TYPE_FACEBOOK)
                        .onErrorResumeNext(throwable -> {
                            hideLoadAnimation();
                            RequestManager.handleError(throwable);
                            return Observable.never();
                        })
                        .subscribe(userData -> {
                            Log.d("cutag", "" + "Login successful");
                            Intent intent = new Intent();
                            intent.putExtra("token", userData.getToken());
                            setResult(RESULT_OK, intent);
                            finish();
                        });

            }


            @Override
            public void onCancel() {
                Toast.makeText(context, "Facebook native app error. Try again.", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
            }


            @Override
            public void onError(FacebookException exception) {
                if (exception.getMessage().equals("CONNECTION_FAILURE: CONNECTION_FAILURE")) {
                    Toast.makeText(context, "Facebook login error. Please check your connection and try again", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "Facebook login error. Please check other option or try again later", Toast.LENGTH_SHORT).show();
                exception.printStackTrace();
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions((Activity) context, new ArrayList<String>());
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo handle errors
                String[] accountTypes = new String[]{"com.google"};
                Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                                                                     accountTypes, false, null, null, null, null);
                showLoadAnimation();
                startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
            }
        });

        /*twitterButton.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                Toast.makeText(getApplicationContext(), session.getUserId()+"  ", Toast.LENGTH_LONG).show();
            }


            @Override
            public void failure(TwitterException e) {
                Log.d("TwitterKit", "Login with Twitter failure", e);
            }


            @Override
            public void onResponse(Response<TwitterSession> response) {

            }


            @Override
            public void onFailure(Throwable t) {

            }
        });*/

    }

    @OnClick(R.id.buy_button)
    public void loginByEmail() {
        startActivityForResult(new Intent(this, LoginActivity.class), 0);
    }

    @OnClick(R.id.left_button)
    public void register() {
        startActivityForResult(new Intent(this, RegisterActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);

        if(resultCode == RESULT_LOGIN) {
            Intent intent = new Intent();
            intent.putExtra("token", data.getStringExtra("token"));
            setResult(RESULT_OK, intent);
            finish();
        }

        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                new GetUsernameTask(this, email, SCOPE).execute();
            } else if (resultCode == RESULT_CANCELED) {
                hideLoadAnimation();
            }
        }

        if (requestCode == REQUEST_PERMISSION) {
            if (resultCode == RESULT_OK) {
                new GetUsernameTask(this, email, SCOPE).execute();
            } else if (resultCode == RESULT_CANCELED) {
                hideLoadAnimation();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            setResult(AbstractTokenActivity.RESULT_EXIT);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

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

    public class GetUsernameTask extends AsyncTask<Void, Void, String> {
        Activity mActivity;
        String mScope;
        String mEmail;

        public static final String NOT_ALLOWED = "NOT_ALLOWED";

        GetUsernameTask(Activity activity, String name, String scope) {
            this.mActivity = activity;
            this.mScope = scope;
            this.mEmail = name;
        }


        @Override
        protected String doInBackground(Void... params) {
            try {
                String token = fetchToken();
                return token;
            } catch (IOException e) {

            }
            return null;
        }


        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);

            if (token != null) {
                if (!token.equals(NOT_ALLOWED)) {

                    ModelManager.getInstance(context).getUserModel()
                            .loginSocial(token, UserSocialLoginData.TYPE_GOOGLE)
                            .onErrorResumeNext(throwable -> {
                                hideLoadAnimation();
                                RequestManager.handleError(throwable);
                                return Observable.never();
                            })
                            .subscribe(userData -> {
                                Log.d("cutag", "" + "Login successful");
                                Intent intent = new Intent();
                                intent.putExtra("token", userData.getToken());
                                setResult(RESULT_OK, intent);
                                finish();
                            });

                } else {
                    Toast.makeText(context, "Google login error. Please try again later", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Google login error. Please check your connection and try again", Toast.LENGTH_SHORT).show();
            }

            hideLoadAnimation();
        }


    protected String fetchToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (UserRecoverableAuthException userRecoverableException) {
            startActivityForResult(userRecoverableException.getIntent(), REQUEST_PERMISSION);
        } catch (GoogleAuthException fatalException) {}
            return NOT_ALLOWED;

    }
}

}
