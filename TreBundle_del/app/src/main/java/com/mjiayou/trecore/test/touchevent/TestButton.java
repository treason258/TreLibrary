package com.mjiayou.trecore.test.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.mjiayou.trecore.util.ViewUtil;

/**
 * Created by treason on 16/8/9.
 */
public class TestButton extends Button {

    private final String TAG = this.getClass().getSimpleName();

    public TestButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TestButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestButton(Context context) {
        this(context, null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        ViewUtil.printMotionEvent(event, TAG, "dispatchTouchEvent");

        return super.dispatchTouchEvent(event);
//        super.dispatchTouchEvent(event);
//        return true;
//        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewUtil.printMotionEvent(event, TAG, "onTouchEvent");

        return super.onTouchEvent(event);
//        super.onTouchEvent(event);
//        return true;
//        return false;
    }
}
