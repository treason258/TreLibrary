package com.haoyang.lovelyreader.tre.bean;

import com.mjiayou.trecorelib.bean.entity.TCUser;

/**
 * Created by xin on 18/9/25.
 */

public class UserBean extends TCUser {

    public static final String DEFAULT_USER_ID = "0";
    public static final String DEFAULT_USER_NAME = "default";

    //{
    //  "statusCode": 900,
    //  "data": {
    //      "uid": 2,
    //      "userName": "treason",
    //      "phone": "18600574121",
    //      "token": "31bfb6e4-04f3-449d-abc0-91b42a1d3184"
    //  },
    //  "timestamp": 1537697135815,
    //  "msg": null
    //}

    private String uid;
    private String userName;
    private String phone;
    private String token;

    /**
     * 默认用户
     */
    public static UserBean getDefault() {
        UserBean userBean = new UserBean();
        userBean.setUid(DEFAULT_USER_ID);
        userBean.setUserName(DEFAULT_USER_NAME);
        return userBean;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
