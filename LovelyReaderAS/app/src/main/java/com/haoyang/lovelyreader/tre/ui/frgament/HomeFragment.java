package com.haoyang.lovelyreader.tre.ui.frgament;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import com.haoyang.lovelyreader.tre.ui.FileActivity;
import com.haoyang.lovelyreader.tre.ui.MainActivity;
import com.haoyang.lovelyreader.tre.util.BookInfoUtils;
import com.haoyang.lovelyreader.tre.util.FileUtils;
import com.haoyang.lovelyreader.tre.util.Utils;
import com.haoyang.lovelyreader.tre.wifi.PopupMenuDialog;
import com.haoyang.lovelyreader.tre.wifi.WebService;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.service.bookservice.BookInfoService;
import com.hwangjr.rxbus.RxBus;
import com.java.common.service.file.FileNameService;
import com.mjiayou.trecorelib.bean.entity.TCMenu;
import com.mjiayou.trecorelib.dialog.DialogHelper;
import com.mjiayou.trecorelib.dialog.TCAlertDialog;
import com.mjiayou.trecorelib.event.UserLoginStatusEvent;
import com.mjiayou.trecorelib.http.RequestSender;
import com.mjiayou.trecorelib.http.callback.RequestCallback;
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
    private ImageView ivSearch;
    private EditText etSearch;
    private ImageView ivDelete;
    private TextView tvTemp1;
    private GridView gvBook;
    private ListView lvSearch;
    private ImageView ivAdd;

    private HomeAdapter mHomeAdapter;
//    private List<BookBean> mListBook = new ArrayList<>();
//    private List<BookBean> mListBookAll = new ArrayList<>();

    private LinkedHashMap<String, BookBean> mMapBookShow = new LinkedHashMap<>();
    private LinkedHashMap<String, BookBean> mMapBookAll = new LinkedHashMap<>();

    private boolean mIsFromLogin = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        RxBus.get().register(this);
        View view = inflater.inflate(R.layout.fragment_home, null);

        // findViewById
        ivCategory = (ImageView) view.findViewById(R.id.ivCategory);
        ivSearch = (ImageView) view.findViewById(R.id.ivSearch);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        tvTemp1 = (TextView) view.findViewById(R.id.tvTemp1);
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
        RxBus.get().unregister(this);
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

        // etSearch
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
                if (mHomeAdapter != null) {
                    mHomeAdapter.setList(convertBookBeanList(mMapBookShow));
                }
            }
        });

        // ivDelete
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                etSearch.clearFocus();
            }
        });

        // tvTemp1
        tvTemp1.setText("showTestDialog");
        tvTemp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTestDialog();
            }
        });

        // gvBook
        mHomeAdapter = new HomeAdapter(mContext, convertBookBeanList(mMapBookShow));
        gvBook.setAdapter(mHomeAdapter);
        gvBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d(TAG, "onItemClick() called with: parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");

                BookBean bookBean = convertBookBeanList(mMapBookShow).get(position);
                if (bookBean == null
                        || bookBean.getBookLocalInfo() == null
                        || TextUtils.isEmpty(bookBean.getBookLocalInfo().getLocalBookPath())
                        || TextUtils.isEmpty(bookBean.getBookLocalInfo().getLocalCoverPath())
                        || bookBean.getBookLocalInfo().getBook() == null) {
                    ToastUtils.show("书文件不存在，请下载");
                    return;
                }

                Book book = bookBean.getBookLocalInfo().getBook();
                book.bookCover = bookBean.getBookLocalInfo().getLocalCoverPath();

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
                                    deleteBookFromServer(bookBean);
                                }

                                @Override
                                public void onCancelAction() {
                                }
                            }).show();
                }
                return true;
            }
        });

        // ivAdd
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, FileActivity.class), REQUEST_CODE_ADD_BOOK);
            }
        });
        ivAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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

//    @Subscribe(tags = {@Tag(Constants.RxBusEventType.POPUP_MENU_DIALOG_SHOW_DISMISS)})
//    public void onPopupMenuDialogDismiss(Integer type) {
//        WebService.stop(mContext);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivAdd, "translationY", ivAdd.getHeight() * 2, 0).setDuration(200L);
//        objectAnimator.setInterpolator(new AccelerateInterpolator());
//        objectAnimator.start();
//    }

