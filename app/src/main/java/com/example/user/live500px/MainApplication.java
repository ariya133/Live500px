package com.example.user.live500px;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize
        Contextor.getInstance().init(getApplicationContext());

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
