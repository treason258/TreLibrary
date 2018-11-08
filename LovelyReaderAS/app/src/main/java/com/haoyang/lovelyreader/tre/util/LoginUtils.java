package com.haoyang.lovelyreader.tre.util;

import com.mjiayou.trecorelib.util.ToastUtils;
import com.mjiayou.trecorelib.util.UserUtils;

/**
 * Created by xin on 18/11/8.
 */

public class LoginUtils {

    /**
     * 如果未登录状态则toast提示
     */
    public static boolean checkNotLoginAndToast() {
        if (!UserUtils.checkLoginStatus()) {
            ToastUtils.show("请登录之后操作");
            return true;
        }
        return false;
    }
}
