package com.haoyang.reader;

import android.view.ViewConfiguration;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.os.Build.VERSION;


public class NavigationBarService {

    // 获取NavigationBar的高度
    public static int getNavigationBarHeight(Context var0) {

        boolean var1 = ViewConfiguration.get(var0).hasPermanentMenuKey();
        int var2;
        return (var2 = var0.getResources().getIdentifier("navigation_bar_height", "dimen", "android")) > 0 && !var1?var0.getResources().getDimensionPixelSize(var2):0;
    }

    // 判断是否显示NavigationBar
    public static boolean isNavigationBarShowing(Context var0) {
        if(!hasNavigationBar(var0)) {
            return false;
        } else {
            if(VERSION.SDK_INT >= 17) {
//                String var1;
//                if(SysUtilsNew.isXiaomi()) {
//                    var1 = "force_fsg_nav_bar";
//                    if(Global.getInt(var0.getContentResolver(), var1, 0) != 0) {
//                        return false;
//                    }
//
//                    return true;
//                }
//
//                if(SysUtilsNew.isVivo()) {
//                    var1 = "navigation_gesture_on";
//                    if(Secure.getInt(var0.getContentResolver(), var1, 0) != 0) {
//                        return true;
//                    }
//
//                    return false;
//                }
            }

            return true;
        }
    }

    // 判断是否支持NavigationBar
    public static boolean hasNavigationBar(Context var0) {

        boolean var1 = false;
        int var2;

        Resources var4;

        if((var2 = (var4 = var0.getResources()).getIdentifier("config_showNavigationBar", "bool", "android")) > 0) {

            var1 = var4.getBoolean(var2);
        }

        try {
            Class var5;
            String var6 = (String)(var5 = Class.forName("android.os.SystemProperties")).getMethod("get", new Class[]{String.class}).invoke(var5, new Object[]{"qemu.hw.mainkeys"});
            if("1".equals(var6)) {
                var1 = false;
            } else if("0".equals(var6)) {
                var1 = true;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return var1;
    }


}
