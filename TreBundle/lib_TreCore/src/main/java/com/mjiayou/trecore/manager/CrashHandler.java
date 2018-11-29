//package com.mjiayou.trecore.manager;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Looper;
//import android.os.Process;
//import android.widget.Toast;
//
//import com.mjiayou.trecore.base.TCApp;
//import com.mjiayou.trecore.common.Configs;
//import com.mjiayou.trecore.helper.UmengHelper;
//import com.mjiayou.trecore.util.ThrowableUtil;
//import com.mjiayou.trecorelib.util.AppUtil;
//import com.mjiayou.trecorelib.util.DateUtil;
//import com.mjiayou.trecorelib.util.DeviceUtil;
//import com.mjiayou.trecorelib.util.DirectoryUtil;
//import com.mjiayou.trecorelib.util.FileUtil;
//import com.mjiayou.trecorelib.util.LogUtil;
//import com.mjiayou.trecorelib.util.ToastUtil;
//
///**
// * Created by treason on 15/11/18.
// */
//public class CrashHandler {
//
//    public static final String TAG = CrashHandler.class.getSimpleName();
//
//    // CrashHandler 实例
//    private static CrashHandler mInstance;
//    // 来自 MainActivity 的 mActivity，主要用户异常时，弹出分享界面
//    private Activity mActivity;
//    // 系统默认的 UncaughtException 处理类
//    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
//
//    /**
//     * 保证只有一个实例
//     */
//    private CrashHandler() {
//    }
//
//    /**
//     * 单例模式获取CrashHandler实例
//     */
//    public static CrashHandler get() {
//        if (mInstance == null) {
//            mInstance = new CrashHandler();
//        }
//        return mInstance;
//    }
//
//    /**
//     * 初始化
//     */
//    public void init(Activity activity) {
//        LogUtil.printInit(TAG);
//
//        mActivity = activity;
//        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
//
//        // 设置该 CrashHandler 为程序的默认处理器
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread thread, Throwable throwable) {
//                // 手动向 Umeng 提交 Throwable
//                try {
//                    UmengHelper.reportError(TCApp.get(), throwable);
//                } catch (Exception e) {
//                    LogUtil.printStackTrace(e);
//                }
//
//                // 处理未捕获异常
//                if (handleException(throwable)) { // 用户处理异常
//                    try {
//                        Thread.sleep(Configs.DELAY_CRASH_FINISH);
//                    } catch (InterruptedException e) {
//                        LogUtil.printStackTrace(e);
//                    }
//                    // 退出程序
//                    Process.killProcess(Process.myPid());
//                    System.exit(1);
//                } else { // 如果用户没有处理则让系统默认的异常处理器来处理
//                    if (mDefaultUncaughtExceptionHandler != null) {
//                        mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * 自定义错误处理，收集错误信息、分享错误报告等操作均在此完成。
//     *
//     * @return true-处理了该异常信息；否则返回false。
//     */
//    private boolean handleException(Throwable throwable) {
//        if (throwable == null) {
//            return false;
//        }
//
//        try {
//            // 使用Toast来显示异常信息
//            new Thread() {
//                @Override
//                public void run() {
//                    Looper.prepare();
//                    ToastUtil.show("很抱歉，程序出现异常，即将退出。", Toast.LENGTH_LONG);
//                    Looper.loop();
//                }
//            }.start();
//
//            dealWithCrash(throwable);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    /**
//     * 收集设备参数信息，保存错误信息到文件中
//     */
//    private void dealWithCrash(Throwable throwable) {
//        try {
//            // 设置保存文件路径和内容
//            String fileDir = DirectoryUtil.get().APP_LOG;
//            String fileName = buildFileName();
//            String filePath = fileDir + fileName;
//            String fileContent = buildFileContent(throwable);
//
//            // 分享文件内容
//            shareText(fileContent);
//
//            // 保存文件
//            FileUtil.writeToFile(filePath, fileContent);
//            logFileInfo(filePath, fileContent);
//
//            // 重启应用
//            // restartApp();
//        } catch (Exception e) {
//            LogUtil.printStackTrace(e);
//        }
//    }
//
//    /**
//     * 构建文件名
//     */
//    private String buildFileName() {
//        return "crash-" + DateUtil.parseString(System.currentTimeMillis(), DateUtil.FormatType.FORMAT_302) + ".log";
//    }
//
//    /**
//     * 构建文件内容
//     */
//    private String buildFileContent(Throwable throwable) {
//        StringBuilder builder = new StringBuilder();
//        // time
//        builder.append("-------- DateTime --------").append("\n");
//        builder.append(DateUtil.parseString(System.currentTimeMillis(), DateUtil.FormatType.FORMAT_101)).append("\n");
//        // VersionInfo
//        builder.append("-------- VersionInfo --------").append("\n");
//        builder.append(AppUtil.getVersionInfo(TCApp.get()));
//        // ThrowableInfo
//        builder.append("-------- ThrowableInfo --------").append("\n");
//        builder.append(ThrowableUtil.getThrowableInfo(throwable));
//        // BuildInfo
//        builder.append("-------- BuildInfo --------").append("\n");
//        builder.append(DeviceUtil.getBuildInfo());
//        return builder.toString();
//    }
//
//    /**
//     * 分享
//     */
//    private void shareText(String text) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("text/plain"); // 纯文本
//        intent.putExtra(Intent.EXTRA_TEXT, text);
//        mActivity.startActivity(intent);
//    }
//
//    /**
//     * LOG
//     */
//    private void logFileInfo(String filePath, String fileContent) {
////        StringBuilder builder = new StringBuilder();
////        builder.append("------------------------------------------------").append("\n");
////        builder.append("---------------- filePath ----------------").append("\n");
////        builder.append(filePath).append("\n");
////        builder.append("---------------- fileContent ----------------").append("\n");
////        builder.append(fileContent).append("\n");
////        builder.append("------------------------------------------------").append("\n");
////        LogUtil.e(TAG, builder.toString());
//
//        LogUtil.e(TAG, "------------------------------------------------");
//        LogUtil.e(TAG, "---------------- filePath ----------------");
//        LogUtil.e(TAG, filePath);
//        LogUtil.e(TAG, "---------------- fileContent ----------------");
//        LogUtil.e(TAG, fileContent);
//        LogUtil.e(TAG, "------------------------------------------------");
//    }
//
//    /**
//     * 重新启动应用
//     */
//    private void restartApp() {
//        Intent intent1 = new Intent(mActivity, mActivity.getClass());
//        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        mActivity.startActivity(intent1);
//        System.exit(0);
//    }
//}
