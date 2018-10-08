package com.haoyang.lovelyreader.tre.ui;

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
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;
import com.haoyang.lovelyreader.tre.bean.UpdateBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.CommonData;
import com.haoyang.lovelyreader.tre.bean.api.CommonParam;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.net.MyFileCallback;
import com.haoyang.lovelyreader.tre.ui.dialog.UpdateDialog;
import com.haoyang.lovelyreader.tre.ui.frgament.HomeFragment;
import com.haoyang.lovelyreader.tre.ui.frgament.MineFragment;
import com.haoyang.lovelyreader.tre.util.Utils;
import com.mjiayou.trecorelib.dialog.TCAlertDialog;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.http.okhttp.RequestBuilder;
import com.mjiayou.trecorelib.http.okhttp.RequestCallback;
import com.mjiayou.trecorelib.json.JsonHelper;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.ToastUtils;
import com.mjiayou.trecorelib.util.UserUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by xin on 18/9/22.
 */
public class MainActivity extends BaseActivity {

    private final int REQUEST_CODE_ADD_BOOK = 102;

    private final int FRAGMENT_HOME = 0;
    private final int FRAGMENT_MINE = 1;

    private ViewPager viewPager;
    private ImageView ivAdd;
    private LinearLayout llHome;
    private ImageView ivHome;
    private TextView tvHome;
    private LinearLayout llMine;
    private ImageView ivMine;
    private TextView tvMine;

    private HomeFragment mHomeFragment;
    private MineFragment mMineFragment;

    private MainAdapter mMainAdapter;
    private List<Fragment> mFragmentList;

    private UpdateBean mUpdateBean;
    private String mPkgPath;
    private String mPkgName;
    private RemoteViews mNotificationView;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private int mPreProgress = -1; //记录上次进度值，用于采样

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        // findViewById
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ivAdd = (ImageView) findViewById(R.id.ivAdd);
        llHome = (LinearLayout) findViewById(R.id.llHome);
        ivHome = (ImageView) findViewById(R.id.ivHome);
        tvHome = (TextView) findViewById(R.id.tvHome);
        llMine = (LinearLayout) findViewById(R.id.llMine);
        ivMine = (ImageView) findViewById(R.id.ivMine);
        tvMine = (TextView) findViewById(R.id.tvMine);

