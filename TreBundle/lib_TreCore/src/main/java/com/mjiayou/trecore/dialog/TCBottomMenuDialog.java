package com.mjiayou.trecore.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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
public class TCBottomMenuDialog extends TCDialog {

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
    private LinearLayout mLayoutCancelContainer;

    private String mTitleStr = "", mMessageStr = "";
    private List<TCMenu> mTCMenus;

    private LayoutType mLayoutType;

    /**
     * LayoutType
     */
    public enum LayoutType {
        DEFAULT(0, "default"),
        HOHO(1, "hoho");

        private int id;
        private String value;

        LayoutType(int id, String value) {
            this.id = id;
            this.value = value;
        }

        static LayoutType getLayoutType(int id) {
            for (LayoutType layoutType : values()) {
                if (layoutType.getId() == id) {
                    return layoutType;
                }
            }
            return DEFAULT;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 构造函数
     */
    public TCBottomMenuDialog(Context context, LayoutType layoutType, int themeResId) {
        super(context, themeResId);
        mLayoutType = layoutType;

        // 窗口显示位置
        getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        // 窗口显示动画
        getWindow().setWindowAnimations(R.style.tc_bottom_menu_animation);
    }

    public TCBottomMenuDialog(Context context, LayoutType layoutType) {
        this(context, layoutType, R.style.tc_dialog_theme_default);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (mLayoutType) {
            default:
            case DEFAULT:
                setContentView(R.layout.tc_dialog_bottom_menu);
                break;
        }

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
        mLayoutCancelContainer = (LinearLayout) findViewById(R.id.layout_cancel_container);

        // 设置窗口宽高
        if (mViewRoot != null) {
            int width = Caches.get().getScreenWidth(WIDTH_RATIO_BIG);
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
                clearCancelContainer();
                // 添加
                for (TCMenu tcMenu : mTCMenus) {
                    // option
                    if (tcMenu.getType() == TCMenu.TYPE_NORMAL) {
                        addOptionTextView(tcMenu.getText(), tcMenu.getOnClickListener());
                    }
                    // cancel
                    if (tcMenu.getType() == TCMenu.TYPE_CANCEL) {
                        addCancelTextView(tcMenu.getText(), tcMenu.getOnClickListener());
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }

        // 设置窗口最大高度
        if (mViewRoot != null) {
            TCRect rootRect = ViewUtil.getWidthAndHeight(mViewRoot);
            int maxHeight = Caches.get().getScreenHeight(WIDTH_RATIO_HALF);
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
     * 添加TextView
     */
    protected void addTextView(LinearLayout parentLayout, String text, View.OnClickListener onClickListener) {
        View view;
        switch (mLayoutType) {
            default:
            case DEFAULT:
                view = LayoutInflater.from(mContext).inflate(R.layout.tc_layout_menu_item_center, parentLayout, false);
                break;
        }
        TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
        tvMenu.setText(text);
        addViewToLayout(parentLayout, view, onClickListener);
    }

    /**
     * Option 容器操作 - 清空
     */
    protected void clearOptionContainer() {
        if (mLayoutOptionContainer != null) {
            mLayoutOptionContainer.removeAllViews();
        }
    }

    /**
     * Option 容器操作 - 添加
     */
    protected void addOptionTextView(String text, View.OnClickListener onClickListener) {
        if (mLayoutOptionContainer != null) {
            addTextView(mLayoutOptionContainer, text, onClickListener);
        }
    }

    /**
     * Cancel 容器操作 - 清空
     */
    protected void clearCancelContainer() {
        if (mLayoutCancelContainer != null) {
            mLayoutCancelContainer.removeAllViews();
        }
    }

    /**
     * Cancel 容器操作 - 添加
     */
    protected void addCancelTextView(String text, View.OnClickListener onClickListener) {
        if (mLayoutCancelContainer != null) {
            addTextView(mLayoutCancelContainer, text, onClickListener);
        }
    }
}
