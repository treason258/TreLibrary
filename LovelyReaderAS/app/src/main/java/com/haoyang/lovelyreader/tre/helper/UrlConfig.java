package com.haoyang.lovelyreader.tre.helper;

/**
 * Created by xin on 18/9/23.
 */

public class UrlConfig {

    // 接口地址
    private static String mHostRoot = "http://47.94.109.157";
    private static String mPathApi = "/api";
    private static String mPathDoc = "/doc";

    // APP版本升级 Api接口
    public static String apiAppUpgrade = mHostRoot + mPathApi + "/appupgrade/v1/check"; // 检查是否有新版本,不验证token，param为‘版本号‘参数，格式为1.0.0

    // Token Api接口
    public static String apiTokenTemp = mHostRoot + mPathApi + "/token/v1/temp"; // 生成临时token,不需要token信息,但是需要数字签名验证,param为‘随机数’参数

    // 意见反馈 Api接口
    public static String apiFeedbackAdd = mHostRoot + mPathApi + "/feedback/v1/add"; // 新增意见反馈,不需要有token信息

    // 用户模块 Api接口
    public static String apiUserFindPwd = mHostRoot + mPathApi + "/user/findPwd"; // 找回密码,不需要token信息
    public static String apiUserLogout = mHostRoot + mPathApi + "/user/logout"; // 退出登录
    public static String apiUserLogin = mHostRoot + mPathApi + "/user/v1/login"; // 用户登录,不需要token信息
    public static String apiUserRegister = mHostRoot + mPathApi + "/user/v1/register"; // 用户注册,不需要token信息

    // 短信模块 Api接口
    public static String apiSmsCheck = mHostRoot + mPathApi + "/sms/v1/check"; // 校验短信验证码,需要临时token
    public static String apiSmsSendchangepwdsms = mHostRoot + mPathApi + "/sms/v1/sendchangepwdsms"; // 发送修改密码短信验证码,需要临时token
    public static String apiSmsSendloginsms = mHostRoot + mPathApi + "/sms/v1/sendloginsms"; // 发送登陆短信验证码,需要临时token
    public static String apiSmsSendrigstersms = mHostRoot + mPathApi + "/sms/v1/sendrigstersms"; // 发送注册短信验证码,需要临时token

    // 用户分类Api接口
    public static String apiCategoryAdd = mHostRoot + mPathApi + "/category/v1/add"; // 新增分类
    public static String apiCategoryDel = mHostRoot + mPathApi + "/category/v1/del"; // 删除分类
    public static String apiCategoryEdit = mHostRoot + mPathApi + "/category/v1/edit"; // 修改分类
    public static String apiCategoryAll = mHostRoot + mPathApi + "/category/v1/all"; // 获取用户的所有分类
    public static String apiCategorySync = mHostRoot + mPathApi + "/category/v1/sync"; // 同步分类
    public static String apiCategoryChild = mHostRoot + mPathApi + "/category/v1/child"; // 获取单个分类下所有子分类
    public static String apiCategoryDetail = mHostRoot + mPathApi + "/category/v1/detail"; // 获取单个分类详情

    // 用户电子书Api接口
    public static String apiBookAdd = mHostRoot + mPathApi + "/book/v1/add"; // 新增电子书
    public static String apiBookDel = mHostRoot + mPathApi + "/book/v1/del"; // 删除电子书
    public static String apiBookSync = mHostRoot + mPathApi + "/book/v1/sync"; // 同步电子书
    public static String apiBookConfigPath = mHostRoot + mPathApi + "/book/v1/configPath"; // configBookPath
    public static String apiBookDetail = mHostRoot + mPathApi + "/book/v1/detail"; // 查看电子书详情


    // 客户端配置参数 Api接口
    public static String apiClientconfigAll = mHostRoot + mPathApi + "/clientconfig/v1/all"; // 获取所有客户端配置参数值,参数随意一个字符串,需要临时token
    public static String apiClientconfigOne = mHostRoot + mPathApi + "/clientconfig/v1/one"; // 获取单个客户端配置参数值,需要临时token


    // 文件上传Api接口
    public static String apiUploadBook = mHostRoot + mPathDoc + "/upload/book"; // 电子书上传
    public static String apiUploadDel = mHostRoot + mPathDoc + "/upload/del"; // 文档删除
    public static String apiUploadPath = mHostRoot + mPathDoc + "/upload/path"; // 文档访问地址,需要申请临时token
}
