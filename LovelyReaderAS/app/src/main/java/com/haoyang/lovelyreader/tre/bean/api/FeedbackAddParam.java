package com.haoyang.lovelyreader.tre.bean.api;

import java.util.List;

/**
 * Created by xin on 18/9/30.
 */

public class FeedbackAddParam extends BaseParam {

    private List<String> imgList;
    private String phone;
    private String problemDesc;
    private String uid;
    private String userName;

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProblemDesc() {
        return problemDesc;
    }

    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
