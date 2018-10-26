package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by xin on 18/10/24.
 */

public class BookAddParam extends BaseParam {

//    "author": "string",
//    "bookCategory": "string",
//    "bookDesc": "string",
//    "bookName": "string",
//    "categoryId": 0,

    private String author; // 作者
    private String bookCategory; // 图书目录
    private String bookDesc; // 图书简介
    private String bookName; // 电子书名称
    private String categoryId; // 分类ID

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
