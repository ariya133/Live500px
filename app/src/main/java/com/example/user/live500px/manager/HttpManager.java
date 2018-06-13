package com.example.user.live500px.manager;

import android.content.Context;

import com.example.user.live500px.Contextor;
import com.example.user.live500px.manager.http.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class HttpManager {

    private static HttpManager instance;

    public static HttpManager getInstance() {
        if (instance == null)
            instance = new HttpManager();
        return instance;
    }

    private Context mContext;
    private ApiService service;

    private HttpManager() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-dd-MM'T'HH:mm:ssZ")
                .create();

        mContext = Contextor.getInstance().getContext();
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("https://nuuneoi.com/courses/500px/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service =retrofit.create(ApiService.class);
    }
    public ApiService getService(){
        return service;
    }

}
