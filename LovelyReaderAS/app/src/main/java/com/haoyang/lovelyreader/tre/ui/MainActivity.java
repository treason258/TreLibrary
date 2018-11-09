package com.haoyang.lovelyreader.tre.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;
import com.haoyang.lovelyreader.tre.bean.CategoryBean;
import com.haoyang.lovelyreader.tre.bean.UpdateBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.CategoryAddParam;
import com.haoyang.lovelyreader.tre.bean.api.CategoryEditParam;
import com.haoyang.lovelyreader.tre.bean.api.CommonParam;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.Global;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.http.MyRequestEntity;
import com.haoyang.lovelyreader.tre.ui.adapter.CategoryAdapter;
import com.haoyang.lovelyreader.tre.ui.dialog.UpdateDialog;
import com.haoyang.lovelyreader.tre.ui.frgament.HomeFragment;
import com.haoyang.lovelyreader.tre.ui.frgament.MineFragment;
import com.haoyang.lovelyreader.tre.util.LoginUtils;
import com.haoyang.lovelyreader.tre.util.UpdateUtils;
import com.mjiayou.trecorelib.dialog.DialogHelper;
import com.mjiayou.trecorelib.dialog.TCAlertDialog;
import com.mjiayou.trecorelib.http.RequestSender;
import com.haoyang.lovelyreader.tre.http.RequestCallback;
import com.mjiayou.trecorelib.util.AppUtils;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xin on 18/9/22.
 */
public class MainActivity extends BaseActivity {

    private final int FRAGMENT_HOME = 0;
    private final int FRAGMENT_MINE = 1;

    private final int CATEGORY_OPTION_NONE = 11;
    private final int CATEGORY_OPTION_ADD = 12;
    private final int CATEGORY_OPTION_MODIFY = 13;
    private int mCategoryOption = CATEGORY_OPTION_NONE;

    private ViewPager viewPager;
    private LinearLayout llHome;
    private ImageView ivHome;
    private TextView tvHome;
    private LinearLayout llMine;
    private ImageView ivMine;
    private TextView tvMine;

    private HomeFragment mHomeFragment;
    private MineFragment mMineFragment;

    private FragmentAdapter mFragmentAdapter;
    private List<Fragment> mFragmentList;

    // 分类面板
    private DrawerLayout dlMain;
    private RelativeLayout rlCategory;
    private TextView tvSync;
    private ListView lvCategory;
    private RelativeLayout rlAddCategory;
    private RelativeLayout rlCategoryView;
    private EditText etCategoryName;
    private TextView tvSubmit;
    private TextView tvCancel;

    private CategoryAdapter mCategoryAdapter;
    private List<CategoryBean> mListCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        // findViewById
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        llHome = (LinearLayout) findViewById(R.id.llHome);
        ivHome = (ImageView) findViewById(R.id.ivHome);
        tvHome = (TextView) findViewById(R.id.tvHome);
        llMine = (LinearLayout) findViewById(R.id.llMine);
        ivMine = (ImageView) findViewById(R.id.ivMine);
        tvMine = (TextView) findViewById(R.id.tvMine);
        // 分类面板
        dlMain = (DrawerLayout) findViewById(R.id.dlMain);
        rlCategory = (RelativeLayout) findViewById(R.id.rlCategory);
        tvSync = (TextView) findViewById(R.id.tvSync);
        lvCategory = (ListView) findViewById(R.id.lvCategory);
        rlAddCategory = (RelativeLayout) findViewById(R.id.rlAddCategory);
        rlCategoryView = (RelativeLayout) findViewById(R.id.rlCategoryView);
        etCategoryName = (EditText) findViewById(R.id.etCategoryName);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvCancel = (TextView) findViewById(R.id.tvCancel);

        initView();
        checkUpdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * initView
     */
    @Override
    protected void initView() {
        LogUtils.d(TAG, "initView() called");

        // llHome-首页
        llHome.setOnClickListener(mOnClickListener);

        // llMine-我的
        llMine.setOnClickListener(mOnClickListener);

        initCategoryView();
        initViewPager();
    }

