package com.haoyang.lovelyreader.tre.bean;

/**
 * Created by xin on 18/9/23.
 */

public class UserRegisterResponse {

    //{
    //  "statusCode": 900,
    //  "data": {
    //      "uid": 4,
    //      "userName": "treason",
    //      "phone": "18600574123"
    //  },
    //  "timestamp": 1537697203613,
    //  "msg": null
    //}

    private String uid;
    private String userName;
    private String phone;

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
}
