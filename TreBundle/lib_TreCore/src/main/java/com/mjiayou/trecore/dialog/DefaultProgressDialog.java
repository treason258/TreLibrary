package com.mjiayou.trecore.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import com.mjiayou.trecorelib.util.LogUtil;

/**
 * Created by treason on 16/5/28.
 */
public class DefaultProgressDialog {

    protected static final String TAG = "DefaultProgressDialog";
    private static ProgressDialog mProgressDialog;

    // ******************************** DefaultProgressDialog ********************************

    /**
     * 创建 DefaultProgressDialog
     */
    public static ProgressDialog createDialog(Context context) {
        boolean needCreate = false;

        // 当 mProgressDialog 为 null 时，需要重新创建
        if (null == mProgressDialog) {
            needCreate = true;
        }

        // 当 mProgressDialog 不为 null，但是 getContext 不是 context 时
        if (null != mProgressDialog && context != mProgressDialog.getContext()) {
            LogUtil.w(TAG, "此处引发窗体泄露 | origin context -> " + mProgressDialog.getContext() + " | now context -> " + context);
            dismissDialog();
            needCreate = true;
        }

        // 根据需要重新创建
        if (needCreate) {
            mProgressDialog = DialogHelper.createProgressDialog(context, null, "请稍候...", true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        }

        return mProgressDialog;
    }

    /**
     * 释放 DefaultProgressDialog
     */
    public static void dismissDialog() {
        if (null == mProgressDialog) {
            return;
        }

        if (mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            } catch (Exception e) {
                LogUtil.printStackTrace(e);
            }
        }
    }

    /**
     * 更新 DefaultProgressDialog
     */
    public static void updateDialog(String message) {
        if (null != mProgressDialog && mProgressDialog.isShowing() && !TextUtils.isEmpty(message)) {
            mProgressDialog.setMessage(message);
        }
    }
}