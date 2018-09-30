package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by xin on 18/9/29.
 */

public class ApiRequest {

    private CommonData commonData;
    private BaseParam param;

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
