package com.mjiayou.trecore.helper;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.mjiayou.trecore.base.TCApp;
import com.mjiayou.trecore.common.Configs;
import com.mjiayou.trecorelib.util.LogUtil;

/**
 * Created by treason on 16/5/14.
 */
public class VolleyHelper {

    private static final String TAG = VolleyHelper.class.getSimpleName();
    private static volatile RequestQueue mRequestQueue;

    /**
     * 初始化
     */
    public static void init() {
        LogUtil.printInit(TAG);

        VolleyLog.DEBUG = Configs.DEBUG_VOLLEY;
    }

    /**
     * 获取RequestQueue对象
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            synchronized (VolleyHelper.class) {
                if (mRequestQueue == null) {
                    mRequestQueue = Volley.newRequestQueue(TCApp.get());
                }
            }
        }
        return mRequestQueue;
    }
}
