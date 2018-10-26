package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by xin on 18/10/26.
 */

public class UploadBookParam extends BaseParam {

    private String bookId;
    private String uuid;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
