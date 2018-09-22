package com.haoyang.lovelyreader.tre;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by xin on 18/9/22.
 */

public class BaseFragment extends Fragment {

  // TAG
  protected final String TAG = this.getClass().getSimpleName();

  // var
  protected Activity mActivity;
  protected Context mContext;
  protected Intent mIntent;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    mActivity = activity;
    mContext = activity;
  }

  protected void initView() {
  }
}
