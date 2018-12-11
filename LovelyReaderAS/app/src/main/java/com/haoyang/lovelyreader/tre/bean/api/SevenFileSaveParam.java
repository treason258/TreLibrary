package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by treason on 2018/12/11.
 */

public class SevenFileSaveParam extends BaseParam {

    private String bookId;
    private String sevenFileName;
    private String sevenFileSize;
    private String sevenHash;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getSevenFileName() {
        return sevenFileName;
    }

    public void setSevenFileName(String sevenFileName) {
        this.sevenFileName = sevenFileName;
    }

    public String getSevenFileSize() {
        return sevenFileSize;
    }

    public void setSevenFileSize(String sevenFileSize) {
        this.sevenFileSize = sevenFileSize;
    }

    public String getSevenHash() {
        return sevenHash;
    }

    public void setSevenHash(String sevenHash) {
        this.sevenHash = sevenHash;
    }
}
