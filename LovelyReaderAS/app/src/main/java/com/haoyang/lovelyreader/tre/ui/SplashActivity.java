package com.haoyang.lovelyreader.tre.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;
import com.mjiayou.trecorelib.util.HandlerUtils;

/**
 * Created by xin on 18/9/22.
 */
public class SplashActivity extends BaseActivity {

    private Handler mHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public void initView() {
        super.initView();

        HandlerUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }
        }, 500);
    }
}
