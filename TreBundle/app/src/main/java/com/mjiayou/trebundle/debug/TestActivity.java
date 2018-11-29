package com.mjiayou.trebundle.debug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mjiayou.trebundle.R;
import com.mjiayou.trecore.base.TCActivity;
import com.mjiayou.trecore.base.TCApp;
import com.mjiayou.trecore.base.TCMenuActivity;
import com.mjiayou.trecore.bean.entity.TCMenu;
import com.mjiayou.trecore.dialog.DialogHelper;
import com.mjiayou.trecorelib.helper.GsonHelper;
import com.mjiayou.trecore.net.RequestAdapter;
import com.mjiayou.trecore.service.WindowService;
import com.mjiayou.trecore.util.MenuUtil;
import com.mjiayou.trecore.util.ServiceUtil;
import com.mjiayou.trecorelib.bean.TCResponse;
import com.mjiayou.trecorelib.bean.TCSinaStatusesResponse;
import com.mjiayou.trecorelib.util.HandlerUtil;
import com.mjiayou.trecorelib.util.LogUtil;
import com.mjiayou.trecorelib.util.PageUtil;
import com.mjiayou.trecorelib.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class TestActivity extends TCActivity {

    @InjectView(R.id.layout_menu_container)
    LinearLayout mLayoutMenuContainer;
    @InjectView(R.id.tv_info)
    TextView mTvInfo;

    /**
     * startActivity
     */
    public static void open(Context context) {
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tc_activity_menu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // mTitleBar
        getTitleBar().setTitle("TestActivity");

        // mLayoutMenuContainer
        MenuUtil.setMenus(mContext, mLayoutMenuContainer, getMenus());

        // mTvInfo
        mTvInfo.setText("");
    }

    /**
     * getMenus
     */
    public List<TCMenu> getMenus() {
        List<TCMenu> tcMenus = new ArrayList<>();
        tcMenus.add(new TCMenu("TEMP", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show("TEMP");
            }
        }));
        tcMenus.add(new TCMenu("TCMenuActivity-FULL", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TCMenuActivity.open(mContext, "TCMenuActivity", "TCMenuActivity Info Test", MenuUtil.getTCMenus(mContext));
            }
        }));
        tcMenus.add(new TCMenu("Dialog测试", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.showDialogDemo(mContext);
            }
        }));
        tcMenus.add(new TCMenu("StatusViewManager - onLoading", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStatusViewManager().onLoading();
                HandlerUtil.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getStatusViewManager().onFailure("数据异常", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getStatusViewManager().onSuccess();
                            }
                        });
                    }
                }, 3000);
            }
        }));
        tcMenus.add(new TCMenu("Test API", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.traceStart("网络请求开始");
                RequestAdapter requestAdapter = new RequestAdapter(mContext, new RequestAdapter.DataResponse() {
                    @Override
                    public void callback(Message msg) {
                        switch (msg.what) {
                            case RequestAdapter.SINA_STATUSES: {
                                LogUtil.traceStop("网络响应");
                                TCSinaStatusesResponse response = (TCSinaStatusesResponse) msg.obj;
                                if (response != null) {
                                    ToastUtil.show(GsonHelper.get().toJson(response));
                                } else {
                                    ToastUtil.show(TCApp.get().getResources().getString(R.string.tc_error_response_null));
                                }
                                break;
                            }
                        }
                    }

                    @Override
                    public void refreshView(TCResponse response) {
                    }
                });
                requestAdapter.sinaStatuses(String.valueOf(PageUtil.pageReset()));
            }
        }));
        tcMenus.add(new TCMenu("TestWeiboActivity", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestWeiboActivity.open(mContext);
            }
        }));
        tcMenus.add(new TCMenu("TestTipsActivity", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestTipsActivity.open(mContext);
            }
        }));
        tcMenus.add(new TCMenu("TestCanvasActivity", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestCanvasActivity.open(mContext);
            }
        }));

        final Intent windowService = new Intent(mContext, WindowService.class);
        if (ServiceUtil.isServiceRunning(mContext, WindowService.class)) {
            ToastUtil.show("WindowService is running");
        } else {
            ToastUtil.show("WindowService is stopped");
        }
        tcMenus.add(new TCMenu("WindowService start", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(windowService);
            }
        }));
        tcMenus.add(new TCMenu("WindowService stop", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(windowService);
            }
        }));

//        tcMenus.add(new TCMenu("TestProviderActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TestProviderActivity.open(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("TestAIDLActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TestAIDLActivity.open(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("TestMessengerActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TestMessengerActivity.open(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("TestProcessBActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TestProcessBActivity.open(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("TestTouchActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TestTouchActivity.open(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("TestCameraActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TestCameraActivity.open(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("TestSurfaceViewActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TestSurfaceViewActivity.open(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("TestCanvasActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TestCanvasActivity.open(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("TestCustomViewActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TestCustomViewActivity.open(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("TestJNIActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TestJNIActivity.open(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("TestHacksActivity", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TestHacksActivity.open(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("友盟分享测试", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TCUMShare tcumShare = new TCUMShare();
//                tcumShare.setTitle(Configs.DEFAULT_SHARE_TARGET_TITLE);
//                tcumShare.setDesc(Configs.DEFAULT_SHARE_TARGET_CONTENT);
//                tcumShare.setImgUrl(Configs.DEFAULT_SHARE_IMAGE_URL);
//                tcumShare.setTargetUrl(Configs.DEFAULT_SHARE_TARGET_URL);
//                UmengHelper.openShare(mActivity, null, UmengHelper.SHARE_FOR_NORMAL, tcumShare);
//            }
//        }));
//        tcMenus.add(new TCMenu("网页视频测试", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        }));
//        tcMenus.add(new TCMenu("视频测试", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Router.openTestVideoActivity(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("Test Main", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Router.openTestSplashActivity(mContext);
//            }
//        }));
//        tcMenus.add(new TCMenu("Test Weibo", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Router.openTestWeiboActivity(mContext);
//            }
//        }));
        return tcMenus;
    }
}
