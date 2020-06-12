package com.cucumber007.pillbox.network;

import android.location.Location;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RequestManager {

    private static RequestManager instance;
    private final RequestService service;

    private static final String serverAddress = "http://invalid_domain_11225852232238.com";

    private RequestManager() {
        //todo static creation?
        /*OkHttpClient client = new OkHttpClient();CustomCookieManager manager = new CustomCookieManager();
        client.setCookieHandler(manager);
        client.interceptors().add(new CookieInterceptor());*/

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.interceptors().add(HttpLogUtil.getHttpInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(RequestService.class);
    }

    public static RequestManager getInstance() {
        if (instance == null) {
            instance = new RequestManager();
        }
        return instance;
    }

    public RequestService getService() {
        return service;
    }

    public void requestWeather(Location location, String apiKey, RequestCallbackExecutable callback) {
        new GetRequest("http://api.openweathermap.org/data/2.5/weather?" +
                "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() +
                "&APPID=" + apiKey, callback).perform();
    }

    private class GetRequest {
        private String url;
        private RequestCallbackExecutable callback;

        public GetRequest(String url, RequestCallbackExecutable callback) {
            this.url = url;
            this.callback = callback;
        }

        public void perform() {
            RequestManager.getInstance().getService().get(url).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    callback.execute(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    callback.execute("Error: "+t.getMessage());
                }

            });
        }
    }

    public interface RequestCallbackExecutable {
        void execute(String parameter);
    }

    public static  <T> Observable.Transformer<T, T> applySchedulersAndHandleErrors() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext((throwable -> {
                    handleError(throwable);
                    return Observable.never();
                }));
    }

    public static  <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void handleError(Throwable t) {
        if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
//            try {
                switch (httpException.code()) {
                    case 401:
                        //UserModel.getInstance().logout();
                        break;
                    default:
//                        LogUtil.logDebug("SERVER: " + httpException.message() + " // "+t.getMessage() + " " + httpException.response().errorBody().string());
//                        LogUtil.makeToastWithDebug("Unknown server error", "SERVER: " + httpException.message() + " // "+t.getMessage() + " " + httpException.response().errorBody().string());
                        break;
                }

//            } catch (IOException e) {
//                LogUtil.logDebug("SERVER: " + "unknown");
//                e.printStackTrace();
//            }
            return;
        }
        if (isNetworkError(t)) {
//            LogUtil.makeToast("No internet connection");
//            LogUtil.logDebug("No internet connection");
            return;
        }
//        LogUtil.logDebug(t.getClass()+" "+t.getMessage());
    }

    public static boolean isNetworkError(Throwable t) {
        return t instanceof SocketTimeoutException || t instanceof UnknownHostException || t instanceof ConnectException || (t instanceof RuntimeException && t.getMessage().contains("Looper.prepare()")) ;
    }

    /*public class CookieInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request newRequest;

            newRequest = request.newBuilder()
                    .addHeader("Cookie", "test")
                    .build();
            return chain.proceed(newRequest);
        }
    }*/

    /*public class CustomCookieManager extends CookieManager {

        public static final String SET_COOKIE_KEY = "Set-Cookie";
        private final String SESSION_KEY = "session-key";

        public CustomCookieManager() {
            super.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        }

        @Override
        public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {

            super.put(uri, responseHeaders);

            if (responseHeaders == null || responseHeaders.get(SET_COOKIE_KEY) == null) {
                return;
            }

            for (String possibleSessionCookieValues : responseHeaders.get(SET_COOKIE_KEY)) {

                if (possibleSessionCookieValues != null) {

                    for (String possibleSessionCookie : possibleSessionCookieValues.split(";")) {

                        if (possibleSessionCookie.startsWith(SESSION_KEY) && possibleSessionCookie.contains("=")) {

                            String session = possibleSessionCookie.split("=")[1];
                            AbstractTokenActivity
                            return;
                        }
                    }
                }
            }
        }
    }*/
}
