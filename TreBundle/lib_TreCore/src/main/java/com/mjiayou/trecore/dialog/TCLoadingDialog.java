package com.mjiayou.trecore.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
public class TCLoadingDialog extends TCDialog {

    protected static final String TAG = "TCLoadingDialog";

    private static TCLoadingDialog mTCLoadingDialog;

    // root
    private ViewGroup mViewRoot;
    // dialog
    private ImageView mIvBg;
    private ImageView mIvLoading;
    private TextView mTvMessage;

    private String mMessageStr = "";

    public TCLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public TCLoadingDialog(Context context) {
        this(context, R.style.tc_dialog_theme_default);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tc_dialog_loading);

        // findViewById
        // root
        mViewRoot = (ViewGroup) findViewById(R.id.view_root);
        // dialog
        mIvBg = (ImageView) findViewById(R.id.iv_bg);
        mIvLoading = (ImageView) findViewById(R.id.iv_loading);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        AnimationDrawable animationDrawable = (AnimationDrawable) mIvLoading.getBackground();
        animationDrawable.start();
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

    // ******************************** TCLoadingDialog ********************************

    /**
     * 创建 mTCLoadingDialog
     */
    public static TCLoadingDialog createDialog(Context context) {
        boolean needCreate = false;

        // 当 mTCLoadingDialog 为 null 时，需要重新创建
        if (null == mTCLoadingDialog) {
            needCreate = true;
        }

        // 当 mTCLoadingDialog 不为 null，但是 getContext 不是 context 时
        if (null != mTCLoadingDialog && context != mTCLoadingDialog.getContext()) {
            LogUtil.w(TAG, "此处引发窗体泄露 | origin context -> " + mTCLoadingDialog.getContext() + " | now context -> " + context);
            dismissDialog();
            needCreate = true;
        }

        // 根据需要重新创建
        if (needCreate) {
            mTCLoadingDialog = new TCLoadingDialog(context);
            mTCLoadingDialog.setCancelable(true);
            mTCLoadingDialog.setCanceledOnTouchOutside(false);
            mTCLoadingDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        }

        return mTCLoadingDialog;
    }

    /**
     * 释放 TCLoadingDialog
     */
    public static void dismissDialog() {
        if (null == mTCLoadingDialog) {
            return;
        }

        if (mTCLoadingDialog.isShowing()) {
            try {
                mTCLoadingDialog.dismiss();
                mTCLoadingDialog = null;
            } catch (Exception e) {
                LogUtil.printStackTrace(e);
            }
        }
    }

    /**
     * 更新 TCLoadingDialog
     */
    public static void updateDialog(String message) {
        if (null != mTCLoadingDialog && mTCLoadingDialog.isShowing() && !TextUtils.isEmpty(message)) {
            mTCLoadingDialog.updateMessage(message);
        }
    }
}