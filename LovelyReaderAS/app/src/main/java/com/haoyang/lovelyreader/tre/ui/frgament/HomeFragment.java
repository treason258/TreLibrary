package com.haoyang.lovelyreader.tre.ui.frgament;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.base.common.util.Size;
import com.app.base.exception.DeviceException;
import com.app.base.service.android.AndroidInfoService;
import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.base.BaseFragment;
import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.haoyang.lovelyreader.tre.bean.BookSyncBean;
import com.haoyang.lovelyreader.tre.bean.CategoryBean;
import com.haoyang.lovelyreader.tre.bean.FileBean;
import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.BookAddParam;
import com.haoyang.lovelyreader.tre.bean.api.BookSyncParam;
import com.haoyang.lovelyreader.tre.bean.api.CategoryParam;
import com.haoyang.lovelyreader.tre.helper.Configs;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.OnBookAddEvent;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.http.MyRequestEntity;
import com.haoyang.lovelyreader.tre.ui.FileActivity;
import com.haoyang.lovelyreader.tre.wifi.Constants;
import com.haoyang.lovelyreader.tre.wifi.PopupMenuDialog;
import com.haoyang.lovelyreader.tre.wifi.WebService;
import com.haoyang.reader.sdk.AnimationType;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.sdk.ColorService;
import com.haoyang.reader.sdk.DefaultCustomServivce;
import com.haoyang.reader.sdk.FeedBack;
import com.haoyang.reader.sdk.LineSpace;
import com.haoyang.reader.sdk.PageStyle;
import com.haoyang.reader.sdk.ReaderSDK;
import com.haoyang.reader.sdk.SDKParameterInfo;
import com.haoyang.reader.sdk.ShareEntity;
import com.haoyang.reader.service.bookservice.BookInfoService;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.java.common.utils.Utils;
import com.mjiayou.trecorelib.event.UserLoginStatusEvent;
import com.mjiayou.trecorelib.http.RequestSender;
import com.mjiayou.trecorelib.http.RequestCallback;
import com.mjiayou.trecorelib.json.JsonParser;
import com.mjiayou.trecorelib.util.ConvertUtils;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by xin on 18/9/22.
 */
public class HomeFragment extends BaseFragment {

    private final int REQUEST_CODE_ADD_BOOK = 102;

    private ImageView ivSearch;
    private EditText etSearch;
    private ImageView ivDelete;
    private TextView tvAddCategory;
    private GridView gvBook;
    private ListView lvSearch;
    private ImageView ivAdd;

    private HomeAdapter mHomeAdapter;
    private List<BookBean> mList = new ArrayList<>();

    UserBean mUserBean;

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
        ivSearch = (ImageView) view.findViewById(R.id.ivSearch);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        tvAddCategory = (TextView) view.findViewById(R.id.tvAddCategory);
        gvBook = (GridView) view.findViewById(R.id.gvBook);
        lvSearch = (ListView) view.findViewById(R.id.lvSearch);
        ivAdd = (ImageView) view.findViewById(R.id.ivAdd);

        initView();

        getBookList();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebService.stop(mContext);
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

