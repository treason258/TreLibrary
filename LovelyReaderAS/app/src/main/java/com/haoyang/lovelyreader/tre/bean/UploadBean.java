package com.haoyang.lovelyreader.tre.bean;

/**
 * Created by xin on 18/10/24.
 */

public class UploadBean {

//    {
//        "statusCode": 900,
//        "data": {
//            "bookId": 5,
//            "uuid": "wp6972",
//            "coverPath": "http://112.126.80.1:80//doc/img//2018-10-27/56df03da02234688aba731bae19a0528.jpg",
//            "coverDocId": 37,
//            "bookPath": "http://112.126.80.1:80//doc/book//2018-10-27/eb4f1c8559a147d3bcbce9ae8ea86127.epub",
//            "bookDocId": 36
//        },
//        "timestamp": 1540617799061,
//        "msg": null
//    }

    private String bookId;
    private String uuid;
    private String coverPath;
    private String coverDocId;
    private String bookPath;
    private String bookDocId;

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

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getCoverDocId() {
        return coverDocId;
    }

    public void setCoverDocId(String coverDocId) {
        this.coverDocId = coverDocId;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public String getBookDocId() {
        return bookDocId;
    }

    public void setBookDocId(String bookDocId) {
        this.bookDocId = bookDocId;
    }
}
