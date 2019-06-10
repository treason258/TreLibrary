package com.haoyang.lovelyreader.tre.util;

import com.haoyang.lovelyreader.tre.helper.Configs;
import com.mjiayou.trecorelib.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by treason on 2018/10/27.
 */

public class BookInfoUtils {

//    /**
//     * getBookInfo
//     */
//    public static Book getBookInfo(BookInfoService bookInfoService, String filePath) {
//        if (bookInfoService == null || TextUtils.isEmpty(filePath)) {
//            return null;
//        }
//
//        Book book = new Book();
//        book.path = filePath;
//        bookInfoService.getBookInfo(book, "ePub");
//        return book;
//    }

    /**
     * getBookCover
     */
    public static String getBookCover(byte[] coverData, String coverFileName) {
//        if (bookInfoService == null || TextUtils.isEmpty(filePath)) {
//            return null;
//        }

//        InputStream inputStream = bookInfoService.getCoverInputStream(filePath);
//        if (inputStream == null) {
//            return null;
//        }

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

            try {
                FileOutputStream fos = new FileOutputStream(coverDirFile);
                fos.write(coverData, 0, coverData.length);
                fos.flush();
                fos.close();
                return coverPathFile.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
        }
        return null;
    }
}
