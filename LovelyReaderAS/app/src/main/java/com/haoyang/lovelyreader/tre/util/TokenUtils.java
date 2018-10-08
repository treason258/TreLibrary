package com.haoyang.lovelyreader.tre.util;

import com.haoyang.lovelyreader.tre.bean.api.ApiRequest;
import com.haoyang.lovelyreader.tre.bean.api.CommonData;
import com.haoyang.lovelyreader.tre.bean.api.CommonParam;
import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.haoyang.lovelyreader.tre.helper.UrlConfig;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.http.okhttp.RequestBuilder;
import com.mjiayou.trecorelib.http.okhttp.RequestCallback;
import com.mjiayou.trecorelib.json.JsonHelper;
import com.mjiayou.trecorelib.util.ToastUtils;

/**
 * Created by xin on 18/10/8.
 */

public class TokenUtils {

    public static void getTempToken(final OnGetTempTokenListener onGetTempTokenListener) {

        String randomChar = EncodeHelper.getRandomChar();
        CommonParam commonParam = new CommonParam();
        commonParam.setData(randomChar);
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setCommonData(CommonData.get());
        apiRequest.setParam(commonParam);
        String content = JsonHelper.get().toJson(apiRequest);

        RequestEntity requestEntity = new RequestEntity(UrlConfig.apiTokenTemp);
        requestEntity.setMethod(RequestMethod.POST_STRING);
        requestEntity.setContent(content);
        requestEntity.addHeader("sign", EncodeHelper.getSign(content));
        RequestBuilder.get().send(requestEntity, new RequestCallback<String>() {
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
