package com.haoyang.lovelyreader.tre.bean;

/**
 * Created by xin on 18/9/23.
 */

public class UserRegisterRequest {

  //{
  //  "confirmPwd": "123456",
  //  "phone": "18600574121",
  //  "pwd": "123456",
  //  "smsCode": "1234",
  //  "userName": "treason"
  //}

  private String userName;
  private String phone;
  private String smsCode;
  private String pwd;
  private String confirmPwd;

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

  public String getSmsCode() {
    return smsCode;
  }

  public void setSmsCode(String smsCode) {
    this.smsCode = smsCode;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public String getConfirmPwd() {
    return confirmPwd;
  }

  public void setConfirmPwd(String confirmPwd) {
    this.confirmPwd = confirmPwd;
  }
}
