package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by treason on 2018/12/11.
 */

public class SevenFileSaveParam extends BaseParam {

    private String bookId;
    private String fileSize;
    private String md5FileName;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getMd5FileName() {
        return md5FileName;
    }

    public void setMd5FileName(String md5FileName) {
        this.md5FileName = md5FileName;
    }
}
