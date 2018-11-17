package com.haoyang.lovelyreader.tre.ui.frgament;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseFragment;
import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.haoyang.lovelyreader.tre.bean.CategoryBean;
import com.haoyang.lovelyreader.tre.bean.FileBean;
import com.haoyang.lovelyreader.tre.bean.ResponseBean;
import com.haoyang.lovelyreader.tre.bean.UpdateBean;
import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.BookAddParam;
import com.haoyang.lovelyreader.tre.bean.api.BookSyncParam;
import com.haoyang.lovelyreader.tre.bean.api.CommonParam;
import com.haoyang.lovelyreader.tre.event.OnWifiDialogDismissEvent;
import com.haoyang.lovelyreader.tre.helper.Configs;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.Global;
import com.haoyang.lovelyreader.tre.helper.OnBookAddEvent;
import com.haoyang.lovelyreader.tre.helper.ReaderHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.http.MyRequestEntity;
import com.haoyang.lovelyreader.tre.http.RequestCallback;
import com.haoyang.lovelyreader.tre.ui.FileActivity;
import com.haoyang.lovelyreader.tre.ui.MainActivity;
import com.haoyang.lovelyreader.tre.ui.adapter.BookAdapter;
import com.haoyang.lovelyreader.tre.util.BookInfoUtils;
import com.haoyang.lovelyreader.tre.util.FileUtils;
import com.haoyang.lovelyreader.tre.util.Utils;
import com.haoyang.lovelyreader.tre.wifi.PopupMenuDialog;
import com.haoyang.lovelyreader.tre.wifi.WebService;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.service.bookservice.BookInfoService;
import com.java.common.service.file.FileNameService;
import com.mjiayou.trecorelib.bean.entity.TCMenu;
import com.mjiayou.trecorelib.dialog.DialogHelper;
import com.mjiayou.trecorelib.dialog.TCAlertDialog;
import com.mjiayou.trecorelib.event.UserLoginStatusEvent;
import com.mjiayou.trecorelib.http.RequestSender;
import com.mjiayou.trecorelib.http.callback.ObjectCallback;
import com.mjiayou.trecorelib.http.callback.StringCallback;
import com.mjiayou.trecorelib.json.JsonParser;
import com.mjiayou.trecorelib.util.AppUtils;
import com.mjiayou.trecorelib.util.KeyBoardUtils;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.ToastUtils;
import com.mjiayou.trecorelib.util.UserUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by xin on 18/9/22.
 */
public class HomeFragment extends BaseFragment {

    private final int REQUEST_CODE_ADD_BOOK = 102;

    private ImageView ivCategory;
    private TextView tvSync;
    private ImageView ivSearch;
    private EditText etSearch;
    private ImageView ivDelete;
    private GridView gvBook;
    private ListView lvSearch;
    private ImageView ivAdd;

    private BookAdapter mBookAdapter;
    private LinkedHashMap<String, BookBean> mMapBookShow = new LinkedHashMap<>();
    private LinkedHashMap<String, BookBean> mMapBookAll = new LinkedHashMap<>();

