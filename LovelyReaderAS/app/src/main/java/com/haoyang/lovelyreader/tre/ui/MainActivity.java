package com.haoyang.lovelyreader.tre.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseActivity;
import com.haoyang.lovelyreader.tre.bean.api.AppUpgradeRequest;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.ui.frgament.HomeFragment;
import com.haoyang.lovelyreader.tre.ui.frgament.MineFragment;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.http.okhttp.RequestBuilder;
import com.mjiayou.trecorelib.http.okhttp.RequestCallback;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.UserUtils;

import java.util.ArrayList;
import java.util.List;

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
}


