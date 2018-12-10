package com.haoyang.lovelyreader.tre.helper;

import android.content.Context;

import com.haoyang.lovelyreader.BuildConfig;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by treason on 2018/12/11.
 */

public class BuglyHelper {

    public static void init(Context context) {
        CrashReport.initCrashReport(context, "7c60c5e9ac", BuildConfig.DEBUG);
    }
}
