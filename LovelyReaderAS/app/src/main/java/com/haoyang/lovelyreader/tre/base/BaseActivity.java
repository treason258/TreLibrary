package com.haoyang.lovelyreader.tre.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mjiayou.trecorelib.dialog.TCLoadingDialog;
import com.mjiayou.trecorelib.util.ToastUtils;

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

    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // var
        mActivity = this;
        mContext = this;

        mLoadingDialog = TCLoadingDialog.createDialog(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void initView() {
    }

    public void showLoading(boolean show) {
//        if (show) {
//            if (!mLoadingDialog.isShowing()) {
//                mLoadingDialog.show();
//            }
//        } else {
//            if (mLoadingDialog.isShowing()) {
//                mLoadingDialog.hide();
//            }
//        }
//        if (show) {
//            ToastUtils.show("loading start");
//        } else {
//            ToastUtils.show("loading stop");
//        }
    }
}
