package com.haoyang.lovelyreader.tre.helper;

/**
 * Created by xin on 18/9/23.
 */

public class UrlConfig {

    private static String mRootUrl = "http://112.126.80.1:9004";

    // APP版本升级 Api接口
    public static String apiAppUpgrade = mRootUrl + "/api/appupgrade/v1/check";

    // Token Api接口
    public static String apiTokenTemp = mRootUrl + "/api/token/v1/temp";

    // 意见反馈 Api接口
    public static String apiFeedbackAdd = mRootUrl + "/api/feedback/v1/add";

    // 用户模块 Api接口
    public static String apiUserFindPwd = mRootUrl + "/api/user/findPwd";
    public static String apiUserLogout = mRootUrl + "/api/user/logout";
    public static String apiUserLogin = mRootUrl + "/api/user/v1/login";
    public static String apiUserRegister = mRootUrl + "/api/user/v1/register";

    // 短信模块 Api接口
    public static String apiSmsSendchangepwdsms = mRootUrl + "/api/sms/v1/sendchangepwdsms";
    public static String apiSmsSendloginsms = mRootUrl + "/api/sms/v1/sendloginsms";
    public static String apiSmsSendrigstersms = mRootUrl + "/api/sms/v1/sendrigstersms";
}
