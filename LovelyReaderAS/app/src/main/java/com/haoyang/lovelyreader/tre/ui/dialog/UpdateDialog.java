package com.haoyang.lovelyreader.tre.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.mjiayou.trecorelib.base.TCDialog;
import com.mjiayou.trecorelib.common.Caches;
import com.mjiayou.trecorelib.dialog.TCAlertDialog;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.TextViewUtils;
import com.mjiayou.trecorelib.util.ViewUtils;

/**
 * Created by xin on 18/9/30.
 */

public class UpdateDialog extends TCDialog {

    // root
    private ViewGroup mViewRoot;
    // dialog
    private LinearLayout mLayoutDialog;
    // title
    private LinearLayout mLayoutTitle;
    private TextView mTvTitle;
    // message
    private LinearLayout mLayoutMessage;
    private TextView mTvMessage;
    // menu
    private LinearLayout mLayoutMenu;
    private LinearLayout mLayoutCancel;
    private TextView mTvCancel;
    private LinearLayout mLayoutOk;
    private TextView mTvOk;

    private String mTitleStr = "", mMessageStr = "", mOkStr = "", mCancelStr = "";
    private TCAlertDialog.OnTCActionListener mOnTCActionListener;

    /**
     * 构造函数
     */
    public UpdateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public UpdateDialog(Context context) {
        this(context, com.mjiayou.trecorelib.R.style.tc_dialog_theme_default);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);

        // findViewById
        // root
        mViewRoot = (ViewGroup) findViewById(com.mjiayou.trecorelib.R.id.view_root);
        // dialog
        mLayoutDialog = (LinearLayout) findViewById(com.mjiayou.trecorelib.R.id.layout_dialog);
        // title
        mLayoutTitle = (LinearLayout) findViewById(com.mjiayou.trecorelib.R.id.layout_title);
        mTvTitle = (TextView) findViewById(com.mjiayou.trecorelib.R.id.tv_title);
        // message
        mLayoutMessage = (LinearLayout) findViewById(com.mjiayou.trecorelib.R.id.layout_message);
        mTvMessage = (TextView) findViewById(com.mjiayou.trecorelib.R.id.tv_message);
        // menu
        mLayoutMenu = (LinearLayout) findViewById(com.mjiayou.trecorelib.R.id.layout_menu);
        mLayoutCancel = (LinearLayout) findViewById(com.mjiayou.trecorelib.R.id.layout_cancel);
        mTvCancel = (TextView) findViewById(com.mjiayou.trecorelib.R.id.tv_cancel);
        mLayoutOk = (LinearLayout) findViewById(com.mjiayou.trecorelib.R.id.layout_ok);
        mTvOk = (TextView) findViewById(com.mjiayou.trecorelib.R.id.tv_ok);

        // 设置窗口宽高
        if (mViewRoot != null) {
            int width = Caches.get().getScreenWidth(WIDTH_RATIO_DEFAULT);
            int height = WindowManager.LayoutParams.WRAP_CONTENT;
            ViewUtils.setWidthAndHeight(mViewRoot, width, height);
        }

        try {
            // title
            boolean titleVisible = !TextUtils.isEmpty(mTitleStr);
            ViewUtils.setVisibility(mLayoutTitle, titleVisible);
            TextViewUtils.setText(mTvTitle, mTitleStr);
            // message
            boolean messageVisible = !TextUtils.isEmpty(mMessageStr);
            ViewUtils.setVisibility(mLayoutMessage, messageVisible);
            TextViewUtils.setText(mTvMessage, mMessageStr);
            // menu
            boolean okVisible = !TextUtils.isEmpty(mOkStr);
            ViewUtils.setVisibility(mLayoutOk, okVisible);
            TextViewUtils.setText(mTvOk, mOkStr);
            ViewUtils.setOnClickListener(mTvOk, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mOnTCActionListener != null) {
                        mOnTCActionListener.onOkAction();
                    }
                }
            });
            boolean cancelVisible = !TextUtils.isEmpty(mCancelStr);
            ViewUtils.setVisibility(mLayoutCancel, cancelVisible);
            TextViewUtils.setText(mTvCancel, mCancelStr);
            ViewUtils.setOnClickListener(mTvCancel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mOnTCActionListener != null) {
                        mOnTCActionListener.onCancelAction();
                    }
                }
            });
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
        }
    }

    // ******************************** 自定义操作 ********************************

    /**
     * setTitle
     */
    public void setTitle(String title) {
        LogUtils.i(TAG, "setTitle -> " + title);
        this.mTitleStr = title;
    }

    /**
     * setMessage
     */
    public void setMessage(String message) {
        LogUtils.i(TAG, "setMessage -> " + message);
        this.mMessageStr = message;
    }

    /**
     * setOkMenu
     */
    public void setOkMenu(String okStr) {
        LogUtils.i(TAG, "setOkMenu -> " + okStr);
        this.mOkStr = okStr;
    }

    /**
     * setCancelMenu
     */
    public void setCancelMenu(String cancelStr) {
        LogUtils.i(TAG, "setCancleMenu -> " + cancelStr);
        this.mCancelStr = cancelStr;
    }

    /**
     * setTCActionListener
     */
    public void setTCActionListener(final TCAlertDialog.OnTCActionListener mOnTCActionListener) {
        this.mOnTCActionListener = mOnTCActionListener;
    }

    // ******************************** 接口 ********************************

    /**
     * OnDialogActionListener
     */
    public interface OnTCActionListener {
        void onOkAction();

        void onCancelAction();
    }
}
