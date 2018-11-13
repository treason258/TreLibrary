package com.haoyang.lovelyreader.tre.helper;

import com.haoyang.lovelyreader.tre.bean.CategoryBean;
import com.haoyang.lovelyreader.tre.bean.UserBean;

/**
 * Created by xin on 18/11/7.
 */

public class Global {

    public static UserBean mCurrentUser;
    public static CategoryBean mCurrentCategory;

    public static boolean mIsUploading = false;
    public static boolean mIsDownloading = false;
//
//    public synchronized static boolean isIsUploading() {
//        return mIsUploading;
//    }
//
//    public synchronized static void setIsUploading(boolean mIsUploading) {
//        Global.mIsUploading = mIsUploading;
//    }
//
//    public synchronized static boolean isDownloading() {
//        return mIsDownloading;
//    }
//
//    public synchronized static void setIsDownloading(boolean mIsDownloading) {
//        Global.mIsDownloading = mIsDownloading;
//    }
}
