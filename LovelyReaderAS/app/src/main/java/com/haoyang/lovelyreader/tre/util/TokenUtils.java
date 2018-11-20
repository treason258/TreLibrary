package com.haoyang.lovelyreader.tre.util;

import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.CommonParam;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.haoyang.lovelyreader.tre.http.MyRequestEntity;
import com.mjiayou.trecorelib.http.RequestSender;
import com.haoyang.lovelyreader.tre.http.RequestCallback;
import com.mjiayou.trecorelib.util.ToastUtils;

/**
 * Created by xin on 18/10/8.
 */

public class TokenUtils {

    public static void getTempToken(final OnGetTempTokenListener onGetTempTokenListener) {
        CommonParam commonParam = new CommonParam();
        commonParam.setData(EncodeHelper.getRandomChar());

        MyRequestEntity myRequestEntity = new MyRequestEntity(UrlConfig.apiTokenTemp);
        myRequestEntity.setContentWithHeader(ApiRequest.getContent(commonParam), null);
        RequestSender.get().send(myRequestEntity, new RequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int code, String object) {
                if (object != null) {
                    if (onGetTempTokenListener != null) {
                        onGetTempTokenListener.onGetTempToken(object);
                    }
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtils.show(msg);
                if (onGetTempTokenListener != null) {
                    onGetTempTokenListener.onGetTempToken(null);
                }
            }
        });
    }


    /**
     * OnGetTempTokenListener
     */
    public interface OnGetTempTokenListener {
        void onGetTempToken(String tempToken);
    }

    private OnGetTempTokenListener mOnGetTempTokenListener;

    public void setOnGetTempTokenListener(OnGetTempTokenListener onGetTempTokenListener) {
        mOnGetTempTokenListener = onGetTempTokenListener;
    }
}
