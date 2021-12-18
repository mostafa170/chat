package com.chat.network;

import android.app.Application;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DevartlinkAPI extends Application {

    private static DevartlinkAPI INSTANCE ;
    private static Retrofit retrofit = null;

    public static DevartlinkAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DevartlinkAPI();
        }
        return INSTANCE;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this ;
    }

    public static Retrofit getClient() {
        if (retrofit==null){
            HttpLoggingInterceptor logging =
                    new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override public void log(@NotNull String message) {
                            Log.e("retrofit",message);
                        }
                    });
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("https://devartlink.devartlab.com/api/")
                    .client(client)
                    .build();
        }
        return retrofit;
    }


    public static Api getApis (){
        return getClient().create(Api.class);
    }

}
