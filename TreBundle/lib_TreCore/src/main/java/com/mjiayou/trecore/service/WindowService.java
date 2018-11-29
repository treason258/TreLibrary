package com.mjiayou.trecore.service;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mjiayou.trecore.R;
import com.mjiayou.trecore.base.TCService;
import com.mjiayou.trecore.util.ServiceUtil;
import com.mjiayou.trecorelib.util.LogUtil;
import com.mjiayou.trecorelib.util.ToastUtil;

/**
 * Created by treason on 2017/2/7.
 */
public class WindowService extends TCService {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private View mViewWindow;
    private TextView mTvShow;

    private int mStartX;
    private int mStartY;
    private int mEndX;
    private int mEndY;

    @Override
    public void onCreate() {
        super.onCreate();

        initView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewWindow != null) {
            // 移除悬浮窗口
            LogUtil.i(TAG, "removeView");
            mWindowManager.removeView(mViewWindow);
        }
    }

    private void initView() {
        // initWindowParams
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE; // 更多type：https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#TYPE_PHONE
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 更多falgs:https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#FLAG_NOT_FOCUSABLE
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // initView
        mViewWindow = LayoutInflater.from(getApplication()).inflate(R.layout.tc_layout_window, null);
        mTvShow = (TextView) mViewWindow.findViewById(R.id.tv_show);
        mViewWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ServiceUtil.isAppRunningBackground(WindowService.this)) {
                    ToastUtil.show("点击");
                }
            }
        });

        // addViewToWindow
        mWindowManager.addView(mViewWindow, mLayoutParams);

        // initClick
        mViewWindow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartX = (int) event.getRawX();
                        mStartY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mEndX = (int) event.getRawX();
                        mEndY = (int) event.getRawY();
                        if (needIntercept()) {
                            // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                            mLayoutParams.x = (int) event.getRawX() - mViewWindow.getMeasuredWidth() / 2;
                            mLayoutParams.y = (int) event.getRawY() - mViewWindow.getMeasuredHeight() / 2;
                            mWindowManager.updateViewLayout(mViewWindow, mLayoutParams);
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (needIntercept()) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 是否拦截
     *
     * @return true-拦截;false-不拦截.
     */
    private boolean needIntercept() {
        if (Math.abs(mStartX - mEndX) > 30 || Math.abs(mStartY - mEndY) > 30) {
            return true;
        }
        return false;
    }
}
