package com.haoyang.lovelyreader.tre;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xin on 18/9/22.
 */
public class MainActivity extends BaseActivity {

  private static final int POSITION_HOME = 0;
  private static final int POSITION_MINE = 1;

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

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_new);
    initView();
  }

  /**
   * initView
   */
  @Override protected void initView() {
    Log.d(TAG, "initView() called");

    // findViewById
    viewPager = (ViewPager) findViewById(R.id.viewPager);
    ivAdd = (ImageView) findViewById(R.id.ivAdd);
    llHome = (LinearLayout) findViewById(R.id.llHome);
    ivHome = (ImageView) findViewById(R.id.ivHome);
    tvHome = (TextView) findViewById(R.id.tvHome);
    llMine = (LinearLayout) findViewById(R.id.llMine);
    ivMine = (ImageView) findViewById(R.id.ivMine);
    tvMine = (TextView) findViewById(R.id.tvMine);

    // ivAdd
    ivAdd.setOnClickListener(mOnClickListener);
    // llHome
    llHome.setOnClickListener(mOnClickListener);
    // llMine
    llMine.setOnClickListener(mOnClickListener);

    initViewPager();
  }

  /**
   * initViewPager
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
    viewPager.setCurrentItem(POSITION_HOME);
    updateBottomSelected(POSITION_HOME);
  }

  /**
   * updateBottomSelected
   */
  private void updateBottomSelected(int position) {
    LogUtil.d(TAG, "updateBottomSelected() called with: position = [" + position + "]");

    int colorSelected = getResources().getColor(R.color.app_theme);
    int colorNormal = getResources().getColor(R.color.color_333333);

    switch (position) {
      case POSITION_HOME:
        tvHome.setTextColor(colorSelected);
        tvMine.setTextColor(colorNormal);
        break;
      case POSITION_MINE:
        tvHome.setTextColor(colorNormal);
        tvMine.setTextColor(colorSelected);
        break;
    }
  }

  /**
   * mOnClickListener
   */
  private View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      LogUtil.d(TAG, "onClick() called with: v = [" + v + "]");

      switch (v.getId()) {
        case R.id.ivAdd:
          ToastUtil.show(mContext, "新增");
          break;
        case R.id.llHome:
          viewPager.setCurrentItem(POSITION_HOME);
          break;
        case R.id.llMine:
          viewPager.setCurrentItem(POSITION_MINE);
          break;
      }
    }
  };

  /**
   * mOnPageChangeListener
   */
  private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      //LogUtil.d(TAG, "onPageScrolled() called with: position = [" + position + "], positionOffset = [" + positionOffset + "], positionOffsetPixels = [" + positionOffsetPixels + "]");
    }

    @Override public void onPageSelected(int position) {
      LogUtil.d(TAG, "onPageSelected() called with: position = [" + position + "]");

      updateBottomSelected(position);
    }

    @Override public void onPageScrollStateChanged(int state) {
      LogUtil.d(TAG, "onPageScrollStateChanged() called with: state = [" + state + "]");
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

    @Override public Fragment getItem(int position) {
      return mList.get(position);
    }

    @Override public int getCount() {
      return mList != null ? mList.size() : 0;
    }
  }
}


