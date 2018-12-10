package com.haoyang.lovelyreader.tre;

import android.os.Build;

import com.haoyang.lovelyreader.BuildConfig;
import com.haoyang.reader.page.ReaderApplication;
import com.mjiayou.trecorelib.base.TCApp;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by xin on 18/9/22.
 */
public class MyApplication extends ReaderApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        TCApp.onCreateManual(this);

        CrashReport.initCrashReport(getApplicationContext(), "7c60c5e9ac", BuildConfig.DEBUG);
    }
}
