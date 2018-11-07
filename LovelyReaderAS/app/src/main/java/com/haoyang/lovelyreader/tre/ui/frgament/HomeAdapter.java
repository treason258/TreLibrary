package com.haoyang.lovelyreader.tre.ui.frgament;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.haoyang.lovelyreader.tre.bean.UploadBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.UploadBookParam;
import com.haoyang.lovelyreader.tre.helper.Configs;
import com.haoyang.lovelyreader.tre.helper.DBHelper;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.util.BookInfoUtils;
import com.haoyang.lovelyreader.tre.util.Utils;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.service.bookservice.BookInfoService;
import com.java.common.service.file.FileNameService;
import com.mjiayou.trecorelib.base.TCAdapter;
import com.mjiayou.trecorelib.base.TCViewHolder;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.http.RequestSender;
import com.mjiayou.trecorelib.http.callback.RequestCallback;
import com.mjiayou.trecorelib.image.ImageLoader;
import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.ToastUtils;
import com.mjiayou.trecorelib.util.UserUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;

import okhttp3.Call;

/**
 * Created by xin on 18/9/22.
 */

public class HomeAdapter extends TCAdapter {

    private Context mContext;
    private List<BookBean> mList;
    private LayoutInflater mLayoutInflater;

    HomeAdapter(Context context, List<BookBean> list) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_home, null);
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

    private class ViewHolder extends TCViewHolder<BookBean> {
        private ImageView ivBook;
        private TextView tvBook;
        private TextView tvSync;

        @Override
        protected void findView(View view) {
            ivBook = (ImageView) view.findViewById(R.id.ivBook);
            tvBook = (TextView) view.findViewById(R.id.tvBook);
            tvSync = (TextView) view.findViewById(R.id.tvSync);
        }

        @Override
        protected void initView(BookBean bookBean, int position) {
            if (bookBean != null) {
                // ivBook
                if (!TextUtils.isEmpty(bookBean.getLocalCoverPath())) {
                    Bitmap bitmap = BitmapFactory.decodeFile(bookBean.getLocalCoverPath());
                    if (bitmap != null) {
                        ivBook.setImageBitmap(bitmap);
                    }
                } else if (!TextUtils.isEmpty(bookBean.getCoverPath())) {
                    ImageLoader.get().load(ivBook, bookBean.getCoverPath());
                } else {
                    ivBook.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_main_book_default));
                }
                // tvBook
                if (!TextUtils.isEmpty(bookBean.getBookName())) {
                    tvBook.setText(bookBean.getBookName());
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
                String bookUrl = bookBean.getBookPath();
                String bookPath = bookBean.getLocalBookPath();
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
                        tvSync.setText("隐藏");
                        tvSync.setVisibility(View.GONE);
                        break;
                    case SYNC_TYPE_DOWNLOAD:
                        tvSync.setText("下载");
                        tvSync.setVisibility(View.VISIBLE);
                        tvSync.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadBook(bookBean, position);
                            }
                        });
                        break;
                    case SYNC_TYPE_UPLOAD:
                        tvSync.setText("上传");
                        tvSync.setVisibility(View.VISIBLE);
                        tvSync.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                uploadBook(bookBean, position);
                            }
                        });
                        break;
                    case SYNC_TYPE_ERROR:
                        tvSync.setText("异常");
                        tvSync.setVisibility(View.VISIBLE);
                        tvSync.setOnClickListener(new View.OnClickListener() {
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
    private void uploadBook(BookBean bookBean, int position) {
        UploadBookParam uploadBookParam = new UploadBookParam();
        uploadBookParam.setBookId(bookBean.getBookId());
        uploadBookParam.setUuid(EncodeHelper.getRandomChar());
        String content = ApiRequest.getContent(uploadBookParam);

        RequestEntity requestEntity = new RequestEntity(UrlConfig.apiUploadBook);
        requestEntity.setMethod(RequestMethod.POST_FILE);
        requestEntity.addHeader("sign", EncodeHelper.getSign(content));
        requestEntity.addHeader("token", UserUtils.getToken());
        requestEntity.addParam("data", content);
        requestEntity.addFile("bookFile", new File(bookBean.getLocalBookPath()));
        if (!TextUtils.isEmpty(bookBean.getLocalCoverPath())) {
            requestEntity.addFile("imgFile", new File(bookBean.getLocalCoverPath()));
        }
        RequestSender.get().send(requestEntity, new RequestCallback<UploadBean>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onProgress(float progress, long total) {
                super.onProgress(progress, total);
                int percent = (int) (progress * 100.0f);
                ToastUtils.show("正在上传：" + percent + "%");
            }

            @Override
            public void onSuccess(int code, UploadBean uploadBean) {
                if (uploadBean != null) {
                    ToastUtils.show("上传成功");
                    LogUtils.i("上传成功 -> " + uploadBean.getBookPath());

                    bookBean.setBookDocId(uploadBean.getBookDocId());
                    bookBean.setBookPath(uploadBean.getBookPath());
                    bookBean.setCoverDocId(uploadBean.getCoverDocId());
                    bookBean.setCoverPath(uploadBean.getCoverPath());

                    mList.set(position, bookBean);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtils.show(msg);
            }
        });
    }

    /**
     * 下载电子书文件
     */
    private void downloadBook(BookBean bookBean, int position) {
        String fileUrl = bookBean.getBookPath();
        String fileDir = Configs.DIR_SDCARD_PROJECT_BOOK;
        String fileName = Utils.getBookName(DBHelper.getUserBean(), bookBean);
        RequestSender.get().downloadFile(fileUrl, new FileCallBack(fileDir, fileName) {

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                int percent = (int) (progress * 100.0f);
                ToastUtils.show("正在下载：" + percent + "%");
            }

            @Override
            public void onResponse(File file, int id) {
                if (file != null) {
                    ToastUtils.show("下载成功");
                    LogUtils.i("下载成功 -> " + file.getAbsolutePath());

                    String filePath = file.getAbsolutePath();
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

                    mList.set(position, bookBean);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.show(e.toString());
            }
        });
    }


}
