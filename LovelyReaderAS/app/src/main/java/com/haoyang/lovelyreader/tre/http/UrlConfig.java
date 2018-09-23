package com.haoyang.lovelyreader.tre.http;

/**
 * Created by xin on 18/9/23.
 */

public class UrlConfig {

  private static String mRootUrl = "http://112.126.80.1:9001";
  public static String apiUserLogin = mRootUrl + "/api/user/v1/login";
  public static String apiUserRegister = mRootUrl + "/api/user/v1/register";
  public static String apiUserFindPwd = mRootUrl + "/api/user/findPwd";
  public static String apiUserLogout = mRootUrl + "/api/user/logout";
}
