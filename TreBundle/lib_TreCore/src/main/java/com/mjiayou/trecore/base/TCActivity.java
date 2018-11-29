package com.mjiayou.trecore.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mjiayou.trecore.R;
import com.mjiayou.trecore.dialog.TCLoadingDialog;
import com.mjiayou.trecore.manager.StatusViewManager;
import com.mjiayou.trecore.net.RequestAdapter;
import com.mjiayou.trecore.util.ConvertUtil;
import com.mjiayou.trecore.widget.TitleBar;
import com.mjiayou.trecorelib.bean.TCResponse;
import com.mjiayou.trecorelib.manager.ActivityManager;
import com.mjiayou.trecorelib.util.LogUtil;

import butterknife.ButterKnife;

/**
 * TCActivity
 */

public abstract class TCActivity extends AppCompatActivity implements RequestAdapter.DataRequest, RequestAdapter.DataResponse {

    // TAG
    protected final String TAG = this.getClass().getSimpleName();

    // 显示生命周期
    protected final String TAG_LIFE_CYCLE = "life_cycle_activity";
    protected boolean SHOW_LIFE_CYCLE = true;

    // var
    protected Activity mActivity;
    protected Context mContext;
    protected Intent mIntent;

    // view
    private LinearLayout mLayoutRoot;
    private TitleBar mTitleBar;
    private FrameLayout mLayoutContainer;

    // 正在加载
    private Dialog mLoadingDialog;
    // 页面状态管理
    private StatusViewManager mStatusViewManager;
    // 网络请求
    private RequestAdapter mRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // beforeOnCreate
        beforeOnCreate(savedInstanceState);

        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onCreate | savedInstanceState -> " + ConvertUtil.parseString(savedInstanceState));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tc_activity_base);

        // var
        mActivity = this;
        mContext = this;

