package com.haoyang.lovelyreader.tre.bean;

import java.io.Serializable;

/**
 * Created by xin on 18/9/25.
 */

public class FileBean implements Serializable {

    private String name; // 名字
    private String suffix; // 后缀
    private String path; // 本地路径
    private String icon; // 图标
    private boolean isFolder; // 是否是文件夹

    public FileBean() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
