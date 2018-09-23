package com.haoyang.lovelyreader.tre;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.mjiayou.trecorelib.util.UserUtils;

/**
 * Created by xin on 18/9/22.
 */
public class SplashActivity extends BaseActivity {

    private TextView tvTitle;

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("闪屏页面");

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UserUtils.checkLoginStatus()) {
                    startActivity(new Intent(mContext, MainActivity.class));
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}
