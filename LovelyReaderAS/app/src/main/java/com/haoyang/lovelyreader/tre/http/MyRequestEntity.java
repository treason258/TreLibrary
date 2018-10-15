package com.haoyang.lovelyreader.tre.http;

import com.haoyang.lovelyreader.tre.helper.EncodeHelper;
import com.mjiayou.trecorelib.http.RequestEntity;
import com.mjiayou.trecorelib.http.RequestMethod;
import com.mjiayou.trecorelib.util.UserUtils;

/**
 * Created by xin on 18/10/15.
 */

public class MyRequestEntity extends RequestEntity {

    /**
     * 构造函数
     */
    public MyRequestEntity(String url) {
        super(url);
        setMethod(RequestMethod.POST_STRING);
    }

//    @Override
//    public void setContent(Object object) {
//        super.setContent(object);
//    }

    @Override
    public void setContent(String content) {
        super.setContent(content);
        addHeader("token", UserUtils.getToken());
        addHeader("sign", EncodeHelper.getSign(content));
    }
}
