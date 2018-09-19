package com.app.base.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.GridView;


/**
 * ListView (回弹效果)
 *
 */
public class BounceGridView extends GridView{
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 50;
    private static final float SCROLL_RATIO = 0.5f;// 阻尼系数  
    private Context mContext;
    private int mMaxYOverscrollDistance;
     
    public BounceGridView(Context context){
        super(context);
        mContext = context;
        initSpringbackListView();
    }
     
    public BounceGridView(Context context, AttributeSet attrs){
        super(context, attrs);
        mContext = context;
        initSpringbackListView();
    }
     
    public BounceGridView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        mContext = context;
        initSpringbackListView();
    }
     
    private void initSpringbackListView(){
        // 初始化，根据分辨率计算回弹的最大值
         
        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        final float density = metrics.density;
         
        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }
     
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent){ 
        //重新设置滚动的距离 
    	int newDeltaY = deltaY;  
        int delta = (int) (deltaY * SCROLL_RATIO);  
        if (delta != 0) newDeltaY = delta;  
//        System.out.println("-------newDeltaY="+newDeltaY+"-------scrollY="+scrollY+"-------scrollRangeY="+scrollRangeY);
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);  
//        return super.overScrollBy(deltaX, newDeltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);  
    }
    
}