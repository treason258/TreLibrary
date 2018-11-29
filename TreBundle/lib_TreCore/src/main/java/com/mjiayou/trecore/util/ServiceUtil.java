package com.mjiayou.trecore.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by treason on 2017/2/7.
 */

public class ServiceUtil {

    /**
     * 判断某个服务是否正在运行
     *
     * @param context     context
     * @param serviceName 包名+类名 EG：com.mjiayou.trecore.service.WindowService，WindowService.class.getName()
     * @return true-正在运行；false-没有运行；
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(40);
        if (services.size() <= 0) {
            return false;
        }
        for (int i = 0; i < services.size(); i++) {
            String name = services.get(i).service.getClassName();
            if (name.equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isServiceRunning(Context context, Class<?> clazz) {
        return isServiceRunning(context, clazz.getName());
    }

    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @param context context
     * @return true-运行在后台；false-运行在前台；
     */
    public static boolean isAppRunningBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
