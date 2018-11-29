package com.mjiayou.trecore.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mjiayou.trecore.R;
import com.mjiayou.trecorelib.util.LogUtil;

/**
 * Created by treason on 16/5/27.
 */
public class TCDialog extends Dialog {

    // TAG
    protected final String TAG = this.getClass().getSimpleName();

    // 显示生命周期
    protected final String TAG_LIFE_CYCLE = "life_cycle_dialog";
    protected boolean SHOW_LIFE_CYCLE = true;

    protected final static double WIDTH_RATIO_DEFAULT = 0.85;
    protected final static double WIDTH_RATIO_BIG = 0.95;
    protected final static double WIDTH_RATIO_FULL = 1.0;
    protected final static double WIDTH_RATIO_HALF = 0.6;

    // var
    protected Context mContext;

    public TCDialog(Context context, int themeResId) {
        super(context, themeResId);
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onConstruct");
        }

        // var
        mContext = context;

        // 窗口显示位置 - 默认位置居中
        getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
    }

    public TCDialog(Context context) {
        this(context, R.style.tc_dialog_theme_default);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onCreate");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onStart");
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onStop");
        }
        super.onStop();
    }

    @Override
    public void dismiss() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | dismiss");
        }
        super.dismiss();
    }

    @Override
    public void onBackPressed() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onBackPressed");
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onKeyDown | keyCode -> " + keyCode + " | event -> " + event.toString());
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onTouchEvent | event -> " + event.toString());
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | dispatchTouchEvent | event -> " + ev.toString());
        }
        return super.dispatchTouchEvent(ev);
    }
}
