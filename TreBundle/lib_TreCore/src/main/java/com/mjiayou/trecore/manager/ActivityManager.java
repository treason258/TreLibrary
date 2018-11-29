//package com.mjiayou.trecore.manager;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.view.KeyEvent;
//
//import com.mjiayou.trecorelib.util.ToastUtil;
//
//import java.util.LinkedList;
//
///**
// * Created by treason on 2016/12/7.
// */
//
//public class ActivityManager {
//
//    public static String TAG = ActivityManager.class.getSimpleName();
//
//    public static ActivityManager mInstance;
//
//    // 退出时间
//    private static long mExitTime = 0;
//
//    /**
//     * 存储Activity队列
//     */
//    private LinkedList<Activity> mActivityList = new LinkedList<>();
//
//    /**
//     * 单例模式，获取实例
//     */
//    public static ActivityManager get() {
//        if (mInstance == null) {
//            synchronized (ActivityManager.class) {
//                if (mInstance == null) {
//                    mInstance = new ActivityManager();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    /**
//     * 返回 mActivityList
//     */
//    public final LinkedList<Activity> getActivityList() {
//        return mActivityList;
//    }
//
//    /**
//     * 向 mActivityList 添加 Activity，最新加入的放在最开始的位置
//     */
//    public void addActivity(Activity activity) {
//        mActivityList.add(0, activity);
//    }
//
//    /**
//     * 从 mActivityList 移除 Activity
//     */
//    public void removeActivity(Activity activity) {
//        if (mActivityList.contains(activity)) {
//            mActivityList.remove(activity);
//        }
//    }
//
//    /**
//     * 返回最近一次添加的 Activity
//     */
//    public Activity getTopActivity() {
//        if (mActivityList.size() > 0) {
//            return mActivityList.get(0);
//        }
//        return null;
//    }
//
//    /**
//     * 移除所有 Activity
//     */
//    public boolean clearAllActivity() {
//        int count = mActivityList.size();
//        while (count > 0) {
//            mActivityList.get(0).finish();
//            mActivityList.remove(0);
//            count--;
//        }
//        return true;
//    }
//
//    // ******************************** Exit ********************************
//
//    /**
//     * 将listActivity中的Activity逐一finish掉，实现完全退出应用
//     */
//    public void removeAllActivity() {
//        if (mActivityList != null) {
//            for (Activity activity : mActivityList) {
//                if (activity != null && !activity.isFinishing())
//                    activity.finish();
//            }
//        }
//    }
//
//    public void removeActivity(Class<?> cls) {
//        if (mActivityList != null) {
//            for (Activity activity : mActivityList) {
//                if (activity != null && !activity.isFinishing()) {
//                    if (activity.getClass().getSimpleName().equals(cls.getSimpleName())) {
//                        activity.finish();
//                    }
//                }
//            }
//        }
//    }
//
//    public void removeActivityExcept(Class<?> cls) {
//        if (mActivityList != null) {
//            for (Activity activity : mActivityList) {
//                if (activity != null && !activity.isFinishing())
//                    if (!activity.getClass().getSimpleName().equals(cls.getSimpleName())) {
//                        activity.finish();
//                    }
//            }
//        }
//    }
//
//    /**
//     * restartApp
//     */
//    public void restartApp(Activity activity) {
//        removeActivityExcept(activity.getClass());
//
//        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        activity.startActivity(intent);
//    }
//
//    /**
//     * 完全退出应用
//     */
//    public void forceFinish(Activity activity) {
//        removeAllActivity();
//        activity.finish();
//        System.exit(0);
//    }
//
//    /**
//     * 再按一次退出
//     */
//    public boolean pressAgainToExit(Activity activity, int keyCode, KeyEvent event) {
//        if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
//            long currentTime = System.currentTimeMillis();
//            if ((currentTime - mExitTime) >= 2000) {
//                ToastUtil.show(activity, "再按一次退出");
//                mExitTime = currentTime;
//            } else {
//                removeAllActivity();
//            }
//            return true;
//        }
//        return false;
//    }
//}
