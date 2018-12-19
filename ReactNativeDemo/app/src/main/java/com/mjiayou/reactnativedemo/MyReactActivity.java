package com.mjiayou.reactnativedemo;

import android.support.annotation.Nullable;

import com.facebook.react.ReactActivity;

/**
 * Created by xin on 2018/12/19.
 */

public class MyReactActivity extends ReactActivity {

    @Nullable
    @Override
    protected String getMainComponentName() {
        return "HelloWorld"; // 这个在Registry.registerComponent注册
    }
}
