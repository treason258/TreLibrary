package com.haoyang.lovelyreader.tre.helper;

import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.haoyang.lovelyreader.tre.bean.CategoryBean;
import com.haoyang.lovelyreader.tre.bean.ResponseBean;
import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.BookSyncParam;
import com.haoyang.lovelyreader.tre.bean.api.CategorySyncParam;
import com.haoyang.lovelyreader.tre.http.MyRequestEntity;
import com.haoyang.lovelyreader.tre.http.RequestCallback;
import com.mjiayou.trecorelib.http.RequestSender;
import com.mjiayou.trecorelib.http.callback.ObjectCallback;

import java.util.List;

/**
 * Created by xin on 2018/11/20.
 */

public class SyncHelper {

    /**
     * 同步数据
     */
    public static void syncServerData(OnSyncDataListener onSyncDataListener) {
        // 取上次同步时间
        long timestamp = DBHelper.getLastSyncDate(Global.mCurrentUser.getUid());

        // 第一步：同步电子书数据
        BookSyncParam bookSyncParam = new BookSyncParam();
        if (timestamp == 0) {
            bookSyncParam.setSyncType(BookSyncParam.SYNC_TYPE_ALL);
            bookSyncParam.setLastSyncDate(String.valueOf(timestamp));
        } else {
            bookSyncParam.setSyncType(BookSyncParam.SYNC_TYPE_TIME);
            bookSyncParam.setLastSyncDate(String.valueOf(timestamp));
        }

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiBookSync);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(bookSyncParam));
        RequestSender.get().send(myRequestEntity, new ObjectCallback<ResponseBean<List<BookBean.BookServerInfo>>>() {
            @Override
            public void onStart() {
                if (onSyncDataListener != null) {
                    onSyncDataListener.onSyncStart();
                }
            }

            @Override
            public void onSuccess(int code, ResponseBean<List<BookBean.BookServerInfo>> object) {
                // 同步分类数据
                DBHelper.setLastSyncDate(Global.mCurrentUser.getUid(), object.getTimestamp());
                syncCategoryList(onSyncDataListener, timestamp, object.getData());
            }

            @Override
            public void onFailure(int code, String msg) {
                if (onSyncDataListener != null) {
                    onSyncDataListener.onSyncFailure(msg);
                }
            }
        });
    }

    /**
     * 同步分类数据
     */
    private static void syncCategoryList(OnSyncDataListener onSyncDataListener, long timestamp, List<BookBean.BookServerInfo> bookServerInfoList) {
        // 第二步：同步分类数据
        CategorySyncParam categorySyncParam = new CategorySyncParam();
        if (timestamp == 0) {
            categorySyncParam.setSyncType(CategorySyncParam.SYNC_TYPE_ALL);
            categorySyncParam.setLastSyncDate(String.valueOf(timestamp));
        } else {
            categorySyncParam.setSyncType(CategorySyncParam.SYNC_TYPE_TIME);
            categorySyncParam.setLastSyncDate(String.valueOf(timestamp));
        }

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiCategorySync);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(categorySyncParam));
        RequestSender.get().send(myRequestEntity, new RequestCallback<List<CategoryBean>>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int code, List<CategoryBean> categoryBeanList) {
                if (onSyncDataListener != null) {
                    onSyncDataListener.onSyncSuccess(bookServerInfoList, categoryBeanList);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if (onSyncDataListener != null) {
                    onSyncDataListener.onSyncFailure(msg);
                }
            }
        });
    }

    /**
     * OnGetTempTokenListener
     */
    public interface OnSyncDataListener {
        void onSyncStart();

        void onSyncSuccess(List<BookBean.BookServerInfo> bookServerInfoList, List<CategoryBean> categoryBeanList);

        void onSyncFailure(String msg);
    }

    //private static OnSyncDataListener mOnSyncDataListener;
    //
    //public void setOnSyncDataListener(OnSyncDataListener onSyncDataListener) {
    //  mOnSyncDataListener = onSyncDataListener;
    //}
}