        initView();
        checkUpdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_BOOK:
                    if (mHomeFragment != null) {
                        mHomeFragment.onAddBook(data);
                    }
                    break;
            }
        }
    }

    /**
     * initView
     */
    @Override
    protected void initView() {
        Log.d(TAG, "initView() called");

        // ivAdd
        ivAdd.setOnClickListener(mOnClickListener);
        ivAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(mContext, com.haoyang.lovelyreader.ui.MainActivity.class));
                return false;
            }
        });
        // llHome
        llHome.setOnClickListener(mOnClickListener);
        // llMine
        llMine.setOnClickListener(mOnClickListener);

        initViewPager();
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        Log.d(TAG, "initViewPager() called");

        mHomeFragment = new HomeFragment();
        mMineFragment = new MineFragment();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(mHomeFragment);
        mFragmentList.add(mMineFragment);

        mMainAdapter = new MainAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(mMainAdapter);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        viewPager.setCurrentItem(FRAGMENT_HOME);
        switchFragment(FRAGMENT_HOME);
    }

    /**
     * switchFragment
     */
    private void switchFragment(int position) {
        LogUtils.d(TAG, "switchFragment() called with: position = [" + position + "]");

        int colorSelected = getResources().getColor(R.color.app_theme);
        int colorNormal = getResources().getColor(R.color.color_333333);

        switch (position) {
            case FRAGMENT_HOME:
                ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_main_home_selected));
                ivMine.setImageDrawable(getResources().getDrawable(R.drawable.ic_main_mine_normal));
                tvHome.setTextColor(colorSelected);
                tvMine.setTextColor(colorNormal);
                ivAdd.setVisibility(View.VISIBLE);
                break;
            case FRAGMENT_MINE:
                ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_main_home_normal));
                ivMine.setImageDrawable(getResources().getDrawable(R.drawable.ic_main_mine_selected));
                tvHome.setTextColor(colorNormal);
                tvMine.setTextColor(colorSelected);
                ivAdd.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * mOnClickListener
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogUtils.d(TAG, "onClick() called with: v = [" + v + "]");

            switch (v.getId()) {
                case R.id.ivAdd:
                    startActivityForResult(new Intent(mContext, FileActivity.class), REQUEST_CODE_ADD_BOOK);
                    break;
                case R.id.llHome:
                    viewPager.setCurrentItem(FRAGMENT_HOME);
                    break;
                case R.id.llMine:
                    viewPager.setCurrentItem(FRAGMENT_MINE);
                    break;
            }
        }
    };

    /**
     * mOnPageChangeListener
     */
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //LogUtil.d(TAG, "onPageScrolled() called with: position = [" + position + "], positionOffset = [" + positionOffset + "], positionOffsetPixels = [" + positionOffsetPixels + "]");
        }

        @Override
        public void onPageSelected(int position) {
            LogUtils.d(TAG, "onPageSelected() called with: position = [" + position + "]");

            switchFragment(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            LogUtils.d(TAG, "onPageScrollStateChanged() called with: state = [" + state + "]");
        }
    };

    /**
     * MainAdapter
     */
    public class MainAdapter extends FragmentPagerAdapter {

        private List<Fragment> mList = new ArrayList<>();

        public MainAdapter(FragmentManager fragmentManager, List<Fragment> list) {
            super(fragmentManager);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList != null ? mList.size() : 0;
        }
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {

//        String url = UrlConfig.apiAppUpgrade;
//        String token = UserUtils.getToken();
//        String type = "2";
//        String jsonData = JsonHelper.get().toJson(appUpgradeRequest);
//        String urlWithSign = EncodeHelper.getUrlWithSign(url, token, type, jsonData);

        CommonParam commonParam = new CommonParam();
        commonParam.setData("1.0.0");
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setCommonData(CommonData.get());
        apiRequest.setParam(commonParam);
        String content = JsonHelper.get().toJson(apiRequest);

        RequestEntity requestEntity = new RequestEntity(UrlConfig.apiAppUpgrade);
        requestEntity.setMethod(RequestMethod.POST_STRING);
        requestEntity.setContent(content);
        requestEntity.addHeader("token", UserUtils.getToken());
        requestEntity.addHeader("sign", EncodeHelper.getSign(content));
        RequestBuilder.get().send(requestEntity, new RequestCallback<UpdateBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int code, UpdateBean updateBean) {
                if (updateBean != null) {
                    showUpdateDialog(updateBean);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtils.show(msg);
            }
        });
    }

    /**
     * 提示升级窗口
     */
    private void showUpdateDialog(final UpdateBean updateBean) {
        UpdateDialog dialog = new UpdateDialog(mContext);
        dialog.setTitle("有新版本");
        dialog.setMessage(updateBean.getDesc());
        dialog.setOkMenu("升级");
        dialog.setCancelMenu("取消");
        dialog.setTCActionListener(new TCAlertDialog.OnTCActionListener() {
            @Override
            public void onOkAction() {
                ToastUtils.show("开始下载...");
                String testUrl = "http://172.16.70.25:8080/download/c_release_A/builds/148/archive/uxinUsedCar/build/outputs/apk/UxinUsedcar_release_148.apk";
                updateBean.setAppUrl(testUrl);
                downloadInstallApk(updateBean);
            }

            @Override
            public void onCancelAction() {
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    /***
     * 点击升级后执行下载安装方法
     */
    public void downloadInstallApk(UpdateBean updateBean) {
        LogUtils.d(TAG, "downloadInstallApk() called with: updateBean = [" + updateBean + "]");

        if (updateBean == null) {
            return;
        }
        mUpdateBean = updateBean;
        mPkgPath = Environment.getExternalStorageDirectory().getPath() + "/LovelyReader/";
        mPkgName = mContext.getPackageName();
        File pkg = new File(mPkgPath, mPkgName + ".apk");
        if (pkg.exists()) {
            installNewApk(mActivity, pkg);
        } else {
            downloadApk();
        }
    }

    /**
     * 下载apk
     */
    private void downloadApk() {
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
                        createNotification();
                    }

                    @Override
                    public void inProgress(long progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        setNotificationProgress(total, progress);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        refreshNotificationState(null, false);
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        File pkg = new File(mPkgPath, mPkgName + ".apk");
                        File f = (File) response;
                        f.renameTo(pkg); // 改名以标记下载完成
                        refreshNotificationState(pkg, true);
                        installNewApk(mActivity, pkg);
                    }
                });
    }

    /**
     * 开始下载，非强制更新，在通知栏显示下载进度
     */
    protected void createNotification() {
        LogUtils.d(TAG, "createNotification() called");

        mNotification = new Notification();
        mNotification.icon = android.R.drawable.stat_sys_download;
        mNotification.tickerText = "正在下载";
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;

        mNotificationView = new RemoteViews(mContext.getPackageName(), R.layout.notification_item);
        mNotificationView.setImageViewBitmap(R.id.notificationImage, Utils.getAppIcon(mContext, mPkgName));

        mNotificationView.setTextViewText(R.id.notificationTitle, "正在下载");
        mNotificationView.setTextViewText(R.id.notificationPercent, "0%");
        mNotificationView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            mNotification.bigContentView = mNotificationView;
            mNotification.contentView = mNotificationView;
        } else {
            mNotification.contentView = mNotificationView;
        }
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
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
    protected void refreshNotificationState(File pkg, boolean isSuccess) {
        LogUtils.d(TAG, "refreshNotificationState() called with: pkg = [" + pkg + "], isSuccess = [" + isSuccess + "]");
        mNotificationView.setTextViewText(R.id.notificationTitle, isSuccess ? "下载完成，点击安装" : "下载失败，请重试");
        mNotificationView.setTextViewText(R.id.notificationPercent, "100%");
        mNotificationView.setProgressBar(R.id.notificationProgress, 100, 100, false);
        mNotification.contentView = mNotificationView;
        if (isSuccess) {
            Intent updateIntent = new Intent(Intent.ACTION_VIEW);
            Uri data;
            if (Build.VERSION.SDK_INT >= 24) {
                data = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", pkg);
                updateIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                data = Uri.fromFile(pkg);
            }
            updateIntent.setDataAndType(data, "application/vnd.android.package-archive");
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, updateIntent, 0);
            mNotification.contentIntent = pendingIntent;
        }
        mNotificationManager.notify(R.layout.notification_item, mNotification);
    }

    private void installNewApk(Activity context, File file) {
        LogUtils.d(TAG, "installNewApk() called with: context = [" + context + "], file = [" + file + "]");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= 24) {
            data =
                    FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");

        //handle target application have the uri permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, data, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }

        context.startActivity(intent);
    }
}