        // findViewById
        mLayoutRoot = (LinearLayout) findViewById(R.id.layout_root);
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);
        mLayoutContainer = (FrameLayout) findViewById(R.id.layout_container);

        // 初始化用户自定义布局
        if (getLayoutId() == 0) {
            LogUtil.i(TAG, "getLayoutId() == 0");
        } else {
            mLayoutContainer.addView(getLayoutInflater().inflate(getLayoutId(), null));
        }
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, "getLayoutId() -> " + getLayoutId());
        }

        // 控件注解
        ButterKnife.inject(this);

        // Activity管理，压栈操作
        ActivityManager.get().addActivity(mActivity);

        // 通过程序改变屏幕显示的方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // afterOnCreate
        afterOnCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onRestart");
        }
        super.onRestart();
    }

    @Override
    protected void onStart() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onStart");
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onResume");
        }
        super.onResume();
        // 打印ActivityManager
        LogUtil.printActivityList(ActivityManager.get().getActivityList());
    }

    @Override
    protected void onPause() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onPause");
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onStop");
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onDestroy");
        }
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onPostCreate | savedInstanceState -> " + ConvertUtil.parseString(savedInstanceState));
        }
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onPostResume");
        }
        super.onPostResume();
    }

    @Override
    public void setContentView(int layoutResID) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | setContentView | layoutResID -> " + layoutResID);
        }
        super.setContentView(layoutResID);
    }

    @Nullable
    @Override
    public View findViewById(@IdRes int id) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | findViewById | id -> " + id);
        }
        return super.findViewById(id);
    }

    @Override
    public void finish() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | finish");
        }
        super.finish();
        // Activity管理，出栈操作
        ActivityManager.get().removeActivity(mActivity);
    }

    @Override
    public void startActivity(Intent intent) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | startActivity | intent -> " + intent.toString());
        }
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | startActivityForResult | intent -> " + intent.toString() + " | requestCode -> " + requestCode);
        }
        super.startActivityForResult(intent, requestCode);
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

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onTitleChanged | title —> " + title + " | color -> " + color);
        }
        super.onTitleChanged(title, color);
    }

    @Override
    public void setTheme(@StyleRes int resid) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | setTheme | resid -> " + resid);
        }
        super.setTheme(resid);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onNewIntent | intent -> " + intent.toString());
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onSaveInstanceState | outState -> " + ConvertUtil.parseString(outState));
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onRestoreInstanceState | savedInstanceState -> " + ConvertUtil.parseString(savedInstanceState));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onConfigurationChanged | newConfig -> " + newConfig.toString());
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onLowMemory");
        }
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onTrimMemory | level -> " + level);
            // 因为onTrimMemory() 是在API 14 里添加的，你可以在老版本里使用onLowMemory() 回调，大致跟TRIM_MEMORY_COMPLETE事件相同。
            switch (level) {
                case TRIM_MEMORY_RUNNING_MODERATE:
                    LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onTrimMemory -> 你的应用正在运行，并且不会被杀死，但设备已经处于低内存状态，并且开始杀死LRU缓存里的内存。");
                    break;
                case TRIM_MEMORY_RUNNING_LOW:
                    LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onTrimMemory -> 你的应用正在运行，并且不会被杀死，但设备处于内存更低的状态，所以你应该释放无用资源以提高系统性能（直接影响app性能）");
                    break;
                case TRIM_MEMORY_RUNNING_CRITICAL:
                    LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onTrimMemory -> 你的应用还在运行，但系统已经杀死了LRU缓存里的大多数进程，所以你应该在此时释放所有非关键的资源。如果系统无法回收足够的内存，它会清理掉所有LRU缓存，并且开始杀死之前优先保持的进程，像那些运行着service的。同时，当你的app进程当前被缓存，你可能会从onTrimMemory() 收到下面的几种level。");
                    break;
                case TRIM_MEMORY_BACKGROUND:
                    LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onTrimMemory -> 系统运行在低内存状态，并且你的进程已经接近LRU列表的顶端（即将被清理）。虽然你的app进程还没有很高的被杀死风险，系统可能已经清理LRU里的进程，你应该释放那些容易被恢复的资源，如此可以让你的进程留在缓存里，并且当用户回到app时快速恢复。");
                    break;
                case TRIM_MEMORY_MODERATE:
                    LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onTrimMemory -> 系统运行在低内存状态，你的进程在LRU列表中间附近。如果系统变得内存紧张，可能会导致你的进程被杀死。");
                    break;
                case TRIM_MEMORY_COMPLETE:
                    LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onTrimMemory -> 系统运行在低内存状态，如果系统没有恢复内存，你的进程是首先被杀死的进程之一。你应该释放所有不重要的资源来恢复你的app状态。");
                    break;
            }
        }
        super.onTrimMemory(level);
    }

    // ******************************** lifeCycle ********************************

    /**
     * 获取activity对应的layout资源文件
     */
    protected abstract int getLayoutId();


    /**
     * 在 onCreate 之前执行
     */
    protected void beforeOnCreate(Bundle savedInstanceState) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | beforeOnCreate | savedInstanceState -> " + ConvertUtil.parseString(savedInstanceState));
        }
    }

    /**
     * 在 onCreate 之后执行
     */
    protected void afterOnCreate(Bundle savedInstanceState) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | afterOnCreate | savedInstanceState -> " + ConvertUtil.parseString(savedInstanceState));
        }

    }

    // ******************************** RequestAdapter.DataRequest ********************************

    @Override
    public void initView() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | initView");
        }
    }

    @Override
    public void getData(int pageNumber) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | getData");
        }
    }

    @Override
    public void refreshData() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | refreshData");
        }
    }

    @Override
    public void loadMoreData() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | loadMoreData");
        }
    }

    @Override
    public void submitData() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | submitData");
        }
    }

    // ******************************** RequestAdapter.DataResponse ********************************

    @Override
    public void callback(Message msg) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | callback");
        }
    }

    @Override
    public void refreshView(TCResponse response) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | refreshView");
        }
    }

    // ******************************** showLoading ********************************

    /**
     * 显示、隐藏正在加载对话框
     */
    public void showLoading(boolean show) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | showLoading | show -> " + show);
        }
        try {
            // 需要显示时，如果页面已经finish，则return
            if (show && isFinishing()) {
                return;
            }

            // 需要显示时，如果正在显示，则return
            if (show && (mLoadingDialog != null && mLoadingDialog.isShowing())) {
                return;
            }

            // 需要隐藏时，如果mLoadingDialog不存在或没有在显示，则return
            if (!show && (mLoadingDialog == null || !mLoadingDialog.isShowing())) {
                return;
            }

            // 如果mLoadingDialog不存在，则创建
            if (mLoadingDialog == null) {
                mLoadingDialog = TCLoadingDialog.createDialog(mContext);
                mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (mRequestAdapter != null) {
                            mRequestAdapter.cancelAll();
                        }
                    }
                });
            }

            // 显示/隐藏
            if (show) {
                mLoadingDialog.show();
            } else {
                mLoadingDialog.dismiss();
            }
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }
    }

    /**
     * 更新信息
     */
    public void updateLoading(String message) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | updateLoading | message -> " + message);
        }
        if (null != mLoadingDialog && mLoadingDialog.isShowing() && !TextUtils.isEmpty(message)) {
            if (mLoadingDialog instanceof TCLoadingDialog) {
                ((TCLoadingDialog) mLoadingDialog).updateMessage(message);
            }
        }
        LogUtil.i(TAG, "updateLoading -> " + message);
    }

    // ******************************** getTitleBar ********************************

    /**
     * 返回 TitleBar 对象
     */
    public TitleBar getTitleBar() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | getTitleBar");
        }
        return mTitleBar;
    }

    // ******************************** getStatusViewManager ********************************

    /**
     * 返回 StatusViewManager 对象
     */
    public StatusViewManager getStatusViewManager() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | getStatusViewManager");
        }
        if (mStatusViewManager == null) {
            mStatusViewManager = new StatusViewManager(mLayoutContainer, getLayoutInflater());
        }
        return mStatusViewManager;
    }

    // ******************************** getRequestAdapter ********************************

    /**
     * 返回 RequestAdapter 对象
     */
    public RequestAdapter getRequestAdapter() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | getRequestAdapter");
        }
        if (mRequestAdapter == null) {
            mRequestAdapter = new RequestAdapter(mContext, this);
        }
        return mRequestAdapter;
    }

    // ******************************** project ********************************
}
