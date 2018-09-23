package com.haoyang.lovelyreader.tre.util;

import android.util.Log;

/**
 * Created by xin on 18/9/22.
 */
public class LogUtil {

  private static final String TAG = "LogUtil";

  public static void d(String tag, String string) {
    Log.d(TAG + "-" + tag, string);
  }

  public static void printStackTrace(Exception e) {
    e.printStackTrace();
  }
}
