package com.mjiayou.trecore.util;

import com.mjiayou.trecore.base.TCApp;
import com.mjiayou.trecore.common.Caches;
import com.mjiayou.trecorelib.util.LogUtil;
import com.mjiayou.trecorelib.util.SharedUtil;

/**
 * 版本管理类
 */
public class VersionUtil {

    public static final String TAG = VersionUtil.class.getSimpleName();

    /**
     * 初始化
     */
    public static void init() {
        LogUtil.printInit(TAG);

        // 根本版本号判断如果是 全新安装APP 或者 覆盖安装APP，需要重置ConfigIsFirst
        int currentCode = Caches.get().getVersionCode(); // 当前APK版本号
        int localCode = SharedUtil.get(TCApp.get()).getConfigVersionCode(); // 本地缓存，旧版APK版本号
        LogUtil.i(TAG, "当前APK版本号 -> " + currentCode);
        LogUtil.i(TAG, "旧版APK版本号 -> " + localCode);
        if (localCode == -1) { // 没有旧版APK版本号记录
            LogUtil.i(TAG, "全新安装");
            SharedUtil.get(TCApp.get()).setConfigIsFirst(true);
            SharedUtil.get(TCApp.get()).setConfigVersionCode(currentCode);
        } else {
            LogUtil.i(TAG, "覆盖安装");
            if (currentCode != localCode) {
                LogUtil.i(TAG, "版本号有更新 | " + localCode + " -> " + currentCode);
                SharedUtil.get(TCApp.get()).setConfigIsFirst(true);
                SharedUtil.get(TCApp.get()).setConfigVersionCode(currentCode);
            } else {
                LogUtil.i(TAG, "版本号没有更新");
            }
        }
    }
}
