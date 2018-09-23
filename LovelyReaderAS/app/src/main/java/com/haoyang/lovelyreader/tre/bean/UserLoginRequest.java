package com.haoyang.lovelyreader.tre.bean;

/**
 * Created by xin on 18/9/23.
 */

public class UserLoginRequest {

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
