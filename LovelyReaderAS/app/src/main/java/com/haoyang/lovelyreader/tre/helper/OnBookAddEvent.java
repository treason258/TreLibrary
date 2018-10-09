package com.haoyang.lovelyreader.tre.helper;

import com.haoyang.lovelyreader.tre.bean.FileBean;

/**
 * Created by xin on 18/10/9.
 */

public class OnBookAddEvent {

    private FileBean fileBean;

    public OnBookAddEvent(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }
}
