package com.haoyang.lovelyreader.tre.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xin on 18/9/25.
 */

public class FileBean implements Parcelable {

    private String name; // 名字
    private String suffix; // 后缀
    private String path; // 本地路径
    private String icon; // 图标
    private boolean isFolder; // 是否是文件夹
    private boolean isSelected; // 是否选中状态

    public FileBean() {
    }

    protected FileBean(Parcel in) {
        name = in.readString();
        suffix = in.readString();
        path = in.readString();
        icon = in.readString();
        isFolder = in.readByte() != 0;
        isSelected = in.readByte() != 0;
    }

    public static final Creator<FileBean> CREATOR = new Creator<FileBean>() {
        @Override
        public FileBean createFromParcel(Parcel in) {
            return new FileBean(in);
        }

        @Override
        public FileBean[] newArray(int size) {
            return new FileBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(suffix);
        dest.writeString(path);
        dest.writeString(icon);
        dest.writeByte((byte) (isFolder ? 1 : 0));
        dest.writeByte((byte) (isSelected ? 1 : 0));
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
