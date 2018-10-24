package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by xin on 18/10/24.
 */

public class BookAddParam extends BaseParam {

//    "author": "string",
//    "bookCategory": "string",
//    "bookDesc": "string",
//    "bookDocId": 0,
//    "bookName": "string",
//    "bookPath": "string",
//    "categoryId": 0,
//    "coverDocId": 0,
//    "coverPath": "string"

    private String author; // 作者
    private String bookCategory; // 图书目录
    private String bookDesc; // 图书简介
    private String bookDocId; // 电子书文档ID
    private String bookName; // 电子书名称
    private String bookPath; // 电子书路径
    private String categoryId; // 分类ID
    private String coverDocId; // 图书封面文档ID
    private String coverPath; // 图书封面路径

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

    public String getBookDocId() {
        return bookDocId;
    }

    public void setBookDocId(String bookDocId) {
        this.bookDocId = bookDocId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCoverDocId() {
        return coverDocId;
    }

    public void setCoverDocId(String coverDocId) {
        this.coverDocId = coverDocId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
