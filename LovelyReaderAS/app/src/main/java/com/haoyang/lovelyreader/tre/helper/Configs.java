package com.haoyang.lovelyreader.tre.helper;

import android.os.Environment;

import java.io.File;

/**
 * Created by xin on 18/10/9.
 */

public class Configs {
    public static final int HTTP_PORT = 12345;
    public static final String DIR_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DIR_SDCARD_PROJECT = DIR_SDCARD + "/LovelyReader";
    public static final String DIR_SDCARD_PROJECT_BOOK = DIR_SDCARD_PROJECT + "/book";
    public static final String DIR_SDCARD_PROJECT_UPDATE = DIR_SDCARD_PROJECT + "/update";
    public static final String DIR_SDCARD_PROJECT_COVER = DIR_SDCARD_PROJECT + "/cover";

//    public static final File FILE_SDCARD_PROJECT_BOOK = new File(DIR_SDCARD_PROJECT_BOOK);

    public static final String CATEGORY_ROOT = "-1";
    public static final String CATEGORY_DEFAULT = "1";
}
