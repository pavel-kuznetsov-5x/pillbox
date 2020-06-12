package com.cucumber007.pillbox.network;

import com.cucumber007.pillbox.objects.gym.Training;
import com.cucumber007.pillbox.objects.net.ShareTextWrapper;
import com.cucumber007.pillbox.objects.net.TrainingsWrapper;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface RequestService {

    static String prefix = "/api";

    @POST(prefix + "/register")
    Call<UserData> register(@Body UserLoginData userLoginData);

    @POST(prefix + "/login")
    Observable<Response<UserData>> login(@Body UserLoginData userLoginData);

    @POST(prefix + "/login")
    Observable<Response<UserData>> loginSocial(@Body UserSocialLoginData userLoginData);

    @GET(prefix + "/trainings")
    Call<TrainingsWrapper> getTrainings(
            @Header("Cookie") String cookie,
            @Query("scope") int scope,
            @Query("orderBy") int sort,
            @Query("fetchSize") int fetch,
            @Query("offset") int offset
    );

    @GET(prefix + "/trainings")
    Call<TrainingsWrapper> getTrainings(@Header("Cookie") String cookie, @Query("scope") int scope);

    @GET(prefix + "/trainings/purchased")
    Call<TrainingsWrapper> getPurchasedTrainings(@Header("Cookie") String cookie, @Query("scope") int scope);

    @GET(prefix + "/trainings/{id}")
    Call<Training> getTraining(@Header("Cookie") String cookie, @Path("id") int trainingId, @Query("scope") int scope);

    @POST(prefix + "/trainings/buy/{id}")
    Call<Training> buyTraining(@Header("Cookie") String cookie, @Path("id") int trainingId);

    @DELETE(prefix + "/trainings/cancel_purchases")
    Call<Object> removePurchases(@Header("Cookie") String cookie);

    @GET(prefix + "/text_for_share")
    Call<ShareTextWrapper> getShareText(@Header("Cookie") String cookie, @Query("scope") int scope);

    @GET
    Call<String> get(@Url String url);

}
