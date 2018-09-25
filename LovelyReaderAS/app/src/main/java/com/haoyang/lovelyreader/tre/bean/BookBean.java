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
