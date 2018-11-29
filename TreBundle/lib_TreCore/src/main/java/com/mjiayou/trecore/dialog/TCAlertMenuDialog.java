package com.mjiayou.trecore.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mjiayou.trecore.R;
import com.mjiayou.trecore.base.TCDialog;
import com.mjiayou.trecore.bean.entity.TCMenu;
import com.mjiayou.trecore.bean.entity.TCRect;
import com.mjiayou.trecore.common.Caches;
import com.mjiayou.trecore.util.ViewUtil;
import com.mjiayou.trecorelib.util.LogUtil;
import com.mjiayou.trecorelib.util.TextViewUtil;

import java.util.List;

/**
 * Created by treason on 16/6/27.
 */
public class TCAlertMenuDialog extends TCDialog {

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
    private LinearLayout mLayoutOptionContainer;

    private String mTitleStr = "", mMessageStr = "";
    private List<TCMenu> mTCMenus;

    /**
     * 构造函数
     */
    public TCAlertMenuDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public TCAlertMenuDialog(Context context) {
        this(context, R.style.tc_dialog_theme_default);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tc_dialog_alert_menu);

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
        mLayoutOptionContainer = (LinearLayout) findViewById(R.id.layout_option_container);

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
            if (null != mTCMenus) {
                // 移除
                clearOptionContainer();
                // 添加
                for (TCMenu tcMenu : mTCMenus) {
                    // option & cancel
                    addOptionTextView(tcMenu.getText(), tcMenu.getOnClickListener());
                }
            }
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }

        // 设置窗口最大高度
        if (null != mViewRoot) {
            TCRect rootRect = ViewUtil.getWidthAndHeight(mViewRoot);
            int maxHeight = Caches.get().getScreenHeight(WIDTH_RATIO_DEFAULT);
            if (rootRect.getHeight() > maxHeight) {
                ViewUtil.setWidthAndHeight(mViewRoot, ViewUtil.NONE_SIZE, maxHeight);
            }
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
     * setMenu
     */
    public void setMenu(List<TCMenu> tcMenus) {
        this.mTCMenus = tcMenus;
    }

    // ******************************** 封装 ********************************

    /**
     * View添加到Layout中
     */
    protected void addViewToLayout(LinearLayout parentLayout, View childView, final View.OnClickListener onClickListener) {
        parentLayout.setVisibility(View.VISIBLE);
        childView.setVisibility(View.VISIBLE);
        parentLayout.addView(childView);

        childView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
    }

    /**
     * Option 容器操作 - 清空
     */
    protected void clearOptionContainer() {
        if (null != mLayoutOptionContainer) {
            mLayoutOptionContainer.removeAllViews();
        }
    }

    /**
     * Option 容器操作 - 添加
     */
    protected void addOptionTextView(String text, View.OnClickListener onClickListener) {
        if (null != mLayoutOptionContainer) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.tc_layout_menu_item_left, mLayoutOptionContainer, false);
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
            tvMenu.setText(text);
            addViewToLayout(mLayoutOptionContainer, view, onClickListener);
        }
    }
}
