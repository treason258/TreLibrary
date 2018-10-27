package com.haoyang.lovelyreader.tre.bean;

import com.haoyang.reader.sdk.Book;

import java.io.Serializable;

/**
 * Created by xin on 18/9/22.
 */

public class BookBean implements Serializable {

    private String name; // 名字
    private String suffix; // 后缀
    private String path; // 本地路径
    private String cover; // 封面
    private String url; // 网络路径
    private Book book; // 书的信息

    // 接口需要
    private String author; // 作者
    private String bookCategory; // 图书目录
    private String bookDesc; // 图书简介
    private String bookDocId; // 电子书文档ID
    private String bookId; // 电子书ID
    private String bookName; // 电子书名称
    private String bookPath; // 电子书路径
    private String categoryId; // 分类ID
    private String coverDocId; // 图书封面文档ID
    private String coverPath; // 图书封面路径
    private String createDate; // 创建时间

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

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public BookBean() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
