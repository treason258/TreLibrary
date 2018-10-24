package com.haoyang.lovelyreader.tre.http;

import android.text.TextUtils;

import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.util.UserUtils;

/**
 * Created by xin on 18/10/15.
 */

public class MyRequestEntity extends RequestEntity {

    public MyRequestEntity(String url) {
        super(url);
        setMethod(RequestMethod.POST_STRING);
    }

    public void setContentWithHeader(String content, String token) {
        setContent(content);
        addHeader("sign", EncodeHelper.getSign(content));
        if (!TextUtils.isEmpty(token)) {
            addHeader("token", token);
        }
    }

    public void setContentWithHeader(String content) {
        setContentWithHeader(content, UserUtils.getToken());
    }
}
