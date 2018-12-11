package com.haoyang.lovelyreader.tre.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.haoyang.lovelyreader.tre.bean.ConfirmBean;
import com.haoyang.lovelyreader.tre.bean.ExistsBean;
import com.haoyang.lovelyreader.tre.bean.FileVoBean;
import com.haoyang.lovelyreader.tre.bean.UploadBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.CommonParam;
import com.haoyang.lovelyreader.tre.bean.api.SevenFileSaveParam;
import com.haoyang.lovelyreader.tre.bean.api.UploadBookParam;
import com.haoyang.lovelyreader.tre.helper.Configs;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.Global;
import com.haoyang.lovelyreader.tre.helper.QiNiuHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.http.MyRequestEntity;
import com.haoyang.lovelyreader.tre.http.RequestCallback;
import com.haoyang.lovelyreader.tre.util.BookInfoUtils;
import com.haoyang.lovelyreader.tre.util.LoginUtils;
import com.haoyang.lovelyreader.tre.util.Utils;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.service.bookservice.BookInfoService;
import com.java.common.service.file.FileNameService;
import com.mjiayou.trecorelib.base.TCAdapter;
import com.mjiayou.trecorelib.base.TCViewHolder;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.http.RequestSender;
import com.mjiayou.trecorelib.http.callback.FileCallback;
import com.mjiayou.trecorelib.image.ImageLoader;
import com.mjiayou.trecorelib.json.JsonParser;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.ToastUtils;
import com.mjiayou.trecorelib.util.UserUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by xin on 18/9/22.
 */

public class BookAdapter extends TCAdapter {

    // TAG
    protected final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private List<BookBean> mList;
    private LayoutInflater mLayoutInflater;