    /**
     * 初始化分类面板
     */
    private void initCategoryView() {
        LogUtils.d(TAG, "initCategoryView() called");

        // dlMain-分类面板属性，不可侧滑打开
        dlMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // tvSync-同步数据
        tvSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtils.checkNotLoginAndToast()) {
                    return;
                }
                syncServerData();
            }
        });

        // lvCategory-分类列表
        mListCategory = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(mContext, mListCategory);
        mCategoryAdapter.setOnOptionListener(new CategoryAdapter.OnOptionListener() {
            @Override
            public void onModify(CategoryBean categoryBean, int position) {
                if (Global.mCurrentCategory == null) {
                    ToastUtils.show("未选中任何分类");
                    return;
                }
                if (Global.mCurrentCategory.getLevel() == CategoryBean.LEVEL_0) {
                    ToastUtils.show("默认目录不可修改");
                    return;
                }
                showCategoryView(CATEGORY_OPTION_MODIFY);
            }

            @Override
            public void onDelete(CategoryBean categoryBean, int position) {
                if (Global.mCurrentCategory == null) {
                    ToastUtils.show("未选中任何分类");
                    return;
                }
                if (Global.mCurrentCategory.getLevel() == CategoryBean.LEVEL_0) {
                    ToastUtils.show("默认目录不可删除");
                    return;
                }
                DialogHelper.createTCAlertDialog(mContext, "提示", "确定要删除分类 " + Global.mCurrentCategory.getCategoryName() + "？", "确定", "取消", true,
                        new TCAlertDialog.OnTCActionListener() {
                            @Override
                            public void onOkAction() {
                                deleteCategory(Global.mCurrentCategory, position);
                            }

                            @Override
                            public void onCancelAction() {
                            }
                        }).show();
            }
        });
        lvCategory.setAdapter(mCategoryAdapter);
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Global.mCurrentCategory = mListCategory.get(position);
                for (int i = 0; i < mListCategory.size(); i++) {
                    mListCategory.get(i).setSelected(i == position);
                }
                mCategoryAdapter.notifyDataSetChanged();

                // 同时更新首页书籍展示
                updateBookList(Global.mCurrentCategory);
            }
        });

        // rlAddCategory-新增分类
        rlAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtils.checkNotLoginAndToast()) {
                    return;
                }
                if (Global.mCurrentCategory == null) {
                    ToastUtils.show("未选中所属分类");
                    return;
                }
                if (Global.mCurrentCategory.getLevel() == CategoryBean.LEVEL_3) {
                    ToastUtils.show("三级分类下不可再创建子分类");
                    return;
                }
                showCategoryView(CATEGORY_OPTION_ADD);
            }
        });

        // rlCategoryView-新增分类背景面板
        rlCategoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // tvSubmit-新增分类-提交
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtils.checkNotLoginAndToast()) {
                    return;
                }
                String categoryName = etCategoryName.getText().toString();
                if (TextUtils.isEmpty(categoryName)) {
                    ToastUtils.show("请输入分类名称");
                    return;
                }
                switch (mCategoryOption) {
                    default:
                    case CATEGORY_OPTION_NONE:
                        break;
                    case CATEGORY_OPTION_ADD:
                        addCategory(categoryName, Global.mCurrentCategory);
                        break;
                    case CATEGORY_OPTION_MODIFY:
                        modifyCategory(categoryName, Global.mCurrentCategory);
                        break;
                }
            }
        });

        // tvCancel-新增分类-取消
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCategoryView();
            }
        });
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        LogUtils.d(TAG, "initViewPager() called");

        mHomeFragment = new HomeFragment();
        mMineFragment = new MineFragment();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(mHomeFragment);
        mFragmentList.add(mMineFragment);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(mFragmentAdapter);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        viewPager.setCurrentItem(FRAGMENT_HOME);
        switchFragment(FRAGMENT_HOME);
    }

    /**
     * Fragment切换更新起步导航状态
     */
    private void switchFragment(int position) {
        LogUtils.d(TAG, "switchFragment() called with: position = [" + position + "]");

        int colorSelected = getResources().getColor(R.color.color_797979);
        int colorNormal = getResources().getColor(R.color.color_c4c4c4);

        switch (position) {
            case FRAGMENT_HOME:
                ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_main_home_selected_new));
                ivMine.setImageDrawable(getResources().getDrawable(R.drawable.ic_main_mine_normal_new));
                tvHome.setTextColor(colorSelected);
                tvMine.setTextColor(colorNormal);
                break;
            case FRAGMENT_MINE:
                ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_main_home_normal_new));
                ivMine.setImageDrawable(getResources().getDrawable(R.drawable.ic_main_mine_selected_new));
                tvHome.setTextColor(colorNormal);
                tvMine.setTextColor(colorSelected);
                break;
        }
    }

    /**
     * 底部导航点击监听
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogUtils.d(TAG, "onClick() called with: v = [" + v + "]");

            switch (v.getId()) {
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
     * ViewPager切换监听
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
     * Fragment适配器
     */
    public class FragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();

        public FragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList != null ? fragmentList.size() : 0;
        }
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        CommonParam commonParam = new CommonParam();
        commonParam.setData(String.valueOf(AppUtils.getVersionCode(mContext)));

        MyRequestEntity requestEntity = new MyRequestEntity(UrlConfig.apiAppUpgrade);
        requestEntity.setContentWithHeader(ApiRequest.getContent(commonParam));
        RequestSender.get().send(requestEntity, new RequestCallback<UpdateBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int code, UpdateBean updateBean) {
                if (updateBean != null) {
                    if (updateBean.isHasNewApp()) {
                        showUpdateDialog(updateBean);
                    }
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
//                String testUrl = "http://172.16.70.25:8080/download/c_release_A/builds/148/archive/uxinUsedCar/build/outputs/apk/UxinUsedcar_release_148.apk";
//                updateBean.setAppUrl(testUrl);
                UpdateUtils.get().downloadInstallApk(mActivity, updateBean);
            }

            @Override
            public void onCancelAction() {
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 更新分类列表
     */
    public void updateCategoryList(List<CategoryBean> categoryBeanList) {
        mListCategory.clear();
        mListCategory.addAll(CategoryBean.convertToShow(categoryBeanList));

        // 默认选中所有电子书分类
        Global.mCurrentCategory = mListCategory.get(0);
        for (int i = 0; i < mListCategory.size(); i++) {
            mListCategory.get(i).setSelected(i == 0);
        }
        mCategoryAdapter.setList(mListCategory);

        // 同时更新首页书籍展示
        updateBookList(Global.mCurrentCategory);
    }

    /**
     * 开关分类面板
     */
    public void toggleCategoryView() {
        if (dlMain.isDrawerOpen(rlCategory)) {
            dlMain.closeDrawer(rlCategory);
        } else {
            dlMain.openDrawer(rlCategory);
        }
    }

    /**
     * 显示新增编辑分类浮层
     */
    private void showCategoryView(int categoryOption) {
        mCategoryOption = categoryOption;

        rlCategoryView.setVisibility(View.VISIBLE);
        if (mCategoryOption == CATEGORY_OPTION_MODIFY && Global.mCurrentCategory != null) {
            etCategoryName.setText(Global.mCurrentCategory.getCategoryName());
        }
    }

    /**
     * 隐藏新增编辑分类浮层
     */
    private void hideCategoryView() {
        mCategoryOption = CATEGORY_OPTION_NONE;

        rlCategoryView.setVisibility(View.GONE);
        etCategoryName.setText("");
    }

    /**
     * 新增分类
     */
    private void addCategory(String categoryName, CategoryBean parentCategory) {
        CategoryAddParam categoryAddParam = new CategoryAddParam();
        categoryAddParam.setCategoryName(categoryName);
        categoryAddParam.setParentId(parentCategory.getCategoryId());

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiCategoryAdd);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(categoryAddParam));
        RequestSender.get().send(myRequestEntity, new RequestCallback<CategoryBean>() {
            @Override
            public void onStart() {
                showLoading(true);
            }

            @Override
            public void onSuccess(int code, CategoryBean categoryBean) {
                showLoading(false);
                if (categoryBean != null) {
                    int parentIndex = mListCategory.indexOf(parentCategory);
                    categoryBean.setLevel(parentCategory.getLevel() + 1);

                    mListCategory.add(parentIndex + 1, categoryBean);
                    mCategoryAdapter.notifyDataSetChanged();

                    ToastUtils.show("新增分类成功 | " + categoryBean.getCategoryId() + " | " + categoryBean.getCategoryName());
                    hideCategoryView();
                } else {
                    ToastUtils.show("新增分类失败");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                showLoading(false);
                ToastUtils.show(msg);
            }
        });
    }

    /**
     * 删除分类
     */
    private void deleteCategory(CategoryBean categoryBean, int position) {
        CommonParam commonParam = new CommonParam();
        commonParam.setData(categoryBean.getCategoryId());

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiCategoryDel);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(commonParam));
        RequestSender.get().send(myRequestEntity, new RequestCallback<Object>() {
            @Override
            public void onStart() {
                showLoading(true);
            }

            @Override
            public void onSuccess(int code, Object object) {
                showLoading(false);
                if (object != null) {
                    mListCategory.remove(position);
                    mCategoryAdapter.notifyDataSetChanged();

                    ToastUtils.show("删除分类成功 | " + categoryBean.getCategoryId());
                } else {
                    ToastUtils.show("删除分类失败");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                showLoading(false);
                ToastUtils.show(msg);
            }
        });
    }

    /**
     * 修改分类
     */
    private void modifyCategory(String categoryName, CategoryBean selectedCategory) {
        CategoryEditParam categoryEditParam = new CategoryEditParam();
        categoryEditParam.setCategoryId(selectedCategory.getCategoryId());
        categoryEditParam.setCategoryName(categoryName);

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiCategoryEdit);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(categoryEditParam));
        RequestSender.get().send(myRequestEntity, new RequestCallback<CategoryBean>() {
            @Override
            public void onStart() {
                showLoading(true);
            }

            @Override
            public void onSuccess(int code, CategoryBean categoryBean) {
                showLoading(false);
                if (categoryBean != null) {
                    int selectedIndex = mListCategory.indexOf(selectedCategory);
                    selectedCategory.setCategoryName(categoryBean.getCategoryName());

                    mListCategory.set(selectedIndex, selectedCategory);
                    mCategoryAdapter.notifyDataSetChanged();

                    ToastUtils.show("修改分类成功 | " + categoryBean.getCategoryId() + " | " + categoryBean.getCategoryName());
                    hideCategoryView();
                } else {
                    ToastUtils.show("修改分类失败");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                showLoading(false);
                ToastUtils.show(msg);
            }
        });
    }

    /**
     * 获取用户的所有分类
     */
    public void getCategoryList() {
        CommonParam commonParam = new CommonParam();
        commonParam.setData(EncodeHelper.getRandomChar());

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiCategoryAll);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(commonParam));
        RequestSender.get().send(myRequestEntity, new RequestCallback<List<CategoryBean>>() {
            @Override
            public void onStart() {
                showLoading(true);
            }

            @Override
            public void onSuccess(int code, List<CategoryBean> categoryBeanList) {
                showLoading(false);
                if (categoryBeanList != null) {
                    DBHelper.setCategoryBeanList(Global.mCurrentUser.getUid(), categoryBeanList);
                    updateCategoryList(categoryBeanList);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                showLoading(false);
                ToastUtils.show(msg);
            }
        });
    }

    /**
     * HomeFragment-更新电子书列表
     */
    public void updateBookList(CategoryBean categoryBean) {
        if (mHomeFragment != null) {
            mHomeFragment.updateBookList(categoryBean);
        }
    }

    /**
     * HomeFragment-同步服务端数据
     */
    public void syncServerData() {
        if (mHomeFragment != null) {
            mHomeFragment.syncServerData();
        }
    }
}


