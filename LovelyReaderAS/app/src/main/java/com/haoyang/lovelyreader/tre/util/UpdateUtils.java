package com.haoyang.lovelyreader.tre.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.RemoteViews;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.UpdateBean;
import com.haoyang.lovelyreader.tre.helper.Configs;
import com.haoyang.lovelyreader.tre.net.MyFileCallback;
import com.mjiayou.trecorelib.util.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by xin on 18/11/7.
 */

public class UpdateUtils {

    // TAG
    protected final String TAG = "UpdateUtils";

    private static UpdateUtils mInstance;

    private UpdateBean mUpdateBean;
    private String mPkgPath;
    private String mPkgName;
    private RemoteViews mNotificationView;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private int mPreProgress = -1; //记录上次进度值，用于采样

    public static UpdateUtils get() {
        if (mInstance == null) {
            mInstance = new UpdateUtils();
        }
        return mInstance;
    }

    /***
     * 点击升级后执行下载安装方法
     */
    public void downloadInstallApk(Activity activity, UpdateBean updateBean) {
        LogUtils.d(TAG, "downloadInstallApk() called with: updateBean = [" + updateBean + "]");

        if (updateBean == null) {
            return;
        }
        mUpdateBean = updateBean;
        mPkgPath = Configs.DIR_SDCARD_PROJECT_UPDATE;
        mPkgName = activity.getPackageName();
        File pkg = new File(mPkgPath, mPkgName + ".apk");
        if (pkg.exists()) {
            installNewApk(activity, pkg);
        } else {
            downloadApk(activity);
        }
    }

    /**
     * 下载apk
     */
    private void downloadApk(Activity activity) {
        LogUtils.d(TAG, "downloadApk() called");

        if (mUpdateBean == null) {
            return;
        }
        OkHttpUtils.get()
                .url(mUpdateBean.getAppUrl())
                .build()
                .execute(new MyFileCallback(mPkgPath, mPkgName) {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        mPreProgress = -1;
                        createNotification(activity);
                    }

                    @Override
                    public void inProgress(long progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        setNotificationProgress(total, progress);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        refreshNotificationState(activity, null, false);
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        File pkg = new File(mPkgPath, mPkgName + ".apk");
                        File f = (File) response;
                        f.renameTo(pkg); // 改名以标记下载完成
                        refreshNotificationState(activity, pkg, true);
                        installNewApk(activity, pkg);
                    }
                });
    }

    /**
     * 开始下载，非强制更新，在通知栏显示下载进度
     */
    protected void createNotification(Activity activity) {
        LogUtils.d(TAG, "createNotification() called");

        mNotification = new Notification();
        mNotification.icon = android.R.drawable.stat_sys_download;
        mNotification.tickerText = "正在下载";
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;

        mNotificationView = new RemoteViews(activity.getPackageName(), R.layout.notification_item);
        mNotificationView.setImageViewBitmap(R.id.notificationImage, Utils.getAppIcon(activity, mPkgName));

        mNotificationView.setTextViewText(R.id.notificationTitle, "正在下载");
        mNotificationView.setTextViewText(R.id.notificationPercent, "0%");
        mNotificationView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            mNotification.bigContentView = mNotificationView;
            mNotification.contentView = mNotificationView;
        } else {
            mNotification.contentView = mNotificationView;
        }
        mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(R.layout.notification_item, mNotification);
    }

    /**
     * 正在下载，更新通知栏进度条
     */
    private void setNotificationProgress(long total, long current) {
        LogUtils.d(TAG, "setNotificationProgress() called with: total = [" + total + "], current = [" + current + "]");
        int progress = (int) (current * 100 / total);
        if (progress == mPreProgress) return;
        mPreProgress = progress;
        mNotificationView.setTextViewText(R.id.notificationPercent, progress + "%");
        mNotificationView.setProgressBar(R.id.notificationProgress, 100, progress, false);
        mNotification.contentView = mNotificationView;
        mNotificationManager.notify(R.layout.notification_item, mNotification);
    }

    /**
     * 下载成功，通知栏可点击／失败提示
     *
     * @param isSuccess 是否下载成功
     */
    protected void refreshNotificationState(Activity activity, File pkg, boolean isSuccess) {
        LogUtils.d(TAG, "refreshNotificationState() called with: pkg = [" + pkg + "], isSuccess = [" + isSuccess + "]");
        mNotificationView.setTextViewText(R.id.notificationTitle, isSuccess ? "下载完成，点击安装" : "下载失败，请重试");
        mNotificationView.setTextViewText(R.id.notificationPercent, "100%");
        mNotificationView.setProgressBar(R.id.notificationProgress, 100, 100, false);
        mNotification.contentView = mNotificationView;
        if (isSuccess) {
            Intent updateIntent = new Intent(Intent.ACTION_VIEW);
            Uri data;
            if (Build.VERSION.SDK_INT >= 24) {
                data = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", pkg);
                updateIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                data = Uri.fromFile(pkg);
            }
            updateIntent.setDataAndType(data, "application/vnd.android.package-archive");
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, updateIntent, 0);
            mNotification.contentIntent = pendingIntent;
        }
        mNotificationManager.notify(R.layout.notification_item, mNotification);
    }

    private void installNewApk(Activity activity, File file) {
        LogUtils.d(TAG, "installNewApk() called with: context = [" + activity + "], file = [" + file + "]");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= 24) {
            data =
                    FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");

        //handle target application have the uri permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                activity.grantUriPermission(packageName, data, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }

        activity.startActivity(intent);
    }
}
