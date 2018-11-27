package com.haoyang.lovelyreader.tre.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;

import com.haoyang.lovelyreader.tre.bean.BookBean;
import com.haoyang.lovelyreader.tre.bean.UserBean;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * Created by xin on 18/10/8.
 */

public class Utils {

    /**
     * 获取程序 图标
     */
    public static Bitmap getAppIcon(Context context, String packname) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            BitmapDrawable bd = (BitmapDrawable) info.loadIcon(pm);
            Bitmap bm = bd.getBitmap();
            return bm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重命名文件
     *
     * @param oldPath 原来的文件地址
     * @param newPath 新的文件地址
     */
    public static void renameFile(String oldPath, String newPath) {
        File oleFile = new File(oldPath);
        File newFile = new File(newPath);
        //执行重命名
        oleFile.renameTo(newFile);
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String getBookName(UserBean userBean, BookBean.BookServerInfo bookServerInfo) {
        return userBean.getUid() + "-" + bookServerInfo.getAuthor() + "-" + bookServerInfo.getBookName() + ".epub";
    }


    /**
     * @param @param mobiles 手机号
     * @return boolean
     * @Description: 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188、17x
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][3456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }
}