    public BookAdapter(Context context, List<BookBean> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_book, null);
            viewHolder = new ViewHolder();
            viewHolder.findView(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mList != null && mList.size() > position && mList.get(position) != null) {
            viewHolder.initView(mList.get(position), position);
        }
        return convertView;
    }

    /**
     * getList
     */
    public List<BookBean> getList() {
        return mList;
    }

    /**
     * setList
     */
    public void setList(List<BookBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder
     */
    private class ViewHolder extends TCViewHolder<BookBean> {
        private ImageView ivBook;
        private TextView tvBook;
        private LinearLayout llSync;
        private TextView tvSync;
        private ImageView ivSync;
        private TextView tvSource;

        @Override
        protected void findView(View view) {
            ivBook = (ImageView) view.findViewById(R.id.ivBook);
            tvBook = (TextView) view.findViewById(R.id.tvBook);
            llSync = (LinearLayout) view.findViewById(R.id.llSync);
            tvSync = (TextView) view.findViewById(R.id.tvSync);
            ivSync = (ImageView) view.findViewById(R.id.ivSync);
            tvSource = (TextView) view.findViewById(R.id.tvSource);
        }

        @Override
        protected void initView(BookBean bookBean, int position) {
            if (bookBean != null) {
                // tvBook
                if (!TextUtils.isEmpty(bookBean.getBookServerInfo().getBookName())) {
                    tvBook.setText(bookBean.getBookServerInfo().getBookName());
                    tvBook.setVisibility(View.GONE);
                }
                // ivBook
                boolean showDefault = false;
                if (!TextUtils.isEmpty(bookBean.getBookLocalInfo().getLocalCoverPath())) {
                    Bitmap bitmap = BitmapFactory.decodeFile(bookBean.getBookLocalInfo().getLocalCoverPath());
                    if (bitmap != null) {
                        ivBook.setImageBitmap(bitmap);
                    } else {
                        showDefault = true;
                    }
                } else if (!TextUtils.isEmpty(bookBean.getBookServerInfo().getCoverPath())) {
                    ImageLoader.get().load(ivBook, bookBean.getBookServerInfo().getCoverPath());
                } else {
                    showDefault = true;
                }
                if (showDefault) {
                    ivBook.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_book_item_default_3));
                    tvBook.setVisibility(View.VISIBLE);
                }
                // tvSync-分四种情况
                // 1-服务器有文件，本地有文件，则隐藏按钮
                // 2-服务器有文件，本地没有文件，则显示"下载"
                // 3-服务器没有文件，本地有文件，则显示"上传"
                // 4-服务器没有文件，本地没有文件，则显示"异常"
                final int SYNC_TYPE_HIDE = 1;
                final int SYNC_TYPE_DOWNLOAD = 2;
                final int SYNC_TYPE_UPLOAD = 3;
                final int SYNC_TYPE_ERROR = 4;
                int syncType = SYNC_TYPE_HIDE;
                String bookUrl = bookBean.getBookServerInfo().getBookPath();
                String bookPath = bookBean.getBookLocalInfo().getLocalBookPath();
                if (!TextUtils.isEmpty(bookUrl)) {
                    if (!TextUtils.isEmpty(bookPath)) {
                        syncType = SYNC_TYPE_HIDE;
                    } else {
                        syncType = SYNC_TYPE_DOWNLOAD;
                    }
                } else {
                    if (!TextUtils.isEmpty(bookPath)) {
                        syncType = SYNC_TYPE_UPLOAD;
                    } else {
                        syncType = SYNC_TYPE_ERROR;
                    }
                }
                switch (syncType) {
                    default:
                    case SYNC_TYPE_HIDE:
                        llSync.setVisibility(View.GONE);
                        break;
                    case SYNC_TYPE_DOWNLOAD:
                        tvSync.setText("下载");
                        ivSync.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_book_item_download_1));
                        llSync.setVisibility(View.VISIBLE);
                        llSync.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_main_home_book_item_download_1));
                        llSync.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (LoginUtils.checkNotLoginAndToast()) {
                                    return;
                                }
                                downloadBook(bookBean, position, llSync, tvSync, ivSync);
                            }
                        });
                        break;
                    case SYNC_TYPE_UPLOAD:
                        tvSync.setText("上传");
                        ivSync.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_book_item_download_1));
                        llSync.setVisibility(View.VISIBLE);
                        llSync.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_main_home_book_item_download_1));
                        llSync.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (LoginUtils.checkNotLoginAndToast()) {
                                    return;
                                }
                                uploadBook2(bookBean, position, llSync, tvSync, ivSync);
                            }
                        });
                        break;
                    case SYNC_TYPE_ERROR:
                        tvSync.setText("未上传");
                        ivSync.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_book_item_download_1));
                        llSync.setVisibility(View.VISIBLE);
                        llSync.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_main_home_book_item_download_1));
                        llSync.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.show("电子书不在当设备中，请到 " +getPlatform(bookBean)+ " 平台上传");
                            }
                        });
                        break;
                }

                // tvSource
                tvSource.setText("来源：" + getPlatform(bookBean));
            }
        }
    }

    /**
     * 上传电子书文件
     */
    private void uploadBook(BookBean bookBean, int position, LinearLayout llSync, TextView tvSync, ImageView ivSync) {
        if (Global.mIsUploading) {
            ToastUtils.show("有其他电子书正在上传，请稍后操作");
            return;
        }
        UploadBookParam uploadBookParam = new UploadBookParam();
        uploadBookParam.setBookId(bookBean.getBookServerInfo().getBookId());
        uploadBookParam.setUuid(EncodeHelper.getRandomChar());
        String content = ApiRequest.getContent(uploadBookParam);

        RequestEntity requestEntity = new RequestEntity(UrlConfig.apiUploadBook);
        requestEntity.setMethod(RequestMethod.POST_FILE);
        requestEntity.addHeader("sign", EncodeHelper.getSign(content));
        requestEntity.addHeader("token", UserUtils.getToken());
        requestEntity.addParam("data", content);
        requestEntity.addFile("bookFile", new File(bookBean.getBookLocalInfo().getLocalBookPath()));
        if (!TextUtils.isEmpty(bookBean.getBookLocalInfo().getLocalCoverPath())) {
            requestEntity.addFile("imgFile", new File(bookBean.getBookLocalInfo().getLocalCoverPath()));
        }
        RequestSender.get().send(requestEntity, new RequestCallback<UploadBean>() {
            @Override
            public void onStart() {
                Global.mIsUploading = true;
            }

            @Override
            public void onProgress(float progress, long total) {
                super.onProgress(progress, total);
                if (progress == 1) {
                    progress = 0.99f;
                }
                String percent = ((int) (progress * 100.0f)) + "%";

                LogUtils.i("正在上传：" + percent);
                tvSync.setText(percent);
                ivSync.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_book_item_download_2));
                llSync.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_main_home_book_item_download_2));
            }

            @Override
            public void onSuccess(int code, UploadBean uploadBean) {
                Global.mIsUploading = false;
                if (uploadBean != null) {
                    ToastUtils.show("上传成功");
                    LogUtils.i("上传成功 -> " + uploadBean.getBookPath());

                    bookBean.getBookServerInfo().setBookDocId(uploadBean.getBookDocId());
                    bookBean.getBookServerInfo().setBookPath(uploadBean.getBookPath());
                    bookBean.getBookServerInfo().setCoverDocId(uploadBean.getCoverDocId());
                    bookBean.getBookServerInfo().setCoverPath(uploadBean.getCoverPath());

                    DBHelper.modifyBookBean(Global.mCurrentUser.getUid(), bookBean);

                    mList.set(position, bookBean);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                Global.mIsUploading = false;
                ToastUtils.show(msg);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 下载电子书文件
     */
    private void downloadBook(BookBean bookBean, int position, LinearLayout llSync, TextView tvSync, ImageView ivSync) {
        if (Global.mIsDownloading) {
            ToastUtils.show("有其他电子书正在下载，请稍后操作");
            return;
        }
        String fileUrl = bookBean.getBookServerInfo().getBookPath();
        String fileDir = Configs.DIR_SDCARD_PROJECT_BOOK;
        String fileName = Utils.getBookName(DBHelper.getUserBean(), bookBean.getBookServerInfo());
        RequestSender.get().downloadFile(fileUrl, fileDir, fileName, new FileCallback() {
            @Override
            public void onStart() {
                Global.mIsDownloading = true;
            }

            @Override
            public void onProgress(float progress, long total) {
                super.onProgress(progress, total);
                if (progress == 1) {
                    progress = 0.99f;
                }
                String percent = ((int) (progress * 100.0f)) + "%";

                LogUtils.i("正在下载：" + percent);
                tvSync.setText(percent);
                ivSync.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_book_item_download_2));
                llSync.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_main_home_book_item_download_2));
            }

            @Override
            public void onSuccess(int code, File file) {
                Global.mIsDownloading = false;
                if (file != null) {
                    ToastUtils.show("下载成功");
                    LogUtils.i("下载成功 -> " + file.getAbsolutePath());

                    String filePath = file.getAbsolutePath();

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

                    DBHelper.modifyBookBean(Global.mCurrentUser.getUid(), bookBean);

                    mList.set(position, bookBean);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                Global.mIsDownloading = false;
                ToastUtils.show(msg);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 上传电子书文件到七牛
     */
    private void uploadBook2(BookBean bookBean, int position, LinearLayout llSync, TextView tvSync, ImageView ivSync) {
        if (Global.mIsUploading) {
            ToastUtils.show("有其他电子书正在上传，请稍后操作");
            return;
        }

        tvSync.setText("准备上传");

        // 电子书文件
        final File bookFile = new File(bookBean.getBookLocalInfo().getLocalBookPath());
        // 电子书id
        final String bookId = bookBean.getBookServerInfo().getBookId();
        // 电子书服务端文件名
        final String bookKey = Utils.getFileMD5(bookFile) + "." + bookBean.getBookLocalInfo().getFileSuffix();

        /*
         * step1-检查当前文件是否存在
         */
        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiSevenfileExists);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(CommonParam.get(bookKey)));
        RequestSender.get().send(myRequestEntity, new RequestCallback<ExistsBean>() {
            @Override
            public void onStart() {
                Global.mIsUploading = true;
            }

            @Override
            public void onSuccess(int code, ExistsBean existsBean) {
                if (existsBean != null && existsBean.isHasExistFile()) {
                    FileVoBean fileVoBean = existsBean.getFileVo();
                    String key = fileVoBean.getSevenFileName();
                    String hash = fileVoBean.getSevenHash();
                    String fsize = fileVoBean.getSevenFileSize();

                    /*
                     * step4-将文件和电子书绑定
                     */
                    SevenFileSaveParam sevenFileSaveParam = new SevenFileSaveParam();
                    sevenFileSaveParam.setBookId(bookId);
                    sevenFileSaveParam.setSevenFileName(key);
                    sevenFileSaveParam.setSevenHash(hash);
                    sevenFileSaveParam.setSevenFileSize(fsize);
                    MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiBookConfirm);
                    myRequestEntity.setContentWithHeader(ApiRequest.getContent(sevenFileSaveParam));
                    RequestSender.get().send(myRequestEntity, new RequestCallback<ConfirmBean>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(int code, ConfirmBean confirmBean) {
                            if (confirmBean != null && !TextUtils.isEmpty(confirmBean.getUrl())) {
                                Global.mIsUploading = false;

                                ToastUtils.show("极速上传成功");
                                LogUtils.i("极速上传成功 -> " + existsBean.getUrl());

                                String url = confirmBean.getUrl();
                                bookBean.getBookServerInfo().setBookPath(url);
                                DBHelper.modifyBookBean(Global.mCurrentUser.getUid(), bookBean);

                                mList.set(position, bookBean);
                                notifyDataSetChanged();
                            } else {
                                Global.mIsUploading = false;
                            }
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            Global.mIsUploading = false;
                            ToastUtils.show(msg);
                        }
                    });
                } else {
                    /*
                     * step2-获取七牛token
                     */
                    MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiSevenfileTOken);
                    myRequestEntity.setContentWithHeader(ApiRequest.getContent(CommonParam.get(EncodeHelper.getRandomChar())));
                    RequestSender.get().send(myRequestEntity, new RequestCallback<String>() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(int code, String token) {
                            if (TextUtils.isEmpty(token)) {
                                Global.mIsUploading = false;
                                ToastUtils.show("获取token失败");
                            } else {
                                /*
                                 * step3-调用七牛sdk上传文件
                                 */
                                QiNiuHelper.getUploadManager().put(bookFile, bookKey, token, new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                                        LogUtils.i(TAG, "key -> " + key);
                                        LogUtils.i(TAG, "responseInfo -> " + JsonParser.get().toJson(responseInfo));
                                        LogUtils.i(TAG, "jsonObject -> " + JsonParser.get().toJson(jsonObject));
                                        // res包含hash、key等信息，具体字段取决于上传策略的设置
                                        if (responseInfo.isOK()) {
                                            LogUtils.i(TAG, "Upload Success");
                                            String hash = "";
                                            String fsize = "";
                                            try {
                                                hash = jsonObject.getString("hash");
                                                fsize = jsonObject.getString("fsize");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            if (TextUtils.isEmpty(hash) || TextUtils.isEmpty(fsize)) {
                                                Global.mIsUploading = false;
                                                ToastUtils.show("上传失败");
                                            } else {
                                                /*
                                                 * step4-将文件和电子书绑定
                                                */
                                                SevenFileSaveParam sevenFileSaveParam = new SevenFileSaveParam();
                                                sevenFileSaveParam.setBookId(bookId);
                                                sevenFileSaveParam.setSevenFileName(key);
                                                sevenFileSaveParam.setSevenHash(hash);
                                                sevenFileSaveParam.setSevenFileSize(fsize);
                                                MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiBookConfirm);
                                                myRequestEntity.setContentWithHeader(ApiRequest.getContent(sevenFileSaveParam));
                                                RequestSender.get().send(myRequestEntity, new RequestCallback<ConfirmBean>() {
                                                    @Override
                                                    public void onStart() {

                                                    }

                                                    @Override
                                                    public void onSuccess(int code, ConfirmBean confirmBean) {
                                                        if (confirmBean != null && !TextUtils.isEmpty(confirmBean.getUrl())) {
                                                            Global.mIsUploading = false;

                                                            ToastUtils.show("上传成功");
                                                            LogUtils.i("上传成功 -> " + existsBean.getUrl());

                                                            String url = confirmBean.getUrl();
                                                            bookBean.getBookServerInfo().setBookPath(url);
                                                            DBHelper.modifyBookBean(Global.mCurrentUser.getUid(), bookBean);

                                                            mList.set(position, bookBean);
                                                            notifyDataSetChanged();
                                                        } else {
                                                            Global.mIsUploading = false;
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(int code, String msg) {
                                                        Global.mIsUploading = false;
                                                        ToastUtils.show(msg);
                                                    }
                                                });
                                            }
                                        } else {
                                            Global.mIsUploading = false;
                                            ToastUtils.show("上传失败");
                                        }
                                    }
                                }, new UploadOptions(null, null, false, new UpProgressHandler() {
                                    @Override
                                    public void progress(String key, double percent) {
                                        if (percent >= 0.95d) {
                                            percent = 0.95d;
                                        }
                                        String progress = ((int) (percent * 100.0f)) + "%";

                                        LogUtils.i("正在上传：" + progress);
                                        tvSync.setText(progress);
                                        ivSync.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_book_item_download_2));
                                        llSync.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_main_home_book_item_download_2));

                                    }
                                }, null));
                            }
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            Global.mIsUploading = false;
                            ToastUtils.show(msg);
                        }
                    });
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                Global.mIsUploading = false;
                ToastUtils.show(msg);
            }
        });
    }

    private String getPlatform(BookBean bookBean) {
        String source = "未知";
        if (bookBean != null && bookBean.getBookServerInfo() != null && !TextUtils.isEmpty(bookBean.getBookServerInfo().getSource())) {
            source = bookBean.getBookServerInfo().getSource();
            switch (source) {
                case "ANDROID_PHONE":
                    source = "Android";
                    break;
                case "ANDROID_PAD":
                    source = "Android";
                    break;
                case "IOS_PHONE":
                    source = "iPhone";
                    break;
                case "IOS_PAD":
                    source = "iPad";
                    break;
                case "WINDOWS":
                    source = "Windows";
                    break;
                case "MAC":
                    source = "Mac";
                    break;
                default:
                    source = "未知";
                    break;
            }
        }
        return source;
    }
}