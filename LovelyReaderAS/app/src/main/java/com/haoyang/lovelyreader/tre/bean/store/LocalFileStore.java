package com.haoyang.lovelyreader.tre.bean.store;

import com.haoyang.lovelyreader.tre.bean.FileBean;

import java.util.List;

/**
 * Created by xin on 2018/11/27.
 */

public class LocalFileStore {

    private List<FileBean> data;

    public List<FileBean> getData() {
        return data;
    }

    public void setData(List<FileBean> data) {
        this.data = data;
    }
}
