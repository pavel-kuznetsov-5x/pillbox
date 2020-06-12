package com.cucumber007.pillbox.network;

import android.widget.Toast;

import com.cucumber007.pillbox.activities.AbstractTokenActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class CookieCheckCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.raw().code() == 401) on401();
        else onSuccess(response);
    }

    public void onSessionOutDate(AbstractTokenActivity activity) {
        Toast.makeText(activity, "Server error. " + "Session out of date", Toast.LENGTH_SHORT).show();
        activity.logout();
        activity.checkToken();
    }

    public abstract void onSuccess(Response<T> response);
    public abstract void on401();
}
