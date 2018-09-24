package com.haoyang.lovelyreader.tre;

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

import com.app.base.exception.DeviceException;
import com.app.base.service.android.AndroidInfoService;
import com.haoyang.lovelyreader.FileBrowseActivity;
import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.entity.UserBook;
import com.haoyang.lovelyreader.service.BookService;
import com.haoyang.lovelyreader.service.UserService;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.service.bookservice.BookInfoService;
import com.java.common.utils.Utils;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xin on 18/9/22.
 */
public class MainActivity extends BaseActivity {

    private final int REQUEST_CODE_ADD_BOOK_OLD = 101;
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
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_BOOK:
                    break;
                case REQUEST_CODE_ADD_BOOK_OLD:
                    super.onActivityResult(requestCode, resultCode, data);
                    final Bundle extras = data.getExtras();

                    if (extras == null) {
                        return;
                    }

                    String filePath = (String) extras.get("path");
                    UserBook userBook = new UserBook();
                    Book book = new Book();
                    book.path = filePath;
                    BookInfoService bookInfoService = new BookInfoService();
                    bookInfoService.init(filePath);
                    int result = bookInfoService.getBookInfo(book, "ePub");
                    InputStream is = bookInfoService.getCoverInputStream(filePath);
                    try {
                        if (is != null) {
                            String newFilePath = readBookImage(filePath, is);
                            userBook.coverPath = newFilePath;
                        }
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    bookInfoService.clear();
                    userBook.userId = UserService.user.userId;
                    userBook.bookPath = filePath;
                    userBook.bookName = book.bookName;
                    userBook.times = System.currentTimeMillis();

                    BookService bookService = new BookService(this);
                    UserBook ub = bookService.loadBookByPath(filePath);
                    if (ub != null) { // 提示已经以书架中了，
                        return;
                    }

                    bookService.saveBook(userBook);

//                    this.clearBookLayout();
//                    this.initBookLayout();
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
        viewPager.setCurrentItem(FRAGMENT_HOME);
        updateBottomSelected(FRAGMENT_HOME);
    }

    /**
     * updateBottomSelected
     */
    private void updateBottomSelected(int position) {
        LogUtils.d(TAG, "updateBottomSelected() called with: position = [" + position + "]");

        int colorSelected = getResources().getColor(R.color.app_theme);
        int colorNormal = getResources().getColor(R.color.color_333333);

        switch (position) {
            case FRAGMENT_HOME:
                tvHome.setTextColor(colorSelected);
                tvMine.setTextColor(colorNormal);
                break;
            case FRAGMENT_MINE:
                tvHome.setTextColor(colorNormal);
                tvMine.setTextColor(colorSelected);
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
                    ToastUtils.show("新增");
                    if (new Random().nextBoolean()) {
                        startActivityForResult(new Intent(mContext, FileActivity.class), REQUEST_CODE_ADD_BOOK);
                    } else {
                        startActivity(new Intent(mContext, com.haoyang.lovelyreader.MainActivity.class));
//                        startActivityForResult(new Intent(mContext, FileBrowseActivity.class), REQUEST_CODE_ADD_BOOK_OLD);
                    }
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

            updateBottomSelected(position);
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
     * readBookImage
     */
    private String readBookImage(String filePath, InputStream is) {

        AndroidInfoService androidInfoService = new AndroidInfoService();
        String documentPath;
        try {
            documentPath = androidInfoService.getDownLoadPath(getApplicationContext());
        } catch (DeviceException e1) {
            e1.printStackTrace();
            return "";
        }

        String newPath = documentPath + File.separator + "bookimage" + File.separator;

        File file = new File(newPath);
        if (!file.exists()) {
            boolean r = file.mkdir();
            if (!r) {
                return "";
            }
        }

        String md5Id = Utils.md5(filePath.getBytes());
        newPath += md5Id;

        file = new File(newPath);
        if (file.exists()) {
            file.delete();
        }

        OutputStream out = null;
        try {
            file.createNewFile();
            out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];

            int len = 0;
            while ((len = is.read(buffer)) != -1) {

                out.write(buffer, 0, len);
            } // end while

            return newPath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}


