package com.mjiayou.trecore.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
public class TCAlertDialog extends TCDialog {

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
    private OnTCActionListener mOnTCActionListener;

    /**
     * 构造函数
     */
    public TCAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public TCAlertDialog(Context context) {
        this(context, R.style.tc_dialog_theme_default);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tc_dialog_alert);

        // findViewById
        // root
        mViewRoot = (ViewGroup) findViewById(R.id.view_root);
        // dialog
        mLayoutDialog = (LinearLayout) findViewById(R.id.layout_dialog);
        // title
        mLayoutTitle = (LinearLayout) findViewById(R.id.layout_title);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        // message
        mLayoutMessage = (LinearLayout) findViewById(R.id.layout_message);
        mTvMessage = (TextView) findViewById(R.id.tv_message);
        // menu
        mLayoutMenu = (LinearLayout) findViewById(R.id.layout_menu);
        mLayoutCancel = (LinearLayout) findViewById(R.id.layout_cancel);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mLayoutOk = (LinearLayout) findViewById(R.id.layout_ok);
        mTvOk = (TextView) findViewById(R.id.tv_ok);

        // 设置窗口宽高
        if (mViewRoot != null) {
            int width = Caches.get().getScreenWidth(WIDTH_RATIO_DEFAULT);
            int height = WindowManager.LayoutParams.WRAP_CONTENT;
            ViewUtil.setWidthAndHeight(mViewRoot, width, height);
        }

        try {
            // title
            boolean titleVisible = !TextUtils.isEmpty(mTitleStr);
            ViewUtil.setVisibility(mLayoutTitle, titleVisible);
            TextViewUtil.setText(mTvTitle, mTitleStr);
            // message
            boolean messageVisible = !TextUtils.isEmpty(mMessageStr);
            ViewUtil.setVisibility(mLayoutMessage, messageVisible);
            TextViewUtil.setText(mTvMessage, mMessageStr);
            // menu
            boolean okVisible = !TextUtils.isEmpty(mOkStr);
            ViewUtil.setVisibility(mLayoutOk, okVisible);
            TextViewUtil.setText(mTvOk, mOkStr);
            ViewUtil.setOnClickListener(mTvOk, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mOnTCActionListener != null) {
                        mOnTCActionListener.onOkAction();
                    }
                }
            });
            boolean cancelVisible = !TextUtils.isEmpty(mCancelStr);
            ViewUtil.setVisibility(mLayoutCancel, cancelVisible);
            TextViewUtil.setText(mTvCancel, mCancelStr);
            ViewUtil.setOnClickListener(mTvCancel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mOnTCActionListener != null) {
                        mOnTCActionListener.onCancelAction();
                    }
                }
            });
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }
    }

    // ******************************** 自定义操作 ********************************

    /**
     * setTitle
     */
    public void setTitle(String title) {
        LogUtil.i(TAG, "setTitle -> " + title);
        this.mTitleStr = title;
    }

    /**
     * setMessage
     */
    public void setMessage(String message) {
        LogUtil.i(TAG, "setMessage -> " + message);
        this.mMessageStr = message;
    }

    /**
     * setOkMenu
     */
    public void setOkMenu(String okStr) {
        LogUtil.i(TAG, "setOkMenu -> " + okStr);
        this.mOkStr = okStr;
    }

    /**
     * setCancelMenu
     */
    public void setCancelMenu(String cancelStr) {
        LogUtil.i(TAG, "setCancleMenu -> " + cancelStr);
        this.mCancelStr = cancelStr;
    }

    /**
     * setTCActionListener
     */
    public void setTCActionListener(final OnTCActionListener mOnTCActionListener) {
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