    private boolean mIsFromLogin = false;
    private boolean mIsFromSyncBook = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_home, null);

        // findViewById
        ivCategory = (ImageView) view.findViewById(R.id.ivCategory);
        tvSync = (TextView) view.findViewById(R.id.tvSync);
        ivSearch = (ImageView) view.findViewById(R.id.ivSearch);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        gvBook = (GridView) view.findViewById(R.id.gvBook);
        lvSearch = (ListView) view.findViewById(R.id.lvSearch);
        ivAdd = (ImageView) view.findViewById(R.id.ivAdd);

        initView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebService.stop(mContext);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_BOOK:
                    ArrayList<FileBean> fileBeanArrayList = data.getParcelableArrayListExtra(FileActivity.EXTRA_FILE_LIST);
                    if (fileBeanArrayList == null) {
                        return;
                    }

                    for (int i = 0; i < fileBeanArrayList.size(); i++) {
                        FileBean fileBean = fileBeanArrayList.get(i);
                        if (fileBean != null) {
                            onAddBook(fileBean);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void initView() {
        // mUserBean
        Global.mCurrentUser = DBHelper.getUserBean();

        // ivCategory-打开分类
        ivCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });
        ivCategory.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showTestDialog();
                return true;
            }
        });

        // tvSync
        tvSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncServerData();
            }
        });

        // etSearch-搜索框
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = s.toString();
                if (TextUtils.isEmpty(key)) {
                    mMapBookShow.clear();
                    mMapBookShow.putAll(DBHelper.getBookBeanList(Global.mCurrentUser.getUid()));
                } else {
                    mMapBookShow.clear();
                    mMapBookShow.putAll(DBHelper.getBookBeanListByKey(Global.mCurrentUser.getUid(), key));
                }
                if (mBookAdapter != null) {
                    mBookAdapter.setList(convertBookBeanList(mMapBookShow));
                }
            }
        });

        // ivDelete-清除搜索框
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                etSearch.clearFocus();
            }
        });

        // gvBook
        mBookAdapter = new BookAdapter(mContext, convertBookBeanList(mMapBookShow));
        gvBook.setAdapter(mBookAdapter);
        gvBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d(TAG, "onItemClick() called with: parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");

                BookBean bookBean = convertBookBeanList(mMapBookShow).get(position);
                if (bookBean == null
                        || bookBean.getBookLocalInfo() == null
                        || bookBean.getBookLocalInfo().getBook() == null
                        || TextUtils.isEmpty(bookBean.getBookLocalInfo().getLocalBookPath())) {
                    ToastUtils.show("电子书文件不存在");
                    return;
                }

                Book book = bookBean.getBookLocalInfo().getBook();

                ToastUtils.show("正在打开书籍...");
                ReaderHelper.startReader(mActivity, book, Global.mCurrentUser);
            }
        });
        gvBook.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d(TAG, "onItemLongClick() called with: parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");

                BookBean bookBean = convertBookBeanList(mMapBookShow).get(position);
                if (bookBean != null) {
                    DialogHelper.createTCAlertDialog(mContext, "提示", "确定要删除？", "确定", "取消", true,
                            new TCAlertDialog.OnTCActionListener() {
                                @Override
                                public void onOkAction() {
                                    deleteBook(bookBean);
                                }

                                @Override
                                public void onCancelAction() {
                                }
                            }).show();
                }
                return true;
            }
        });
        gvBook.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (etSearch.hasFocus()) {
                    etSearch.clearFocus();
                    KeyBoardUtils.hide(mContext, etSearch);
                }
                return false;
            }
        });

        // ivAdd
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserUtils.checkLoginStatus() && Global.mCurrentCategory.getCategoryId().equals(CategoryBean.CATEGORY_ROOT_ID)) {
                    ToastUtils.show("请先选中所属分类");
                    return;
                }
                startActivityForResult(new Intent(mContext, FileActivity.class), REQUEST_CODE_ADD_BOOK);
            }
        });
        ivAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (UserUtils.checkLoginStatus() && Global.mCurrentCategory.getCategoryId().equals(CategoryBean.CATEGORY_ROOT_ID)) {
                    ToastUtils.show("请先选中所属分类");
                    return false;
                }

