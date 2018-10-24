package com.haoyang.lovelyreader.tre.helper;

/**
 * Created by xin on 18/9/23.
 */

public class UrlConfig {

    // 接口地址
    private static String mRootHost = "http://112.126.80.1:9003";
    private static String mRootPath = "/api";
    private static String mRootUrl = mRootHost + mRootPath;

    // APP版本升级 Api接口
    public static String apiAppUpgrade = mRootUrl + "/appupgrade/v1/check";

    // Token Api接口
    public static String apiTokenTemp = mRootUrl + "/token/v1/temp";

    // 意见反馈 Api接口
    public static String apiFeedbackAdd = mRootUrl + "/feedback/v1/add";

    // 用户模块 Api接口
    public static String apiUserFindPwd = mRootUrl + "/user/findPwd";
    public static String apiUserLogout = mRootUrl + "/user/logout";
    public static String apiUserLogin = mRootUrl + "/user/v1/login";
    public static String apiUserRegister = mRootUrl + "/user/v1/register";

    // 短信模块 Api接口
    public static String apiSmsSendchangepwdsms = mRootUrl + "/sms/v1/sendchangepwdsms";
    public static String apiSmsSendloginsms = mRootUrl + "/sms/v1/sendloginsms";
    public static String apiSmsSendrigstersms = mRootUrl + "/sms/v1/sendrigstersms";

    // 文件上传Api接口
    public static String apiUploadBook = mRootUrl + "/upload/book";
    public static String apiUploadChange = mRootUrl + "/upload/change";
    public static String apiUploadDel = mRootUrl + "/upload/del";
    public static String apiUploadImg = mRootUrl + "/upload/img";
    public static String apiUploadNotarize = mRootUrl + "/upload/notarize";
    public static String apiUploadPath = mRootUrl + "/upload/path";

    // 用户分类Api接口
    public static String apiCategoryAdd = mRootUrl + "/category/v1/add";
    public static String apiCategoryAll = mRootUrl + "/category/v1/all";
    public static String apiCategoryChild = mRootUrl + "/category/v1/child";
    public static String apiCategoryDel = mRootUrl + "/category/v1/del";
    public static String apiCategoryDetail = mRootUrl + "/category/v1/detail";
    public static String apiCategoryEdit = mRootUrl + "/category/v1/edit";

    // 用户电子书Api接口
    public static String apiBookAdd = mRootUrl + "/book/v1/add";
    public static String apiBookDel = mRootUrl + "/book/v1/del";
    public static String apiBookDetail = mRootUrl + "/book/v1/detail";
    public static String apiBookSync = mRootUrl + "/book/v1/sync";
}
