package com.mjiayou.trebundle;

import android.os.Bundle;
import android.widget.ImageView;

import com.mjiayou.trecore.base.TCActivity;
import com.mjiayou.trecorelib.util.HandlerUtil;

import butterknife.InjectView;

public class SplashActivity extends TCActivity {

    @InjectView(R.id.iv_splash)
    ImageView mIvSplash;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void beforeOnCreate(Bundle savedInstanceState) {
        super.beforeOnCreate(savedInstanceState);

        // 设置主题
        // setTheme(R.style.TCTheme_NoTitleBar_NoActionBar_Fullscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏TitleBar
        getTitleBar().setVisible(false);

        // 延迟跳转到主页
        HandlerUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.open(mContext);
                finish();
            }
        }, 300);
    }
}
