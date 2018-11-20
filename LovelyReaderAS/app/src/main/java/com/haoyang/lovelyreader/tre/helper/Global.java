package com.haoyang.lovelyreader.tre.helper;

import com.haoyang.lovelyreader.tre.bean.CategoryBean;
import com.haoyang.lovelyreader.tre.bean.UserBean;

/**
 * Created by xin on 18/11/7.
 */

public class Global {

    public static UserBean mCurrentUser; // 当前登录用户
    public static CategoryBean mCurrentCategory; // 当前选择分类

    public static boolean mIsUploading = false; // 正在上传标记位
    public static boolean mIsDownloading = false; // 正在下载标记位

    public static String maxFileSize; // 可以上传的文件大小最大值

    public static boolean debugTokenExpired = false;
}