//    @Subscribe(thread = EventThread.IO, tags = {@Tag(Constants.RxBusEventType.LOAD_BOOK_LIST)})
//    public void loadBookList(Integer type) {
//        Timber.d("loadBookList:" + Thread.currentThread().getName());
//        List<String> books = new ArrayList<>();
//        File dir = new File(Configs.DIR_SDCARD_PROJECT_BOOK);
//        if (dir.exists() && dir.isDirectory()) {
//            String[] fileNames = dir.list();
//            if (fileNames != null) {
//                for (String fileName : fileNames) {
//                    books.add(fileName);
//                }
//            }
//        }
//        mActivity.runOnUiThread(() -> {
//            LogUtils.d(TAG, ConvertUtils.parseString(books, "\n"));
////            mBooks.clear();
////            mBooks.addAll(books);
////            mHomeAdapter.setList(mListBook);
//        });
//    }

    /**
     * onEvent
     */
    public void onEvent(UserLoginStatusEvent event) {
        LogUtils.d(TAG, "onEvent() called with: event = [" + event + "]");
        mIsFromLogin = true;
        initView();
    }

    /**
     * onEvent
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
     * onEvent
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
            syncBookListFromServer();
        }
    }

    /**
     * onAddBook
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
        bookBean.getBookServerInfo().setBookId(BookBean.NO_UPLOAD_BOOK_ID);
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
            addBookToServer(bookBean);
        } else {
            mMapBookAll.put(bookBean.getBookServerInfo().getBookId(), bookBean);
            DBHelper.setBookBeanList(Global.mCurrentUser.getUid(), mMapBookAll);

            mMapBookShow.put(bookBean.getBookServerInfo().getBookId(), bookBean);
            mHomeAdapter.setList(convertBookBeanList(mMapBookShow));
        }
    }


    /**
     * syncGuestBook
     */
    private void syncGuestBook() {
        // 游客用户
        UserBean userBeanDefault = UserBean.getDefault();
        // 游客用户添加的书
        LinkedHashMap<String, BookBean> bookBeanListDefault = DBHelper.getBookBeanList(userBeanDefault.getUid());

        // 当前用户
        UserBean userBean = DBHelper.getUserBean();
        // 当前用户添加的书
        LinkedHashMap<String, BookBean> bookBeanList = DBHelper.getBookBeanList(userBean.getUid());

        // 待上传的书
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
                bookBeanListUnAdd.add(bookBeanDefault);
            }
        }

        for (int i = 0; i < bookBeanListUnAdd.size(); i++) {
            addBookToServer(bookBeanListUnAdd.get(i));
        }

        // 清空游客数据
        DBHelper.setBookBeanList(userBeanDefault.getUid(), new LinkedHashMap<>());
    }

    /**
     * 新增电子书
     */
    public void addBookToServer(BookBean bookBean) {
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
                    mHomeAdapter.setList(convertBookBeanList(mMapBookShow));
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
    public void deleteBookFromServer(BookBean bookBean) {
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
                    mHomeAdapter.setList(convertBookBeanList(mMapBookShow));
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
    public void syncBookListFromServer() {
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
                    BookBean bookBean = new BookBean();
                    BookBean.BookServerInfo bookServerInfo = bookServerInfoList.get(i);
                    if (Boolean.valueOf(bookServerInfo.getIsDel())) { // 删除的书
                        if (mMapBookAll.containsKey(bookServerInfo.getBookId())) {
                            mMapBookAll.remove(bookServerInfo.getBookId());
                        }
                        if (mMapBookShow.containsKey(bookServerInfo.getBookId())) {
                            mMapBookShow.remove(bookServerInfo.getBookId());
                        }
                    } else {
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
                            bookBean.setBookServerInfo(bookServerInfo);
                        }

                        // 如果当前用户已有此书，则更新数据；否则添加此书
                        if (mMapBookAll.containsKey(bookServerInfo.getBookId())) {
                            BookBean bookBeanOld = mMapBookAll.get(bookServerInfo.getBookId());
                            bookBeanOld.setBookServerInfo(bookServerInfo);
                            mMapBookAll.put(bookServerInfo.getBookId(), bookBeanOld);

                            if (mMapBookShow.containsKey(bookServerInfo.getBookId())) {
                                mMapBookShow.put(bookServerInfo.getBookId(), bookBeanOld);
                            }
                        } else {
                            mMapBookAll.put(bookServerInfo.getBookId(), bookBean);
                            mMapBookShow.put(bookServerInfo.getBookId(), bookBean);
                        }
                    }
                }
                DBHelper.setBookBeanList(Global.mCurrentUser.getUid(), mMapBookAll);
                DBHelper.setLastSyncDate(Global.mCurrentUser.getUid(), System.currentTimeMillis());

                mHomeAdapter.setList(convertBookBeanList(mMapBookShow));

                // 如果来自刚登录的初始化，还需要同步游客数据
                if (mIsFromLogin) {
                    syncGuestBook();
                    mIsFromLogin = false;
                }

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
     * showTestDialog
     */
    public void showTestDialog() {
        List<TCMenu> tcMenus = new ArrayList<>();
        tcMenus.add(new TCMenu("同步电子书以及分类", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncBookListFromServer();
            }
        }));
        DialogHelper.createTCAlertMenuDialog(mContext, "测试", "接口测试", true, tcMenus).show();
    }

    /**
     * updateBookList
     */
    public void updateBookList(CategoryBean categoryBean) {
        mMapBookShow.clear();
        if (categoryBean.getCategoryId().equals(CategoryBean.CATEGORY_ROOT_ID)) {
            mMapBookShow.putAll(mMapBookAll);
        } else {
            for (Map.Entry<String, BookBean> entry : mMapBookAll.entrySet()) {
                BookBean bookBean = entry.getValue();
                if (bookBean != null && bookBean.getBookServerInfo().getCategoryId().equals(categoryBean.getCategoryId())) {
                    mMapBookShow.put(bookBean.getBookServerInfo().getBookId(), bookBean);
                }
            }
        }
        mHomeAdapter.setList(convertBookBeanList(mMapBookShow));
    }

    /**
     * MainActivity-getCategoryList
     */
    private void getCategoryList() {
        if (mActivity != null && mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).getCategoryList();
        }
    }

    /**
     * MainActivity-updateCategoryList
     */
    private void updateCategoryList(List<CategoryBean> categoryBeanList) {
        if (mActivity != null && mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).updateCategoryList(categoryBeanList);
        }
    }

    /**
     * MainActivity-toggleDrawer
     */
    private void toggleDrawer() {
        if (mActivity != null && mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).toggleDrawer();
        }
    }

    /**
     * MainActivity-showLoading
     */
    private void showLoading(boolean show) {
        if (mActivity != null && mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).showLoading(show);
        }
    }

    /**
     * convertBookBeanList
     */
    private List<BookBean> convertBookBeanList(LinkedHashMap<String, BookBean> bookBeanMap) {
        List<BookBean> bookBeanList = new ArrayList<>();
        bookBeanList.addAll(bookBeanMap.values());
        return bookBeanList;
    }
}
