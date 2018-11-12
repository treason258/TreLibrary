package com.mjiayou.routerdemo;

import android.app.Application;

import com.mjiayou.routerdemo.router.Router;

/**
 * Created by treason on 2017/11/21.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    public static MyApplication get() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Router.get().init();
    }
}
