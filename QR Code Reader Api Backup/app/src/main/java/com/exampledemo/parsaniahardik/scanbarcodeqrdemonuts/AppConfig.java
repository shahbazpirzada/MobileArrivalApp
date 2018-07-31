package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.app.Application;


import com.orm.SugarContext;

/**
 * Created by Stech on 3/1/2018.
 */

public class AppConfig extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
