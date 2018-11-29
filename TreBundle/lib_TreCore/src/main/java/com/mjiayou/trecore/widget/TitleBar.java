package com.mjiayou.trecore.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mjiayou.trecore.R;
import com.mjiayou.trecore.common.Caches;

/**
 * 自定义顶部导航控件
 * Created by treason on 2016/10/20.
 */
public class TitleBar extends RelativeLayout {

    protected final String TAG = TitleBar.class.getSimpleName();

    // var
    protected Context mContext;

    // 根布局
    protected LinearLayout mLayoutBar;
    // 内容
    protected RelativeLayout mLayoutBarContent;
    protected LinearLayout mLayoutLeftContainer; // 左菜单容器
    protected LinearLayout mLayoutRightContainer; // 右菜单容器
    protected LinearLayout mLayoutTitleContainer; // 标题容器
    protected TextView mTvTitle; // 标题
    // 分割线
    protected LinearLayout mLayoutBarLine;
    protected ImageView mIvBarLine; // 分割线

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context, attrs);
    }

    /**
     * 初始化组件
     */
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.tc_layout_toolbar, this);

        // findViewById
        // 根布局
        mLayoutBar = (LinearLayout) this.findViewById(R.id.layout_bar);
        // 内容
        mLayoutBarContent = (RelativeLayout) this.findViewById(R.id.layout_bar_content);
        mLayoutLeftContainer = (LinearLayout) this.findViewById(R.id.layout_left_container);
        mLayoutRightContainer = (LinearLayout) this.findViewById(R.id.layout_right_container);
        mLayoutTitleContainer = (LinearLayout) this.findViewById(R.id.layout_title_container);
        mTvTitle = (TextView) this.findViewById(R.id.tv_title);
        // 分割线
        mLayoutBarLine = (LinearLayout) this.findViewById(R.id.layout_bar_line);
        mIvBarLine = (ImageView) this.findViewById(R.id.iv_bar_line);

        // 初始化
        clearLeftContainer();
        clearRightContainer();
    }

    // ******************************** TitleBar-Start ********************************

    // **************** 显示隐藏操作 ****************

    /**
     * 显示隐藏 TitleBar
     *
     * @param show 显示或隐藏 TitleBar
     */
    public void setVisible(boolean show) {
        this.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // **************** 标题操作 ****************

    /**
     * 设置标题
     *
     * @param title 显示标题
     */
    public void setTitleOnly(String title) {
        if (mTvTitle != null) {
            if (!TextUtils.isEmpty(title)) {
                mTvTitle.setText(title);
            } else {
                mTvTitle.setText("");
            }
        }
        resizeTitleWidth();
    }

    /**
     * 设置标题 - 同时设置返回按钮
     *
     * @param title 显示标题
     */
    public void setTitle(String title) {
        setTitleOnly(title);
        addLeftImageView(R.drawable.tc_back, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).onBackPressed();
                }
            }
        });
    }

    // **************** 左菜单容器操作 ****************

    /**
     * 左菜单容器操作 - 移除所有子view
     */
    public void clearLeftContainer() {
        if (mLayoutLeftContainer != null) {
            mLayoutLeftContainer.removeAllViews();
        }
    }

    /**
     * 左菜单容器操作 - 是否隐藏
     */
    public void setLeftContainerVisible(boolean show) {
        if (mLayoutLeftContainer != null) {
            mLayoutLeftContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 左菜单容器操作 - 添加自定义view菜单
     *
     * @param view     自定义view菜单
     * @param listener 点击view执行的监听
     */
    public void addLeftView(View view, OnClickListener listener) {
        if (mLayoutLeftContainer != null) {
            addViewToLayout(mLayoutLeftContainer, view, listener);
        }
    }

    /**
     * 左菜单容器操作 - 添加TextView菜单
     *
     * @param text     TextView显示的文本
     * @param listener 点击view执行的监听
     */
    public void addLeftTextView(String text, OnClickListener listener) {
        if (mLayoutLeftContainer != null) {
            TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tc_layout_toolbar_menu_text, mLayoutLeftContainer, false);
            textView.setText(text);
            addViewToLayout(mLayoutLeftContainer, textView, listener);
        }
    }

    /**
     * 左菜单容器操作 - 添加ImageView菜单
     *
     * @param resId    ImageView显示的图片资源
     * @param listener 点击view执行的监听
     */
    public void addLeftImageView(int resId, OnClickListener listener) {
        if (mLayoutLeftContainer != null) {
            ImageView imageView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.tc_layout_toolbar_menu_image, mLayoutLeftContainer, false);
            imageView.setImageResource(resId);
            addViewToLayout(mLayoutLeftContainer, imageView, listener);
        }
    }

    // **************** 右菜单容器操作 ****************

    /**
     * 右菜单容器操作 - 移除所有子view
     */
    public void clearRightContainer() {
        if (mLayoutRightContainer != null) {
            mLayoutRightContainer.removeAllViews();
        }
    }

    /**
     * 右菜单容器操作 - 是否隐藏
     */
    public void setRightContainerVisible(boolean visible) {
        if (mLayoutRightContainer != null) {
            mLayoutRightContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 右菜单容器操作 - 添加自定义view菜单
     *
     * @param view     自定义view菜单
     * @param listener 点击view执行的监听
     */
    public void addRightView(View view, OnClickListener listener) {
        if (mLayoutRightContainer != null) {
            addViewToLayout(mLayoutRightContainer, view, listener);
        }
    }

    /**
     * 右菜单容器操作 - 添加TextView菜单
     *
     * @param text     TextView显示的文本
     * @param listener 点击view执行的监听
     */
    public void addRightTextView(String text, OnClickListener listener) {
        if (mLayoutRightContainer != null) {
            TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tc_layout_toolbar_menu_text, mLayoutRightContainer, false);
            textView.setText(text);
            addViewToLayout(mLayoutRightContainer, textView, listener);
        }
    }

    /**
     * 右菜单容器操作 - 添加ImageView菜单
     *
     * @param resId    ImageView显示的图片资源
     * @param listener 点击view执行的监听
     */
    public void addRightImageView(int resId, OnClickListener listener) {
        if (mLayoutRightContainer != null) {
            ImageView imageView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.tc_layout_toolbar_menu_image, mLayoutRightContainer, false);
            imageView.setImageResource(resId);
            addViewToLayout(mLayoutRightContainer, imageView, listener);
        }
    }

    // **************** 封装 ****************

    /**
     * 子View添加到父Layout中
     *
     * @param parentLayout 父Layout
     * @param childView    子View
     * @param listener     点击childView执行的监听
     */
    public void addViewToLayout(LinearLayout parentLayout, View childView, final OnClickListener listener) {
        parentLayout.setVisibility(View.VISIBLE);
        childView.setVisibility(View.VISIBLE);
        parentLayout.addView(childView);
        resizeTitleWidth();

        if (listener != null) {
            childView.setOnClickListener(listener);
        }
    }

    // **************** 工具 ****************

    /**
     * 计算宽度，防止覆盖
     */
    public void resizeTitleWidth() {
        int leftWidth = mLayoutLeftContainer.getWidth();
        int rightWidth = mLayoutRightContainer.getWidth();
        mTvTitle.setMaxWidth(Caches.get().getScreenWidth() - leftWidth - rightWidth);
    }

    // ******************************** TitleBar-End ********************************
}
