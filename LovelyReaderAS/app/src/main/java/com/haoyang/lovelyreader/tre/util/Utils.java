package com.haoyang.lovelyreader.tre.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

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

}
