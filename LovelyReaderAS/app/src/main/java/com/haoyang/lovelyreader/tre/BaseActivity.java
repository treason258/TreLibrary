package com.haoyang.lovelyreader.tre;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by xin on 18/9/22.
 */

public class BaseActivity extends AppCompatActivity {

  // TAG
  protected final String TAG = this.getClass().getSimpleName();

  // var
  protected Activity mActivity;
  protected Context mContext;
  protected Intent mIntent;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // var
    mActivity = this;
    mContext = this;
  }

  @Override protected void onResume() {
    super.onResume();
  }

  @Override protected void onPause() {
    super.onPause();
  }

  protected void initView() {
  }
}
