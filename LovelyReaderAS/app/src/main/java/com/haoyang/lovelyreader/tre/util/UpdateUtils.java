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
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.UpdateBean;
import com.haoyang.lovelyreader.tre.helper.Configs;
import com.haoyang.lovelyreader.tre.http.MyFileCallback;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.ToastUtils;
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

    private RemoteViews mNotificationView;
    private Notification mNotification;
    private NotificationManager mNotificationManager;

    private int mPreProgress = -1; // 记录上次进度值，用于采样

    /**
     * get
     */
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
        LogUtils.d(TAG, "downloadInstallApk() called with: activity = [" + activity + "], updateBean = [" + updateBean + "]");

        if (updateBean == null || TextUtils.isEmpty(updateBean.getAppUrl())) {
            ToastUtils.show("安装包下载地址为空");
            return;
        }

        String packageName = activity.getPackageName();
        String apkUrl = updateBean.getAppUrl();
        String apkFileDir = Configs.DIR_SDCARD_PROJECT_UPDATE; // apk本地存储路径
        String apkFileName = packageName + ".apk"; // apk本地存储文件名

        File apkFile = new File(apkFileDir, apkFileName);
        if (apkFile.exists()) {
            installApk(activity, apkFile);
        } else {
            downloadApk(activity, apkUrl, apkFileDir, apkFileName);
        }
    }

    /**
     * 下载apk
     */
    private void downloadApk(Activity activity, String apkUrl, String apkFileDir, String apkFileName) {
        LogUtils.d(TAG, "downloadApk() called with: activity = [" + activity + "], apkUrl = [" + apkUrl + "], apkFileDir = [" + apkFileDir + "], apkFileName = [" + apkFileName + "]");

        OkHttpUtils.get()
                .url(apkUrl)
                .build()
                .execute(new MyFileCallback(apkFileDir, apkFileName) {
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
                        File apkFile = new File(apkFileDir, apkFileName);
                        File downloadFile = (File) response;
                        downloadFile.renameTo(apkFile); // 改名以标记下载完成
                        refreshNotificationState(activity, apkFile, true);
                        installApk(activity, apkFile);
                    }
                });
    }

    /**
     * 安装APK
     */
    private void installApk(Activity activity, File apkFile) {
        LogUtils.d(TAG, "installApk() called with: activity = [" + activity + "], apkFile = [" + apkFile + "]");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= 24) {
            data = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");

        // handle target application have the uri permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                activity.grantUriPermission(packageName, data, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }

        activity.startActivity(intent);
    }

    /**
     * 开始下载，非强制更新，在通知栏显示下载进度
     */
    private void createNotification(Activity activity) {
        LogUtils.d(TAG, "createNotification() called");

        mNotification = new Notification();
        mNotification.icon = android.R.drawable.stat_sys_download;
        mNotification.tickerText = "正在下载";
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;

        mNotificationView = new RemoteViews(activity.getPackageName(), R.layout.notification_item);
        mNotificationView.setImageViewBitmap(R.id.ivIcon, Utils.getAppIcon(activity, activity.getPackageName()));
        mNotificationView.setTextViewText(R.id.tvTitle, "正在下载");
        mNotificationView.setTextViewText(R.id.tvPercent, "0%");
        mNotificationView.setProgressBar(R.id.pbProgress, 100, 0, false);

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
        if (progress == mPreProgress) {
            return;
        }
        mPreProgress = progress;
        mNotificationView.setTextViewText(R.id.tvTitle, "正在下载");
        mNotificationView.setTextViewText(R.id.tvPercent, progress + "%");
        mNotificationView.setProgressBar(R.id.pbProgress, 100, progress, false);
        mNotification.contentView = mNotificationView;
        mNotificationManager.notify(R.layout.notification_item, mNotification);
    }

    /**
     * 下载成功，通知栏可点击／失败提示
     *
     * @param isSuccess 是否下载成功
     */
    private void refreshNotificationState(Activity activity, File apkFile, boolean isSuccess) {
        LogUtils.d(TAG, "refreshNotificationState() called with: apkFile = [" + apkFile + "], isSuccess = [" + isSuccess + "]");

        mNotificationView.setTextViewText(R.id.tvTitle, isSuccess ? "下载完成，点击安装" : "下载失败，请重试");
        mNotificationView.setTextViewText(R.id.tvPercent, "100%");
        mNotificationView.setProgressBar(R.id.pbProgress, 100, 100, false);
        mNotification.contentView = mNotificationView;
        if (isSuccess) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data;
            if (Build.VERSION.SDK_INT >= 24) {
                data = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", apkFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                data = Uri.fromFile(apkFile);
            }
            intent.setDataAndType(data, "application/vnd.android.package-archive");

            mNotification.contentIntent = PendingIntent.getActivity(activity, 0, intent, 0);
        }
        mNotificationManager.notify(R.layout.notification_item, mNotification);
    }

    /**
     * 安装apk的intent
     */
    private Intent getInstallIntent(Activity activity, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= 24) {
            data = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        return intent;
    }
}
