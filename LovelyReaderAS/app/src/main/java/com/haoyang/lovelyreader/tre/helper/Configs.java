package com.haoyang.lovelyreader.tre.helper;

import android.os.Environment;

import java.io.File;

/**
 * Created by xin on 18/10/9.
 */

public class Configs {
    public static final int HTTP_PORT = 12345;
    public static final String DIR_IN_SDCARD = "LovelyReader";
    public static final String DIR_IN_SDCARD_BOOK = "LovelyReader/book";
    public static final String DIR_IN_SDCARD_UPDATE = "LovelyReader/update";
    public static final File DIR_BOOK = new File(Environment.getExternalStorageDirectory() + File.separator + Configs.DIR_IN_SDCARD_BOOK);
    public static final String DIR_UPDATE = Environment.getExternalStorageDirectory() + File.separator + Configs.DIR_IN_SDCARD_UPDATE;
}
