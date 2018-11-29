package com.mjiayou.trecore.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mjiayou.trecore.R;
import com.mjiayou.trecore.base.TCDialog;
import com.mjiayou.trecore.common.Caches;
import com.mjiayou.trecore.util.ViewUtil;
import com.mjiayou.trecorelib.util.LogUtil;
import com.mjiayou.trecorelib.util.TextViewUtil;

/**
 * Created by treason on 16/5/27.
 */
public class TCProgressDialog extends TCDialog {

    private static final String TAG = "TCProgressDialog";

    private static TCProgressDialog mTCProgressDialog;

    // root
    private ViewGroup mViewRoot;
    // dialog
    private LinearLayout mLayoutDialog;
    // progressbar
    private ProgressBar mProgressBar;
    // message
    private TextView mTvMessage;

    private String mMessageStr = "";

    public TCProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public TCProgressDialog(Context context) {
        this(context, R.style.tc_dialog_theme_default);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tc_dialog_progress);

        // findViewById
        // root
        mViewRoot = (ViewGroup) findViewById(R.id.view_root);
        // dialog
        mLayoutDialog = (LinearLayout) findViewById(R.id.layout_dialog);
        // progressbar
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        // message
        mTvMessage = (TextView) findViewById(R.id.tv_message);

        // 设置窗口宽高
        if (null != mViewRoot) {
            int width = Caches.get().getScreenWidth(WIDTH_RATIO_DEFAULT);
            int height = WindowManager.LayoutParams.WRAP_CONTENT;
            ViewUtil.setWidthAndHeight(mViewRoot, width, height);
        }

        try {
            // message
            updateMessage(mMessageStr);
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }
    }

    // ******************************** 自定义操作 ********************************

    /**
     * setMessage
     */
    public void setMessage(String message) {
        LogUtil.i(TAG, "setMessage -> " + message);
        mMessageStr = message;
    }

    /**
     * updateMessage
     */
    public void updateMessage(String message) {
        LogUtil.i(TAG, "updateMessage -> " + message);
        boolean messageVisible = !TextUtils.isEmpty(message);
        ViewUtil.setVisibility(mTvMessage, messageVisible);
        TextViewUtil.setText(mTvMessage, message);
    }

    // ******************************** TCProgressDialog ********************************

    /**
     * 创建 TCProgressDialog
     */
    public static TCProgressDialog createDialog(Context context) {
        boolean needCreate = false;

        // 当 mTCProgressDialog 为 null 时，需要重新创建
        if (null == mTCProgressDialog) {
            needCreate = true;
        }

        // 当 mTCProgressDialog 不为 null，但是 getContext 不是 context 时
        if (null != mTCProgressDialog && context != mTCProgressDialog.getContext()) {
            LogUtil.w(TAG, "此处引发窗体泄露 | origin context -> " + mTCProgressDialog.getContext() + " | now context -> " + context);
            dismissDialog();
            needCreate = true;
        }

        // 根据需要重新创建
        if (needCreate) {
            mTCProgressDialog = new TCProgressDialog(context);
            mTCProgressDialog.setCancelable(true);
            mTCProgressDialog.setCanceledOnTouchOutside(false);
            mTCProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        }

        return mTCProgressDialog;
    }

    /**
     * 释放 TCProgressDialog
     */
    public static void dismissDialog() {
        if (null == mTCProgressDialog) {
            return;
        }

        if (mTCProgressDialog.isShowing()) {
            try {
                mTCProgressDialog.dismiss();
                mTCProgressDialog = null;
            } catch (Exception e) {
                LogUtil.printStackTrace(e);
            }
        }
    }

    /**
     * 更新 TCProgressDialog
     */
    public static void updateDialog(String message) {
        if (null != mTCProgressDialog && mTCProgressDialog.isShowing() && !TextUtils.isEmpty(message)) {
            mTCProgressDialog.updateMessage(message);
        }
    }
}