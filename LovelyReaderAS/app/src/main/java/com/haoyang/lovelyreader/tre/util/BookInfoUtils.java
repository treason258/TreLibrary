package com.haoyang.lovelyreader.tre.util;

import android.text.TextUtils;

import com.haoyang.lovelyreader.tre.helper.Configs;
import com.haoyang.reader.sdk.Book;
import com.haoyang.reader.sdk.BookInfoService;
import com.mjiayou.trecorelib.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by treason on 2018/10/27.
 */

public class BookInfoUtils {

    /**
     * getBookInfo
     */
    public static Book getBookInfo(BookInfoService bookInfoService, String filePath) {
        if (bookInfoService == null || TextUtils.isEmpty(filePath)) {
            return null;
        }

        Book book = new Book();
        book.path = filePath;
        bookInfoService.getBookInfo(book, "ePub");
        return book;
    }

    /**
     * getBookCover
     */
    public static String getBookCover(BookInfoService bookInfoService, String filePath, String coverFileName) {
        if (bookInfoService == null || TextUtils.isEmpty(filePath)) {
            return null;
        }

        InputStream inputStream = bookInfoService.getCoverInputStream(filePath);
        if (inputStream == null) {
            return null;
        }

        try {
//            AndroidInfoService androidInfoService = new AndroidInfoService();
//            String documentPath;
//            try {
//                documentPath = androidInfoService.getDownLoadPath(mContext);
//            } catch (DeviceException e) {
//                e.printStackTrace();
//                return null;
//            }

            // 存放图片的目录
            File coverDirFile = new File(Configs.DIR_SDCARD_PROJECT_COVER);
            if (!coverDirFile.exists()) {
                if (!coverDirFile.mkdirs()) {
                    return null;
                }
            }

            // 图片文件
            File coverPathFile = new File(coverDirFile, coverFileName);
            if (coverPathFile.exists()) {
                coverPathFile.delete();
            }

            OutputStream outputStream = null;
            try {
                coverPathFile.createNewFile();
                outputStream = new FileOutputStream(coverPathFile);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                return coverPathFile.getAbsolutePath();
            } catch (IOException e) {
                LogUtils.printStackTrace(e);
                return null;
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        LogUtils.printStackTrace(e);
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LogUtils.printStackTrace(e);
            }
        }
        return null;
    }
}
