package com.mjiayou.trecore.common;

import com.mjiayou.trecore.base.TCApp;
import com.mjiayou.trecorelib.util.LogUtil;
import com.mjiayou.trecorelib.util.AppUtil;
import com.mjiayou.trecorelib.util.DeviceUtil;

/**
 * Created by treason on 2016/11/28.
 */

public class Caches {

    private static final String TAG = Caches.class.getSimpleName();
    private static volatile Caches mInstance;

    // 包名
    private String packageName;
    // 版本信息
    private int versionCode;
    private String versionName;
    // 屏幕信息
    private int screenWidth;
    private int screenHeight;

    /**
     * 构造函数
     */
    private Caches() {
    }

    /**
     * 单例模式，获取实例
     */
    public static Caches get() {
        if (mInstance == null) {
            synchronized (Configs.class) {
                if (mInstance == null) {
                    mInstance = new Caches();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     */
    public void init() {
        LogUtil.printInit(TAG);

        // 设置包名
        setPackageName(AppUtil.getPackageName(TCApp.get()));
        // 设置版本信息
        setVersionCode(AppUtil.getVersionCode(TCApp.get()));
        setVersionName(AppUtil.getVersionName(TCApp.get()));
        // 设置屏幕信息
        setScreenWidth(DeviceUtil.getScreenWidth(TCApp.get()));
        setScreenHeight(DeviceUtil.getScreenHeight(TCApp.get()));
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    // ******************************** custom ********************************

    public int getScreenWidth(double ratio) {
        return (int) (screenWidth * ratio);
    }

    public int getScreenHeight(double ratio) {
        return (int) (screenHeight * ratio);
    }

    // ******************************** project ********************************
}
