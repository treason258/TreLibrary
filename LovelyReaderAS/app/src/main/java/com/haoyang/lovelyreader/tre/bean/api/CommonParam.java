package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by xin on 18/9/30.
 */

public class CommonParam extends BaseParam {

    private String data;

    public CommonParam() {
    }

    public static CommonParam get(String data) {
        CommonParam commonParam = new CommonParam();
        commonParam.setData(data);
        return commonParam;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
