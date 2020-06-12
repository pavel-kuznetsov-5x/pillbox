package com.cucumber007.pillbox.models;

import com.cucumber007.pillbox.network.RequestManager;
import com.cucumber007.pillbox.network.UserData;
import com.cucumber007.pillbox.network.UserSocialLoginData;

import rx.Observable;


public class UserModel {

    public Observable<UserData> login(String login, String password) {
        /*return RequestManager.getInstance().getService()
                .login(new UserLoginData(login, password))
                .compose(RequestManager.applySchedulers())
                .map(response -> {
                    final String token = response.headers().get("Set-Cookie").split(";")[0];
                    UserData userData = response.body();
                    userData.setToken(token);
                    return response.body();
                });*/
        return Observable.just(new UserData(1, "", "fake_token"));
    }

    public Observable<UserData> loginSocial(String accessToken, int socialType) {
        return RequestManager.getInstance().getService()
                .loginSocial(new UserSocialLoginData(accessToken, socialType))
                .compose(RequestManager.applySchedulers())
                .map(response -> {
                    final String token = response.headers().get("Set-Cookie").split(";")[0];
                    UserData userData = response.body();
                    userData.setToken(token);
                    return response.body();
                });
    }

}
