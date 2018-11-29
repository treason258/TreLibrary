package com.mjiayou.trebundle.debug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mjiayou.trebundle.R;
import com.mjiayou.trecore.base.TCActivity;
import com.mjiayou.trecore.bean.entity.TCMenu;
import com.mjiayou.trecore.dialog.DialogHelper;
import com.mjiayou.trecore.dialog.TCAlertDialog;
import com.mjiayou.trecore.util.MenuUtil;
import com.mjiayou.trecore.util.UserUtil;
import com.mjiayou.trecorelib.util.HelloUtil;
import com.mjiayou.trecorelib.util.LogUtil;
import com.mjiayou.trecorelib.util.SharedUtil;
import com.mjiayou.trecorelib.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class DebugActivity extends TCActivity {

    @InjectView(R.id.layout_menu_container)
    LinearLayout mLayoutMenuContainer;
    @InjectView(R.id.tv_info)
    TextView mTvInfo;

    /**
     * startActivity
     */
    public static void open(Context context) {
        Intent intent = new Intent(context, DebugActivity.class);
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
        getTitleBar().setTitle(TAG);
        getTitleBar().addLeftImageView(R.drawable.tc_launcher, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show("addLeftImageView");
            }
        });
        getTitleBar().addRightTextView("Debug", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show("addRightTextView");
            }
        });

        // MaterialDrawer
        initMaterialDrawer();

        // mLayoutMenuContainer
        MenuUtil.setMenus(mContext, mLayoutMenuContainer, getMenus());

        // mTvInfo
        mTvInfo.append("\n" + HelloUtil.getHI() + "\n");
        mTvInfo.append(DebugUtil.getBuildConfigInfo(mContext));
//        mTvInfo.append(AppUtil.getAppInfoDetail(mContext));
        mTvInfo.append(com.mjiayou.trecorelib.util.AppUtil.getAppInfoDetail(mContext));
    }

    /**
     * MaterialDrawer
     */
    private void initMaterialDrawer() {
        // accountHeader
        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.tc_shape_rect_gradient_theme)
                .addProfiles(
                        new ProfileDrawerItem().withName("NameA").withEmail("EmailA").withIdentifier(0).withIcon(R.drawable.tc_launcher),
                        new ProfileDrawerItem().withName("NameB").withEmail("EmailB").withIdentifier(1).withIcon(R.drawable.tc_launcher),
                        new ProfileDrawerItem().withName("NameC").withEmail("EmailC").withIdentifier(2).withIcon(R.drawable.tc_launcher))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        ToastUtil.show("onProfileChanged | profile.getIdentifier() -> " + profile.getIdentifier() + " | profile.getName() -> " + profile.getName());
                        return true;
                    }
                })
                .build();
        accountHeader.setActiveProfile(0);

        // drawerBuilder
        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withSliderBackgroundColor(getResources().getColor(R.color.tc_white_alpha_light))
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new SectionDrawerItem().withName("SectionDrawerA"),
                        new PrimaryDrawerItem().withName("PrimaryDrawer000").withIcon(R.drawable.tc_launcher).withIdentifier(0).withDescription("description000"),
                        new SectionDrawerItem().withName("SectionDrawerB"),
                        new PrimaryDrawerItem().withName("PrimaryDrawer111").withIcon(R.drawable.tc_launcher).withIdentifier(1),
                        new SectionDrawerItem().withName("SectionDrawerC"),
                        new PrimaryDrawerItem().withName("PrimaryDrawer222").withIcon(R.drawable.tc_launcher),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("PrimaryDrawer333"),
                        new PrimaryDrawerItem().withName("PrimaryDrawer444")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        ToastUtil.show("onItemClick | drawerItem.getIdentifier() -> " + drawerItem.getIdentifier());
                        return true;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                        ToastUtil.show("onItemLongClick | drawerItem.getIdentifier() -> " + drawerItem.getIdentifier());
                        return true;
                    }
                })
                .build();
        drawer.setSelection(0);
    }

    /**
     * getMenus
     */
    public List<TCMenu> getMenus() {
        List<TCMenu> tcMenus = new ArrayList<>();
        tcMenus.add(new TCMenu("TestActivity", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestActivity.open(mContext);
            }
        }));
        tcMenus.add(new TCMenu("LOG AND TOAST", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show("LOG AND TOAST TEST");
                LogUtil.i("LOG AND TOAST TEST");
            }
        }));
        tcMenus.add(new TCMenu("设置为第一次启动", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.createTCAlertDialog(mContext,
                        "提示", "确认设置为第一次启动？",
                        "确定", "取消", true,
                        new TCAlertDialog.OnTCActionListener() {
                            @Override
                            public void onOkAction() {
                                SharedUtil.get(mContext).setConfigIsFirst(true);
                                ToastUtil.show("设置第一次启动成功");
                            }

                            @Override
                            public void onCancelAction() {
                            }
                        }).show();
            }
        }));
        tcMenus.add(new TCMenu("模拟登录", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.createTCAlertDialog(mContext,
                        "提示", "确认模拟登陆？",
                        "确定", "取消", true,
                        new TCAlertDialog.OnTCActionListener() {
                            @Override
                            public void onOkAction() {
                                UserUtil.doLogin("12345678901234567890123456789012");
                                ToastUtil.show("模拟登录成功");
                            }

                            @Override
                            public void onCancelAction() {
                            }
                        }).show();
            }
        }));
        tcMenus.add(new TCMenu("退出登录", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.createTCAlertDialog(mContext,
                        "提示", "确认退出登录？",
                        "确定", "取消", true,
                        new TCAlertDialog.OnTCActionListener() {
                            @Override
                            public void onOkAction() {
                                UserUtil.doLogout();
                                ToastUtil.show("退出登录成功");
                            }

                            @Override
                            public void onCancelAction() {
                            }
                        }).show();
            }
        }));
        tcMenus.add(new TCMenu("NullPointExceptionTest", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.createTCAlertDialog(mContext,
                        "提示", "确认抛出NullPointerException？",
                        "确定", "取消", true,
                        new TCAlertDialog.OnTCActionListener() {
                            @Override
                            public void onOkAction() {
                                // 抛出异常
                                throw new NullPointerException("NullPointExceptionTest");
                            }

                            @Override
                            public void onCancelAction() {
                            }
                        }).show();
            }
        }));
        return tcMenus;
    }

    // ******************************** project ********************************
}
