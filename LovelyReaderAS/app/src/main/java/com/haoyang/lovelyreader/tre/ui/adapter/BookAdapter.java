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
import com.haoyang.lovelyreader.tre.bean.UploadBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.UploadBookParam;
import com.haoyang.lovelyreader.tre.helper.Configs;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.Global;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
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
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.ToastUtils;
import com.mjiayou.trecorelib.util.UserUtils;

import java.io.File;
import java.util.List;

/**
 * Created by xin on 18/9/22.
 */

public class BookAdapter extends TCAdapter {

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

        @Override
        protected void findView(View view) {
            ivBook = (ImageView) view.findViewById(R.id.ivBook);
            tvBook = (TextView) view.findViewById(R.id.tvBook);
            llSync = (LinearLayout) view.findViewById(R.id.llSync);
            tvSync = (TextView) view.findViewById(R.id.tvSync);
            ivSync = (ImageView) view.findViewById(R.id.ivSync);
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
                    ivBook.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_book_item_default_2));
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
                                uploadBook(bookBean, position, llSync, tvSync, ivSync);
                            }
                        });
                        break;
                    case SYNC_TYPE_ERROR:
                        tvSync.setText("异常");
                        ivSync.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_book_item_download_1));
                        llSync.setVisibility(View.VISIBLE);
                        llSync.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_main_home_book_item_download_1));
                        llSync.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.show("服务端和本地均没有书文件");
                            }
                        });
                        break;
                }
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

//    /**
//     * OnOptionListener
//     */
//    public interface OnOptionListener {
//        void onItemClick(BookBean bookBean, int position);
//
//        void onItemLongClick(BookBean bookBean, int position);
//    }
//
//    private CategoryAdapter.OnOptionListener mOnOptionListener;
//
//    public void setOnOptionListener(CategoryAdapter.OnOptionListener onOptionListener) {
//        mOnOptionListener = onOptionListener;
//    }
}
