package com.mjiayou.trecore.base;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            佛祖保佑       永无BUG
*/

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.mjiayou.trecore.common.Caches;
import com.mjiayou.trecore.helper.VolleyHelper;
import com.mjiayou.trecore.util.VersionUtil;
import com.mjiayou.trecorelib.helper.GsonHelper;
import com.mjiayou.trecorelib.helper.TCHelper;
import com.mjiayou.trecorelib.util.LogUtil;
import com.mjiayou.trecorelib.util.ProcessUtil;

/**
 * TCApp
 */
public class TCApp extends Application {

    // APP_NAME
    public final String APP_NAME = "trebundle";

    // TAG
    private static final String TAG = TCApp.class.getSimpleName();

    // 显示生命周期
    protected final String TAG_LIFE_CYCLE = "life_cycle_app";
    protected boolean SHOW_LIFE_CYCLE = true;

    // var
    private static TCApp mInstance;
    private Context mContext;

    /**
     * 获取Application对象
     */
    public static TCApp get() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onCreate");
        }
        super.onCreate();

        // var
        mInstance = this;
        mContext = getApplicationContext();

        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | process id -> " + ProcessUtil.getProcessId());
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | process name -> " + ProcessUtil.getProcessName(mContext));
        }

        initApp();
    }

    @Override
    public void onTerminate() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onTerminate");
        }
        super.onTerminate();
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
        }
        super.onTrimMemory(level);
    }

    /**
     * 初始化APP
     */
    public void initApp() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | initApp");
        }
        LogUtil.printInit(TAG);

        /**
         * 初始化 配置信息
         */
        Caches.get().init();
        VersionUtil.init();
        TCHelper.init(mContext, "trebundle");

        /**
         * 初始化 第三方库
         */
        VolleyHelper.init();
        GsonHelper.init();
        // SwissArmyKnifeUtil.init();
    }

    // ******************************** project ********************************
}
