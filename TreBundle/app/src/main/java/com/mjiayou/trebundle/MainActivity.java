package com.mjiayou.trebundle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.mjiayou.trebundle.debug.DebugActivity;
import com.mjiayou.trecore.base.TCActivity;
import com.mjiayou.trecore.base.TCApp;
import com.mjiayou.trecore.helper.UmengHelper;
import com.mjiayou.trecorelib.manager.ActivityManager;
import com.mjiayou.trecorelib.manager.CrashHandler;

public class MainActivity extends TCActivity {

    /**
     * startActivity
     */
    public static void open(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化异常捕获
        CrashHandler.get().init(mActivity, new CrashHandler.OnCaughtExceptionListener() {
            @Override
            public boolean onCaughtException(Thread thread, Throwable throwable) {
                UmengHelper.reportError(TCApp.get(), throwable);
                return false;
            }
        });

        // mTitleBar
        getTitleBar().setTitleOnly(getString(R.string.app_name));
        getTitleBar().addRightTextView("Debug", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DebugActivity.open(mContext);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ActivityManager.get().pressAgainToExit(mActivity, keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
