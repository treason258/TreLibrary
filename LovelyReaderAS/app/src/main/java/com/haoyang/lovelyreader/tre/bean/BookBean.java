package com.haoyang.lovelyreader.tre.bean;

/**
 * Created by xin on 18/9/22.
 */

public class BookBean {

    private String url; // 网络路径
    private String path; // 本地路径
    private String name; // 书名
    private String cover; // 封面图片

    public BookBean(String name) {
        this.name = name;
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
}