//                startActivity(new Intent(mContext, com.haoyang.lovelyreader.ui.MainActivity.class));
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivAdd, "translationY", 0, ivAdd.getHeight() * 2).setDuration(200L);
                objectAnimator.setInterpolator(new AccelerateInterpolator());
                objectAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        WebService.start(mContext);
                        new PopupMenuDialog(mContext).builder().setCancelable(false).setCanceledOnTouchOutside(false).show();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                objectAnimator.start();
                return false;
            }
        });

        // 展示本地数据
        showLocalData();

        // 同步服务端数据
        syncServerData();
    }

    /**
     * onEvent-登陆
     */
    public void onEvent(UserLoginStatusEvent event) {
        LogUtils.d(TAG, "onEvent() called with: event = [" + event + "]");
        mIsFromLogin = true;
        initView();
    }

    /**
     * onEvent-添加电子书
     */
    public void onEvent(OnBookAddEvent event) {
        LogUtils.d(TAG, "onEvent() called with: event = [" + event + "]");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FileBean fileBean = event.getFileBean();
                if (fileBean != null) {
                    onAddBook(fileBean);
                }
            }
        });
    }

    /**
     * onEvent-隐藏wifi传输弹窗
     */
    public void onEvent(OnWifiDialogDismissEvent onWifiDialogDismissEvent) {
        if (onWifiDialogDismissEvent != null && onWifiDialogDismissEvent.isDismiss()) {
            WebService.stop(mContext);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivAdd, "translationY", ivAdd.getHeight() * 2, 0).setDuration(200L);
            objectAnimator.setInterpolator(new AccelerateInterpolator());
            objectAnimator.start();
        }
    }

    /**
     * 展示本地数据
     */
    private void showLocalData() {
        // 本地数据-电子书
        LinkedHashMap<String, BookBean> bookBeanMap = DBHelper.getBookBeanList(Global.mCurrentUser.getUid());
        mMapBookAll.clear();
        mMapBookAll.putAll(bookBeanMap);

        // 本地数据-分类
        List<CategoryBean> categoryBeanList = DBHelper.getCategoryBeanList(Global.mCurrentUser.getUid());
        updateCategoryList(categoryBeanList);
    }

    /**
     * 同步服务端数据
     */
    public void syncServerData() {
        // 如果用户已登录，则同步电子书
        if (UserUtils.checkLoginStatus()) {
            mIsFromSyncBook = true;
            syncBookList();
        }
    }

    /**
     * 添加电子书
     */
    public void onAddBook(FileBean fileBean) {
        if (fileBean == null) {
            return;
        }

        // 查询该书是否已添加
        List<BookBean> bookBeanList = convertBookBeanList(mMapBookAll);
        for (int i = 0; i < bookBeanList.size(); i++) {
            if (!TextUtils.isEmpty(bookBeanList.get(i).getBookLocalInfo().getLocalBookPath())
                    && bookBeanList.get(i).getBookLocalInfo().getLocalBookPath().equals(fileBean.getPath())) {
                ToastUtils.show(fileBean.getName() + fileBean.getSuffix() + "-这本书已经添加过了");
                return;
            }
        }

        // bookInfoService操作
        String filePath = fileBean.getPath();
        BookInfoService bookInfoService = new BookInfoService();
        bookInfoService.init(filePath);
        Book book = BookInfoUtils.getBookInfo(bookInfoService, filePath);
        String localCoverPath = BookInfoUtils.getBookCover(bookInfoService, filePath);
        bookInfoService.clear();

        // new BookBean
        BookBean bookBean = new BookBean();
        bookBean.getBookLocalInfo().setFileName(fileBean.getName());
        bookBean.getBookLocalInfo().setFileSuffix(fileBean.getSuffix());
        bookBean.getBookLocalInfo().setLocalBookPath(fileBean.getPath());
        bookBean.getBookLocalInfo().setLocalCoverPath(localCoverPath); // 读取封面图片
        bookBean.getBookLocalInfo().setBook(book);
        bookBean.getBookServerInfo().setBookId(BookBean.NO_UPLOAD_BOOK_ID + System.currentTimeMillis());
        bookBean.getBookServerInfo().setAuthor(book.authors);
        bookBean.getBookServerInfo().setBookName(book.bookName);
        bookBean.getBookServerInfo().setBookCategory("");
        bookBean.getBookServerInfo().setBookDesc("");
        bookBean.getBookServerInfo().setCategoryId(Global.mCurrentCategory.getCategoryId());

        // 移动到book文件夹下，并且以文件的md5命名
        String md5 = Utils.getFileMD5(new File(fileBean.getPath()));
        String fileName = Utils.getBookName(Global.mCurrentUser, bookBean.getBookServerInfo());
        String localBookPath = Configs.DIR_SDCARD_PROJECT_BOOK + "/" + fileName;
        FileUtils.copyFile(fileBean.getPath(), localBookPath);
        bookBean.getBookLocalInfo().setLocalBookPath(localBookPath);

        // 如果已登录，则添加到服务端；如果未登录，则只添加到本地
        if (UserUtils.checkLoginStatus()) {
            addBook(bookBean);
        } else {
            mMapBookAll.put(bookBean.getBookServerInfo().getBookId(), bookBean);
            DBHelper.setBookBeanList(Global.mCurrentUser.getUid(), mMapBookAll);

            mMapBookShow.put(bookBean.getBookServerInfo().getBookId(), bookBean);
            mBookAdapter.setList(convertBookBeanList(mMapBookShow));
        }
    }

    /**
     * 同步游客添加的电子书
     */
    private void syncGuestBook() {
        // 游客用户
        UserBean userBeanDefault = UserBean.getDefault();
        // 游客用户添加的书
        LinkedHashMap<String, BookBean> bookBeanListDefault = DBHelper.getBookBeanList(userBeanDefault.getUid());

        if (bookBeanListDefault.size() == 0) {
            return;
        }

        // 当前用户
        UserBean userBean = DBHelper.getUserBean();
        // 当前用户添加的书
        LinkedHashMap<String, BookBean> bookBeanList = DBHelper.getBookBeanList(userBean.getUid());

        // 待上传的书列表
        List<BookBean> bookBeanListUnAdd = new ArrayList<>();

        // 合并
        for (Map.Entry<String, BookBean> entryDefault : bookBeanListDefault.entrySet()) {
            BookBean bookBeanDefault = entryDefault.getValue();
            boolean hasThisBook = false;
            for (Map.Entry<String, BookBean> entry : bookBeanList.entrySet()) {
                BookBean bookBean = entry.getValue();
                if (!TextUtils.isEmpty(bookBeanDefault.getBookLocalInfo().getLocalBookPath())
                        && !TextUtils.isEmpty(bookBean.getBookLocalInfo().getLocalBookPath())
                        && bookBeanDefault.getBookLocalInfo().getLocalBookPath().equals(bookBean.getBookLocalInfo().getLocalBookPath())) {
                    hasThisBook = true;
                    break;
                }
            }
            if (!hasThisBook) {
                // 更改分类id
                bookBeanDefault.getBookServerInfo().setCategoryId(Global.mCurrentCategory.getCategoryId());
                bookBeanListUnAdd.add(bookBeanDefault);
            }
        }

        // 添加电子书
        for (int i = 0; i < bookBeanListUnAdd.size(); i++) {
            addBook(bookBeanListUnAdd.get(i));
        }

        // 清空游客数据
        DBHelper.setBookBeanList(userBeanDefault.getUid(), new LinkedHashMap<>());
    }

    /**
     * 新增电子书
     */
    public void addBook(BookBean bookBean) {
        BookAddParam bookAddParam = new BookAddParam();
        bookAddParam.setAuthor(bookBean.getBookServerInfo().getAuthor()); // 作者
        bookAddParam.setBookCategory(bookBean.getBookServerInfo().getBookCategory()); // 图书目录
        bookAddParam.setBookDesc(bookBean.getBookServerInfo().getBookDesc()); // 图书简介
        bookAddParam.setBookName(bookBean.getBookServerInfo().getBookName()); // 电子书名称
        bookAddParam.setCategoryId(bookBean.getBookServerInfo().getCategoryId()); // 分类ID

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiBookAdd);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(bookAddParam));
        RequestSender.get().send(myRequestEntity, new RequestCallback<BookBean.BookServerInfo>() {
            @Override
            public void onStart() {
                showLoading(true);
            }

            @Override
            public void onSuccess(int code, BookBean.BookServerInfo bookServerInfo) {
                showLoading(false);
                if (bookServerInfo != null) {
                    ToastUtils.show("新增电子书成功 | " + bookServerInfo.getBookId() + " - " + bookServerInfo.getBookName());

                    bookBean.getBookServerInfo().setBookId(bookServerInfo.getBookId());

                    mMapBookAll.put(bookBean.getBookServerInfo().getBookId(), bookBean);
                    DBHelper.setBookBeanList(Global.mCurrentUser.getUid(), mMapBookAll);

                    mMapBookShow.put(bookBean.getBookServerInfo().getBookId(), bookBean);
                    mBookAdapter.setList(convertBookBeanList(mMapBookShow));
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
     * 删除电子书
     */
    public void deleteBook(BookBean bookBean) {
        CommonParam commonParam = new CommonParam();
        commonParam.setData(bookBean.getBookServerInfo().getBookId());

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiBookDel);
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
                    ToastUtils.show("删除电子书成功 | " + bookBean.getBookServerInfo().getBookId() + " - " + bookBean.getBookServerInfo().getBookName());

                    mMapBookAll.remove(bookBean.getBookServerInfo().getBookId());
                    DBHelper.setBookBeanList(Global.mCurrentUser.getUid(), mMapBookAll);

                    mMapBookShow.remove(bookBean.getBookServerInfo().getBookId());
                    mBookAdapter.setList(convertBookBeanList(mMapBookShow));
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
     * 同步电子书
     */
    public void syncBookList() {
        BookSyncParam bookSyncParam = new BookSyncParam();
        long timestamp = DBHelper.getLastSyncDate(Global.mCurrentUser.getUid());
        if (timestamp == 0) {
            bookSyncParam.setSyncType("1");
            bookSyncParam.setLastSyncDate("0");
        } else {
            bookSyncParam.setSyncType("2");
            bookSyncParam.setLastSyncDate(String.valueOf(timestamp));
        }

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiBookSync);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(bookSyncParam));
        RequestSender.get().send(myRequestEntity, new RequestCallback<List<BookBean.BookServerInfo>>() {
            @Override
            public void onStart() {
                showLoading(true);
            }

            @Override
            public void onSuccess(int code, List<BookBean.BookServerInfo> bookServerInfoList) {
                showLoading(false);
                for (int i = bookServerInfoList.size() - 1; i >= 0; i--) {
                    BookBean.BookServerInfo bookServerInfo = bookServerInfoList.get(i);
                    if (Boolean.valueOf(bookServerInfo.getIsDel())) { // 删除的书
                        if (mMapBookAll.containsKey(bookServerInfo.getBookId())) {
                            mMapBookAll.remove(bookServerInfo.getBookId());
                        }
                        if (mMapBookShow.containsKey(bookServerInfo.getBookId())) {
                            mMapBookShow.remove(bookServerInfo.getBookId());
                        }
                    } else { // 更新电子书或新增电子书
                        if (mMapBookAll.containsKey(bookServerInfo.getBookId())) { // 如果当前用户已有此书，则更新电子书
                            BookBean bookBeanOld = mMapBookAll.get(bookServerInfo.getBookId());
                            bookBeanOld.setBookServerInfo(bookServerInfo);
                            mMapBookAll.put(bookServerInfo.getBookId(), bookBeanOld);

                            if (mMapBookShow.containsKey(bookServerInfo.getBookId())) {
                                mMapBookShow.put(bookServerInfo.getBookId(), bookBeanOld);
                            }
                        } else { // 否则没有，则新增的电子书
                            BookBean bookBean = new BookBean();
                            // 设如果本地已有文件，则设置电子书本地存储信息
                            String bookFileDir = Configs.DIR_SDCARD_PROJECT_BOOK;
                            String bookFileName = Utils.getBookName(DBHelper.getUserBean(), bookServerInfo);
                            File bookFile = new File(bookFileDir, bookFileName);
                            if (bookFile.exists()) {
                                String filePath = bookFile.getAbsolutePath();

                                FileNameService fileNameService = new FileNameService();
                                String fileName = fileNameService.getFileName(filePath);
                                String fileSuffix = fileNameService.getFileExtendName(filePath);

                                BookInfoService bookInfoService = new BookInfoService();
                                bookInfoService.init(filePath);
                                Book book = BookInfoUtils.getBookInfo(bookInfoService, filePath);
                                String localCoverPath = BookInfoUtils.getBookCover(bookInfoService, filePath);
                                bookInfoService.clear();

                                bookBean.getBookLocalInfo().setFileName(fileName);
                                bookBean.getBookLocalInfo().setFileSuffix(fileSuffix);
                                bookBean.getBookLocalInfo().setLocalBookPath(filePath);
                                bookBean.getBookLocalInfo().setLocalCoverPath(localCoverPath);
                                bookBean.getBookLocalInfo().setBook(book);
                            }
                            // 设置服务端信息
                            bookBean.setBookServerInfo(bookServerInfo);

                            mMapBookAll.put(bookServerInfo.getBookId(), bookBean);
                            mMapBookShow.put(bookServerInfo.getBookId(), bookBean);
                        }
                    }
                }
                DBHelper.setBookBeanList(Global.mCurrentUser.getUid(), mMapBookAll);
                DBHelper.setLastSyncDate(Global.mCurrentUser.getUid(), System.currentTimeMillis());

                mBookAdapter.setList(convertBookBeanList(mMapBookShow));

                // 同步分类
                getCategoryList();
            }

            @Override
            public void onFailure(int code, String msg) {
                showLoading(false);
                ToastUtils.show(msg);
            }
        });
    }

    /**
     * 更新电子书列表
     */
    public void updateBookList(CategoryBean categoryBean) {
        if (categoryBean == null) {
            return;
        }

        mMapBookShow.clear();
        if (categoryBean.getCategoryId().equals(CategoryBean.CATEGORY_ROOT_ID)) {
            mMapBookShow.putAll(mMapBookAll);
        } else {
            for (Map.Entry<String, BookBean> entry : mMapBookAll.entrySet()) {
                BookBean bookBean = entry.getValue();
                if (bookBean != null && bookBean.getBookServerInfo() != null && bookBean.getBookServerInfo().getCategoryId().equals(categoryBean.getCategoryId())) {
                    mMapBookShow.put(bookBean.getBookServerInfo().getBookId(), bookBean);
                }
            }
        }
        if (mBookAdapter != null) {
            mBookAdapter.setList(convertBookBeanList(mMapBookShow));
        }

        // 如果来自刚登录的初始化，还需要同步游客数据
        if (mIsFromLogin && mIsFromSyncBook) {
            syncGuestBook();
            mIsFromLogin = false;
            mIsFromSyncBook = false;
        }
    }

    /**
     * MainActivity-获取用户的所有分类
     */
    private void getCategoryList() {
        if (mActivity != null && mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).getCategoryList();
        }
    }

    /**
     * MainActivity-更新分类列表
     */
    private void updateCategoryList(List<CategoryBean> categoryBeanList) {
        if (mActivity != null && mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).updateCategoryList(categoryBeanList);
        }
    }

    /**
     * MainActivity-开关分类面板
     */
    private void toggleDrawer() {
        if (mActivity != null && mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).toggleCategoryView();
        }
    }

    /**
     * MainActivity-正在加载
     */
    private void showLoading(boolean show) {
        if (mActivity != null && mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).showLoading(show);
        }
    }

    /**
     * BookBeanMap转BookBeanList
     */
    private List<BookBean> convertBookBeanList(LinkedHashMap<String, BookBean> bookBeanMap) {
        List<BookBean> bookBeanList = new ArrayList<>();
        bookBeanList.addAll(bookBeanMap.values());
        return bookBeanList;
    }

    /**
     * showTestDialog
     */
    public void showTestDialog() {
        List<TCMenu> tcMenus = new ArrayList<>();
        tcMenus.add(new TCMenu("同步数据", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncBookList();
            }
        }));

        CommonParam commonParam = new CommonParam();
        commonParam.setData(String.valueOf(AppUtils.getVersionCode(mContext)));
        MyRequestEntity requestEntity = new MyRequestEntity(UrlConfig.apiAppUpgrade);
        requestEntity.setContentWithHeader(ApiRequest.getContent(commonParam));

        tcMenus.add(new TCMenu("RequestCallback", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestSender.get().send(requestEntity, new RequestCallback<UpdateBean>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(int code, UpdateBean object) {
                        if (object != null) {
                            ToastUtils.show(JsonParser.get().toJson(object));
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        ToastUtils.show(msg);
                    }
                });
            }
        }));
        tcMenus.add(new TCMenu("ObjectCallback", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestSender.get().send(requestEntity, new ObjectCallback<ResponseBean<UpdateBean>>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(int code, ResponseBean<UpdateBean> object) {
                        if (object != null) {
                            ToastUtils.show(JsonParser.get().toJson(object));
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        ToastUtils.show(msg);
                    }
                });
            }
        }));
        tcMenus.add(new TCMenu("StringCallback", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestSender.get().send(requestEntity, new StringCallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(int code, String object) {
                        if (object != null) {
                            ToastUtils.show(object);
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        ToastUtils.show(msg);
                    }
                });
            }
        }));
        tcMenus.add(new TCMenu("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestSender.get().send(requestEntity, new RequestCallback<UpdateBean>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(int code, UpdateBean updateBean) {
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        ToastUtils.show(msg);
                    }
                });
            }
        }));
        DialogHelper.createTCAlertMenuDialog(mContext, "测试", "接口测试", true, tcMenus).show();
    }
}
