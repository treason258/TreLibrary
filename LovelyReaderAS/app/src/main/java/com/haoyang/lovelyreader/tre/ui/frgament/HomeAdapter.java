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
import com.haoyang.lovelyreader.tre.bean.api.CommonParam;
import com.haoyang.lovelyreader.tre.helper.Configs;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.util.TokenUtils;
import com.mjiayou.trecorelib.base.TCAdapter;
import com.mjiayou.trecorelib.base.TCViewHolder;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.http.RequestSender;
import com.mjiayou.trecorelib.http.RequestCallback;
import com.mjiayou.trecorelib.util.ToastUtils;
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
            viewHolder.initView(mList.get(position));
        }
        return convertView;
    }

    private class ViewHolder extends TCViewHolder<BookBean> {
        private ImageView ivBook;
        private TextView tvBook;
        private TextView tvUpload;
        private TextView tvDownload;

        @Override
        protected void findView(View view) {
            ivBook = (ImageView) view.findViewById(R.id.ivBook);
            tvBook = (TextView) view.findViewById(R.id.tvBook);
            tvUpload = (TextView) view.findViewById(R.id.tvUpload);
            tvDownload = (TextView) view.findViewById(R.id.tvDownload);
        }

        @Override
        protected void initView(BookBean bean) {
            if (bean != null) {
                // ivBook
                if (!TextUtils.isEmpty(bean.getCover())) {
                    Bitmap bitmap = BitmapFactory.decodeFile(bean.getCover());
                    if (bitmap != null) {
                        ivBook.setImageBitmap(bitmap);
                    }
                } else {
                    ivBook.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_main_book_default));
                }
                // tvBook
                if (bean.getBook() != null && !TextUtils.isEmpty(bean.getBook().bookName)) {
                    tvBook.setText(bean.getBook().bookName);
                }
                // tvUpload
                tvUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TokenUtils.getTempToken(new TokenUtils.OnGetTempTokenListener() {
                            @Override
                            public void onGetTempToken(String tempToken) {
                                CommonParam commonParam = new CommonParam();
                                commonParam.setData(EncodeHelper.getRandomChar());
                                String content = ApiRequest.getContent(commonParam);

                                RequestEntity requestEntity = new RequestEntity(UrlConfig.apiUploadBook);
                                requestEntity.setMethod(RequestMethod.POST_FILE);
                                requestEntity.addHeader("sign", EncodeHelper.getSign(content));
                                requestEntity.addHeader("token", tempToken);
                                requestEntity.addParam("data", content);
                                requestEntity.addFile("bookFile", new File(bean.getPath()));
                                RequestSender.get().send(requestEntity, new RequestCallback<UploadBean>() {
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void inProgress(float progress, long total) {
                                        super.inProgress(progress, total);
                                        int percent = (int) (progress * 100.0f);
                                        ToastUtils.show("正在上传：" + percent + "%");
                                    }

                                    @Override
                                    public void onSuccess(int code, UploadBean uploadBean) {
                                        if (uploadBean != null) {
                                            ToastUtils.show("上传成功：" + uploadBean.getFullFilePath());
                                        }
                                    }

                                    @Override
                                    public void onFailure(int code, String msg) {
                                    }
                                });
                            }
                        });
                    }
                });
                // tvDownload
                tvDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fileUrl = "http://112.126.80.1:80//doc/book//2018-10-24/3154d92b44054c3494b12ea5991f92ec.epub";
                        RequestSender.get().downloadFile(fileUrl, new FileCallBack(Configs.DIR_BOOK.getAbsolutePath(), "下载文件.epub") {

                            @Override
                            public void inProgress(float progress, long total, int id) {
                                super.inProgress(progress, total, id);
                                int percent = (int) (progress * 100.0f);
                                ToastUtils.show("正在下载：" + percent + "%");
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(File file, int id) {
                                if (file != null) {
                                    ToastUtils.show("下载成功：" + file.getAbsolutePath());
                                }
                            }
                        });
                    }
                });
            }
        }
    }
}
