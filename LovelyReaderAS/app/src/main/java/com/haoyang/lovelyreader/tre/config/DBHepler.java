package com.haoyang.lovelyreader.tre.config;

import android.text.TextUtils;

import com.haoyang.lovelyreader.tre.bean.UserBean;
import com.mjiayou.trecorelib.helper.GsonHelper;
import com.mjiayou.trecorelib.util.SharedUtils;

/**
 * Created by xin on 18/9/26.
 */

public class DBHepler {

    private static final String KEY_USER_BEAN = "key_user_bean";

    public static void setUserBean(UserBean userBean) {
        String data = "";
        if (userBean != null) {
            data = GsonHelper.get().toJson(userBean);
        }
        SharedUtils.get().setCommon(KEY_USER_BEAN, data);
    }

    public static UserBean getUserBean() {
        String data = SharedUtils.get().getCommon(KEY_USER_BEAN);
        if (!TextUtils.isEmpty(data)) {
            return GsonHelper.get().fromJson(data, UserBean.class);
        } else {
            // 如果当前没有用户，则设置默认用户
            UserBean userBean = new UserBean();
            userBean.setUid("0");
            userBean.setUserName("default");
            return userBean;
        }
    }
}
