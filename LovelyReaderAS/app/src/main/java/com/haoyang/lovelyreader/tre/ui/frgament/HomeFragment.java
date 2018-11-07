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
import java.util.List;

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
    private List<BookBean> mListBook = new ArrayList<>();
    private List<BookBean> mListBookAll = new ArrayList<>();

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
                    mListBook.clear();
                    mListBook.addAll(DBHelper.getBookBeanList(Global.mCurrentUser.getUid()));
                } else {
                    mListBook.clear();
                    mListBook.addAll(DBHelper.getBookBeanListByKey(Global.mCurrentUser.getUid(), key));
                }
                if (mHomeAdapter != null) {
                    mHomeAdapter.setList(mListBook);
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
        mHomeAdapter = new HomeAdapter(mContext, mListBook);
        gvBook.setAdapter(mHomeAdapter);
        gvBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d(TAG, "onItemClick() called with: parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");

                BookBean bookBean = mListBook.get(position);
                if (bookBean == null
                        || TextUtils.isEmpty(bookBean.getLocalBookPath())
                        || TextUtils.isEmpty(bookBean.getLocalCoverPath())
                        || bookBean.getBook() == null) {
                    ToastUtils.show("书文件不存在，请下载");
                    return;
                }

                Book book = bookBean.getBook();
                book.bookCover = bookBean.getLocalCoverPath();

                ToastUtils.show("正在打开书籍...");
                ReaderHelper.startReader(mActivity, book, Global.mCurrentUser);
            }
        });
        gvBook.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d(TAG, "onItemLongClick() called with: parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");

                BookBean bookBean = mListBook.get(position);
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
        List<BookBean> bookBeanList = DBHelper.getBookBeanList(Global.mCurrentUser.getUid());
        mListBookAll.clear();
        mListBookAll.addAll(bookBeanList);

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

        BookBean bookBean = new BookBean();
        bookBean.setFileName(fileBean.getName());
        bookBean.setFileSuffix(fileBean.getSuffix());
        bookBean.setLocalBookPath(fileBean.getPath());

        // 查询该书是否已添加
        for (int i = 0; i < mListBook.size(); i++) {
            if (!TextUtils.isEmpty(mListBook.get(i).getLocalBookPath())
                    && mListBook.get(i).getLocalBookPath().equals(bookBean.getLocalBookPath())) {
                ToastUtils.show(bookBean.getFileName() + bookBean.getFileSuffix() + "-这本书已经添加过了");
                return;
            }
        }

        // bookInfoService操作
        BookInfoService bookInfoService = new BookInfoService();
        bookInfoService.init(bookBean.getLocalBookPath());
        Book book = BookInfoUtils.getBookInfo(bookInfoService, bookBean.getLocalBookPath());
        String localCoverPath = BookInfoUtils.getBookCover(bookInfoService, bookBean.getLocalBookPath());
        bookInfoService.clear();

        bookBean.setBook(book);
        bookBean.setLocalCoverPath(localCoverPath); // 读取封面图片
        bookBean.setAuthor(book.authors);
        bookBean.setBookName(book.bookName);
        bookBean.setBookCategory("");
        bookBean.setBookDesc("");
        bookBean.setCategoryId(Global.mCurrentCategory.getCategoryId());

        // 移动到book文件夹下，并且以文件的md5命名
        String md5 = Utils.getFileMD5(new File(fileBean.getPath()));
        String fileName = Utils.getBookName(Global.mCurrentUser, bookBean);
        String localBookPath = Configs.DIR_SDCARD_PROJECT_BOOK + "/" + fileName;
        FileUtils.copyFile(fileBean.getPath(), localBookPath);
        bookBean.setLocalBookPath(localBookPath);

        // 如果已登录，则添加到服务端；如果未登录，则只添加到本地
        if (UserUtils.checkLoginStatus()) {
            addBookToServer(bookBean);
        } else {
            mListBook.add(0, bookBean);
            mHomeAdapter.setList(mListBook);
            DBHelper.setBookBeanList(Global.mCurrentUser.getUid(), mListBook);
        }
    }


    /**
     * syncGuestBook
     */
    private void syncGuestBook() {
        // 游客用户
        UserBean userBeanDefault = UserBean.getDefault();
        // 游客用户添加的书
        List<BookBean> bookBeanListDefault = DBHelper.getBookBeanList(userBeanDefault.getUid());

        // 当前用户
        UserBean userBean = DBHelper.getUserBean();
        // 当前用户添加的书
        List<BookBean> bookBeanList = DBHelper.getBookBeanList(userBean.getUid());

        // 待上传的书
        List<BookBean> bookBeanListUnAdd = new ArrayList<>();

        // 合并
        for (int i = 0; i < bookBeanListDefault.size(); i++) {
            BookBean bookBeanDefault = bookBeanListDefault.get(i);
            boolean hasThisBook = false;
            for (int j = 0; j < bookBeanList.size(); j++) {
                BookBean bookBean = bookBeanList.get(j);
                if (!TextUtils.isEmpty(bookBeanDefault.getLocalBookPath())
                        && !TextUtils.isEmpty(bookBean.getLocalBookPath())
                        && bookBeanDefault.getLocalBookPath().equals(bookBean.getLocalBookPath())) {
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
        DBHelper.setBookBeanList(userBeanDefault.getUid(), new ArrayList<>());
    }

    /**
     * 新增电子书
     */
    public void addBookToServer(BookBean bookBean) {
        BookAddParam bookAddParam = new BookAddParam();
        bookAddParam.setAuthor(bookBean.getAuthor()); // 作者
        bookAddParam.setBookCategory(bookBean.getBookCategory()); // 图书目录
        bookAddParam.setBookDesc(bookBean.getBookDesc()); // 图书简介
        bookAddParam.setBookName(bookBean.getBookName()); // 电子书名称
        bookAddParam.setCategoryId(bookBean.getCategoryId()); // 分类ID

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiBookAdd);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(bookAddParam));
        RequestSender.get().send(myRequestEntity, new RequestCallback<BookBean>() {
            @Override
            public void onStart() {
                showLoading(true);
            }

            @Override
            public void onSuccess(int code, BookBean bookSyncBean) {
                showLoading(false);
                if (bookSyncBean != null) {
                    ToastUtils.show("新增电子书成功 | " + bookSyncBean.getBookId() + " - " + bookSyncBean.getBookName());

                    bookBean.setBookId(bookSyncBean.getBookId());
                    mListBook.add(0, bookBean);
                    mHomeAdapter.setList(mListBook);

                    mListBookAll.add(0, bookBean);
                    DBHelper.setBookBeanList(Global.mCurrentUser.getUid(), mListBookAll);
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
        commonParam.setData(bookBean.getBookId());

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
                    mListBook.remove(bookBean);
                    mHomeAdapter.setList(mListBook);
                    DBHelper.delBookBean(Global.mCurrentUser.getUid(), bookBean.getBookId());
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
        RequestSender.get().send(myRequestEntity, new RequestCallback<List<BookBean>>() {
            @Override
            public void onStart() {
                showLoading(true);
            }

            @Override
            public void onSuccess(int code, List<BookBean> bookBeanList) {
                showLoading(false);
                for (int i = bookBeanList.size() - 1; i >= 0; i--) {
                    BookBean bookBean = bookBeanList.get(i);
                    if (Boolean.valueOf(bookBean.getIsDel())) { // 删除的书
                        // TODO
                    } else {
                        String bookFileDir = Configs.DIR_SDCARD_PROJECT_BOOK;
                        String bookFileName = Utils.getBookName(DBHelper.getUserBean(), bookBean);
                        File bookFile = new File(bookFileDir, bookFileName);
                        if (bookFile.exists()) {
                            String filePath = bookFile.getAbsolutePath();
                            FileNameService fileNameService = new FileNameService();
                            String fileName = fileNameService.getFileName(filePath);
                            String fileSuffix = fileNameService.getFileExtendName(filePath);
                            bookBean.setFileName(fileName);
                            bookBean.setFileSuffix(fileSuffix);
                            bookBean.setLocalBookPath(filePath);
                            BookInfoService bookInfoService = new BookInfoService();
                            bookInfoService.init(bookBean.getLocalBookPath());
                            Book book = BookInfoUtils.getBookInfo(bookInfoService, bookBean.getLocalBookPath());
                            String localCoverPath = BookInfoUtils.getBookCover(bookInfoService, bookBean.getLocalBookPath());
                            bookInfoService.clear();
                            bookBean.setBook(book);
                            bookBean.setLocalCoverPath(localCoverPath);
                        }
                        mListBookAll.add(0, bookBean);
                    }
                }
                DBHelper.setBookBeanList(Global.mCurrentUser.getUid(), mListBookAll);
                DBHelper.setLastSyncDate(Global.mCurrentUser.getUid(), System.currentTimeMillis());

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
        mListBook.clear();
        if (categoryBean.getCategoryId().equals(CategoryBean.CATEGORY_ROOT_ID)) {
            mListBook.addAll(mListBookAll);
        } else {
            for (int i = 0; i < mListBookAll.size(); i++) {
                BookBean bookBean = mListBookAll.get(i);
                if (bookBean != null && bookBean.getCategoryId().equals(categoryBean.getCategoryId())) {
                    mListBook.add(bookBean);
                }
            }
        }
        mHomeAdapter.setList(mListBook);
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
}
