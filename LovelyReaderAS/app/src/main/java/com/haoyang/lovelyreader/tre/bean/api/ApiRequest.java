package com.haoyang.lovelyreader.tre.bean.api;

import com.mjiayou.trecorelib.json.JsonParser;

/**
 * Created by xin on 18/9/29.
 */

public class ApiRequest {

    private CommonData commonData;
    private BaseParam param;

    public ApiRequest() {
    }

    public static ApiRequest get(BaseParam param) {
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setCommonData(CommonData.get());
        apiRequest.setParam(param);
        return apiRequest;
    }

    public static String getContent(BaseParam param) {
        return JsonParser.get().toJson(get(param));
    }

    public CommonData getCommonData() {
        return commonData;
    }

    public void setCommonData(CommonData commonData) {
        this.commonData = commonData;
    }

    public BaseParam getParam() {
        return param;
    }

    public void setParam(BaseParam param) {
        this.param = param;
    }
}
