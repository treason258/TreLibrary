package com.haoyang.lovelyreader.tre;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xin on 18/9/22.
 */

public class ToastUtil {

  // TAG
  private static final String TAG = "ToastUtil";

  public static void show(Context context, String msg) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    LogUtil.d(TAG, msg);
  }
}
