package com.haoyang.lovelyreader.tre.bean.api;

/**
 * Created by xin on 18/9/23.
 */

public class UserLoginParam extends BaseParam {

    //{
    //  "phone": "18600574121",
    //  "pwd": "123456"
    //}

    private String phone;
    private String pwd;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
