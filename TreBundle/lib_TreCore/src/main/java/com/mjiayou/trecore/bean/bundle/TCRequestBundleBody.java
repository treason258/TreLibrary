package com.mjiayou.trecore.bean.bundle;

/**
 * Created by treason on 15-10-21.
 */
public class TCRequestBundleBody {

    private String page_number; // 页数，从1开始
    private String page_count; // 请求条数，默认为10，最大为20

    public String getPage_number() {
        return page_number;
    }

    public void setPage_number(String page_number) {
        this.page_number = page_number;
    }

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }
}