        // etSearch
//        etSearch.clearFocus();
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
                    mList.clear();
                    mList.addAll(DBHelper.getBookBeanList(mUserBean.getUid()));
                } else {
                    mList.clear();
                    mList.addAll(DBHelper.getBookBeanListByKey(mUserBean.getUid(), key));
                }
                if (mHomeAdapter != null) {
                    mHomeAdapter.notifyDataSetChanged();
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

        // tvAddCategory
        tvAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });

        // gvBook
        mUserBean = DBHelper.getUserBean();
        mList.clear();
        mList.addAll(DBHelper.getBookBeanList(mUserBean.getUid()));
        mHomeAdapter = new HomeAdapter(mContext, mList);
        gvBook.setAdapter(mHomeAdapter);
        gvBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d(TAG, "onItemClick() called with: parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");
                BookBean bookBean = mList.get(position);

                Book book = bookBean.getBook();
                book.bookCover = bookBean.getCover();

                ToastUtils.show("正在打开书籍...");
                startReader(book, mUserBean);
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
    }

    @Subscribe(tags = {@Tag(Constants.RxBusEventType.POPUP_MENU_DIALOG_SHOW_DISMISS)})
    public void onPopupMenuDialogDismiss(Integer type) {
        WebService.stop(mContext);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivAdd, "translationY", ivAdd.getHeight() * 2, 0).setDuration(200L);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.start();
    }

    @Subscribe(thread = EventThread.IO, tags = {@Tag(Constants.RxBusEventType.LOAD_BOOK_LIST)})
    public void loadBookList(Integer type) {
        Timber.d("loadBookList:" + Thread.currentThread().getName());
        List<String> books = new ArrayList<>();
        File dir = Configs.DIR_BOOK;
        if (dir.exists() && dir.isDirectory()) {
            String[] fileNames = dir.list();
            if (fileNames != null) {
                for (String fileName : fileNames) {
                    books.add(fileName);
                }
            }
        }
        mActivity.runOnUiThread(() -> {
            LogUtils.d(TAG, ConvertUtils.parseString(books, "\n"));
//            mBooks.clear();
//            mBooks.addAll(books);
//            mBookshelfAdapter.notifyDataSetChanged();
        });
    }

    /**
     * onEvent
     */
    public void onEvent(UserLoginStatusEvent event) {
        LogUtils.d(TAG, "onEvent() called with: event = [" + event + "]");
        initView();
    }

    public void onEvent(OnBookAddEvent event) {
        LogUtils.d(TAG, "onEvent() called with: event = [" + event + "]");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onAddBook(event.getFileBean());
            }
        });
    }


    /**
     * onAddBook
     */
    public void onAddBook(FileBean fileBean) {
        if (fileBean == null) {
            return;
        }

        BookBean bookBean = new BookBean();
        bookBean.setName(fileBean.getName());
        bookBean.setSuffix(fileBean.getSuffix());
        bookBean.setPath(fileBean.getPath());

        // 查询该书是否已添加
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getPath().equals(bookBean.getPath())) {
                ToastUtils.show("这本书已经添加过了");
                return;
            }
        }

        // bookInfoService操作
        BookInfoService bookInfoService = new BookInfoService();
        bookInfoService.init(bookBean.getPath());
        bookBean.setBook(getBookInfo(bookInfoService, bookBean.getPath())); // 读取书籍信息
        bookBean.setCover(getBookCover(bookInfoService, bookBean.getPath())); // 读取封面图片
        bookInfoService.clear();

        addBook(bookBean);

        mList.add(bookBean);
        mHomeAdapter.notifyDataSetChanged();

        DBHelper.addBookBean(mUserBean.getUid(), bookBean);
    }

    /**
     * getBookInfo
     */
    private Book getBookInfo(BookInfoService bookInfoService, String filePath) {
        if (bookInfoService == null || TextUtils.isEmpty(filePath)) {
            return null;
        }

        Book book = new Book();
        book.path = filePath;
        bookInfoService.getBookInfo(book, "ePub");
        return book;
    }

    /**
     * getBookCover
     */
    private String getBookCover(BookInfoService bookInfoService, String filePath) {
        if (bookInfoService == null || TextUtils.isEmpty(filePath)) {
            return null;
        }

        InputStream inputStream = bookInfoService.getCoverInputStream(filePath);
        if (inputStream == null) {
            return null;
        }

        try {
            AndroidInfoService androidInfoService = new AndroidInfoService();
            String documentPath;
            try {
                documentPath = androidInfoService.getDownLoadPath(mContext);
            } catch (DeviceException e) {
                e.printStackTrace();
                return null;
            }

            String newPath = documentPath + File.separator + "bookimage" + File.separator;
            File file = new File(newPath);
            if (!file.exists()) {
                boolean mkdir = file.mkdir();
                if (!mkdir) {
                    return null;
                }
            }

            String md5Id = Utils.md5(filePath.getBytes());
            newPath += md5Id;
            file = new File(newPath);
            if (file.exists()) {
                file.delete();
            }

            OutputStream outputStream = null;
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                return newPath;
            } catch (IOException e) {
                LogUtils.printStackTrace(e);
                return null;
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        LogUtils.printStackTrace(e);
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LogUtils.printStackTrace(e);
            }
        }
        return null;
    }

    /**
     * 启动阅读器进行阅读
     */
    private void startReader(Book book, UserBean userBean) {
        try {
            // parameter
            SDKParameterInfo parameter = new SDKParameterInfo();
            parameter.appId = "773278";
            parameter.appKey = "10f2a8b3759b4304a5414269c5c4bf63";
            parameter.userId = userBean.getUid(); // 接入时需提供应用下唯一的，否则会出现数据错乱。
            parameter.userName = userBean.getNickName();

            ReaderSDK readerSDK = ReaderSDK.getInstance();
            readerSDK.initSDK(mContext, parameter);
            readerSDK.setCurrentPageStyle("wallpapers/background_4.jpg"); // 设置默认的样式，这个方法需要在setPageStyle方法之前执行
            readerSDK.setPageStyle(getPageStyleList()); // 将样式对象列表传给阅读器，阅读器根据这个来定义背景切换，如果在之前没有执行setCurrentPageStyle这个方法，setPageStyle就会把第一个做为默认样式
            readerSDK.setAnimationType(AnimationType.SlideAnimation); // 设置默认翻页动画。
            readerSDK.setAnimationSpeed(10); // 设置翻页动画移动时，每次移动的距离基础数字。
            readerSDK.setAnimationSpeedFactor(1.2f); // 设置翻页动画速度不断加快的因子。
            readerSDK.setTypefaceSwitchEnable(true); // 是否开启字体切换功能。
            readerSDK.setDefaultTypeface(Typeface.createFromAsset(mContext.getAssets(), "yahei.ttf")); // 设置当前的默认字体。

            // 设置阅读的各种属性
            readerSDK.setUpDownSpace(100); // 设置阅读器上下屏幕边与文字之间的间距。
            readerSDK.setLeftRightSpace(50); // 设置阅读器左右屏幕边与文字之间的间距。
            readerSDK.setLineSpacing(LineSpace.lineSpaceMiddle); // 设置当前行间距。
            readerSDK.setStressLineThickness(3); // 设置对文字画线时线条的粗细程度。
            readerSDK.setStressLineColor(new ColorService(230, 45, 150)); // 设置对文字画线时线条的颜色。
            readerSDK.setShowCatalog(); // 设置阅读器显示目录。setHideCatalog
            readerSDK.setShowBattery(true); // 设置阅读器显示电池。
            readerSDK.setShowPageNumber(true); // 设置阅读器显示页码。
            readerSDK.setShowTime(true); // 设置阅读器显示当前时间。
            readerSDK.setShareEnable(false); // 是否需要分享功能。
            readerSDK.setTypefaceSwitchEnable(false); // 是否需要有切换字体功能。

            // 设置阅读器内部一些功能由开发者来实现的接口，这里需要开发者实现SDKInteractive接口。
            readerSDK.setSDKInteractive(new SDKInteractiveImpl());
            readerSDK.setCustomServivce(new CustomServiceImpl());

            String id = book.path;
            if (book.path != null) {
                int index = book.path.lastIndexOf(File.separator);
                if (index != -1) {
                    id = book.path.substring(index + 1);
                }
            }
            book.id = id;

            readerSDK.startReader(mActivity, book, ReaderSDK.FilePathType.Local_File);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片处理方式，开发者自定义。
     *
     * @author tianyu912@yeah.net Mar 10, 2018
     */
    private class CustomServiceImpl extends DefaultCustomServivce {
        @Override
        public Size getImageSize(float imageWidth, float imageHeight, float readAdeaWidth, float readAreaHeight) {
            float height, width;
            float unit = readAdeaWidth / 4.0f;
            if (imageWidth <= unit) {
                width = unit;
                float rate = width / imageWidth;
                height = rate * imageHeight;
            } else {
                width = readAdeaWidth;
                float rate = width / imageWidth;
                height = rate * imageHeight;
            }

            if (height > readAreaHeight) {
                float rate = readAreaHeight / height;
                height = (int) (rate * height);
                width = width * rate;
            }
            return new Size((int) width, (int) height);
        }
    }

    /**
     * SDKInteractiveImpl
     */
    private class SDKInteractiveImpl implements ReaderSDK.SDKInteractive {

        @Override
        public void share(ShareEntity shareEntity) { // 打开分享界面
            ToastUtils.show("分享");
        }

        @Override
        public void typefaceSwitch() { // 打开切换字体界面
            Log.d(TAG, "typefaceSwitch() called");
            ToastUtils.show("切换字体");
        }

        @Override
        public void toolsBar(RelativeLayout relativeLayout) {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_reader_custom, null);
            relativeLayout.addView(contentView);

            // findViewById
            View tvComment = contentView.findViewById(R.id.tvComment);
            View tvBuy = contentView.findViewById(R.id.tvBuy);

            // tvComment
            tvComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show("评论");
                }
            });
            // tvBuy
            tvBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show("购买");
                }
            });
        }

        @Override
        public void feedback(FeedBack feedBack) {
            LogUtils.d(TAG, "feedback() called with: feedBack = [" + JsonParser.get().toJson(feedBack) + "]");
            ToastUtils.show("意见反馈");
        }

        @Override
        public void decrypt(String sourceFilePath, String targetFilePath) {
            LogUtils.d(TAG, "decrypt() called with: sourceFilePath = [" + sourceFilePath + "], targetFilePath = [" + targetFilePath + "]");
            ToastUtils.show("decrypt");
        }
    }

    /**
     * getPageStyleList
     */
    private List<PageStyle> getPageStyleList() {
        String[] wordColorArray = {
                "0x372d21",
                "0xb6bbbe",
                "0x322b23",
                "0x444444",
                "0x444444",
                "0x444444",
                "0x444444",
                "0x342d25",
                "0x293a27",
                "0x000200",
                "0x5f707a",
                "0x96938e"}; // 阅读器文字颜色，与背景资源是一一对应的。

        String[] wallPaperArray = {
                "wallpapers/background_2.jpg",
                "wallpapers/background_1.jpg",
                "wallpapers/background_3.jpg",
                "wallpapers/background_4.jpg",
                "wallpapers/background_5.jpg",
                "wallpapers/background_6.jpg",
                "wallpapers/background_7.jpg",
                "0xf6efe7",
                "0xceefd0",
                "0x596476",
                "0x001d28",
                "0x39312f"}; // 阅读器背景资源。

        PageStyle.BackgroundFillMode[] wallPaperType = {
                PageStyle.BackgroundFillMode.fullscreen,
                PageStyle.BackgroundFillMode.tileMirror,
                PageStyle.BackgroundFillMode.fullscreen,
                PageStyle.BackgroundFillMode.fullscreen,
                PageStyle.BackgroundFillMode.fullscreen,
                PageStyle.BackgroundFillMode.fullscreen,
                PageStyle.BackgroundFillMode.fullscreen,
                PageStyle.BackgroundFillMode.backgroundColor,
                PageStyle.BackgroundFillMode.backgroundColor,
                PageStyle.BackgroundFillMode.backgroundColor,
                PageStyle.BackgroundFillMode.backgroundColor,
                PageStyle.BackgroundFillMode.backgroundColor}; // 阅读器背景资源类型。

        PageStyle.NightDayMode[] nightDayMode = {
                PageStyle.NightDayMode.day,
                PageStyle.NightDayMode.none,
                PageStyle.NightDayMode.none,
                PageStyle.NightDayMode.none,
                PageStyle.NightDayMode.none,
                PageStyle.NightDayMode.none,
                PageStyle.NightDayMode.none,
                PageStyle.NightDayMode.none,
                PageStyle.NightDayMode.none,
                PageStyle.NightDayMode.none,
                PageStyle.NightDayMode.none,
                PageStyle.NightDayMode.night}; // 定义每种背景及文字是夜间模式还是白天模式。

        List<PageStyle> pageStyleList = new ArrayList<>();
        for (int index = 0; index < wallPaperArray.length; index++) { // 生成样式对象。
            PageStyle pageStyle = new PageStyle();
            pageStyle.styleName = wallPaperArray[index];
            pageStyle.backgroundValue = wallPaperArray[index];
            pageStyle.backgroundFillMode = wallPaperType[index];
            pageStyle.textColor = wordColorArray[index];
            pageStyle.catalogColor = wordColorArray[index];
            pageStyle.pageNumberColor = wordColorArray[index];
            pageStyle.timeColor = wordColorArray[index];
            pageStyle.nightDayMode = nightDayMode[index];
            pageStyle.source = PageStyle.Source.DEVELOPER;
            pageStyleList.add(pageStyle);
        }
        return pageStyleList;
    }


    public void addBook(BookBean bookBean) {
        BookAddParam bookAddParam = new BookAddParam();
        bookAddParam.setAuthor(bookBean.getBook().authors); // 作者
        bookAddParam.setBookCategory(""); // 图书目录
        bookAddParam.setBookDesc(""); // 图书简介
        bookAddParam.setBookDocId(""); // 电子书文档ID
        bookAddParam.setBookName(bookBean.getBook().bookName); // 电子书名称
        bookAddParam.setBookPath(""); // 电子书路径
        bookAddParam.setCategoryId(""); // 分类ID
        bookAddParam.setCoverDocId(""); // 图书封面文档ID
        bookAddParam.setCoverPath(""); // 图书封面路径

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiBookAdd);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(bookAddParam));
        RequestSender.get().send(myRequestEntity, new RequestCallback<BookSyncBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int code, BookSyncBean object) {

            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    public void getBookList() {
        BookSyncParam bookSyncParam = new BookSyncParam();
        bookSyncParam.setSyncType("1");
        bookSyncParam.setLastSyncDate("0");

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiBookSync);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(bookSyncParam));
        RequestSender.get().send(myRequestEntity, new RequestCallback<List<BookSyncBean>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int code, List<BookSyncBean> object) {

            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    public void addCategory() {
        CategoryParam categoryParam = new CategoryParam();
        categoryParam.setParentId("-1");
        categoryParam.setCategoryName("一级分类");

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiCategoryAdd);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(categoryParam));
        RequestSender.get().send(myRequestEntity, new RequestCallback<CategoryBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int code, CategoryBean categoryBean) {
                if (categoryBean != null) {
                    ToastUtils.show(categoryBean.getCategoryId() + " - " + categoryBean.getCategoryName());
                }
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }
}
