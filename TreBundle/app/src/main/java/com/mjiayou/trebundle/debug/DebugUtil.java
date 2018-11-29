package com.mjiayou.trebundle.debug;

import android.content.Context;

import com.mjiayou.trebundle.BuildConfig;
import com.mjiayou.trebundle.R;
import com.mjiayou.trecore.common.Configs;
import com.mjiayou.trecore.util.ConvertUtil;
import com.mjiayou.trecorelib.util.AppUtil;

/**
 * Created by treason on 2016/12/16.
 */

public class DebugUtil {

    public static String getBuildConfigInfo(Context context) {
        StringBuilder builder = new StringBuilder();

        builder.append("\n");
        builder.append("**************** getBuildConfigInfo ****************").append("\n");

        builder.append("\n");
        builder.append("BuildConfig = ").append(BuildConfig.class.getName()).append("\n");
        builder.append("BuildConfig.DEBUG = ").append(BuildConfig.DEBUG).append("\n");
        builder.append("BuildConfig.APPLICATION_ID = ").append(BuildConfig.APPLICATION_ID).append("\n");
        builder.append("BuildConfig.BUILD_TYPE = ").append(BuildConfig.BUILD_TYPE).append("\n");
        builder.append("BuildConfig.FLAVOR = ").append(BuildConfig.FLAVOR).append("\n");
        builder.append("BuildConfig.VERSION_CODE = ").append(BuildConfig.VERSION_CODE).append("\n");
        builder.append("BuildConfig.VERSION_NAME = ").append(BuildConfig.VERSION_NAME).append("\n");

        builder.append("\n");
        builder.append("BuildConfig.GRADLE_DEBUG = ").append(BuildConfig.GRADLE_DEBUG).append("\n");
        builder.append("BuildConfig.GRADLE_OFFICIAL = ").append(BuildConfig.GRADLE_OFFICIAL).append("\n");
        builder.append("BuildConfig.GRADLE_BUILD_TYPES_NAME = ").append(BuildConfig.GRADLE_BUILD_TYPES_NAME).append("\n");
        builder.append("BuildConfig.GRADLE_BUILD_TYPES_VALUE = ").append(BuildConfig.GRADLE_BUILD_TYPES_VALUE).append("\n");
        builder.append("BuildConfig.GRADLE_PRODUCT_FLAVORS_NAME = ").append(BuildConfig.GRADLE_PRODUCT_FLAVORS_NAME).append("\n");
        builder.append("BuildConfig.GRADLE_PRODUCT_FLAVORS_VALUE = ").append(BuildConfig.GRADLE_PRODUCT_FLAVORS_VALUE).append("\n");

        builder.append("\n");
        builder.append("gradle_app_name = ").append(context.getString(R.string.gradle_app_name)).append("\n");
        builder.append("gradle_build_types_name = ").append(context.getString(R.string.gradle_build_types_name)).append("\n");
        builder.append("gradle_build_types_value = ").append(context.getString(R.string.gradle_build_types_value)).append("\n");
        builder.append("gradle_product_flavors_name = ").append(context.getString(R.string.gradle_product_flavors_name)).append("\n");
        builder.append("gradle_product_flavors_value = ").append(context.getString(R.string.gradle_product_flavors_value)).append("\n");

        builder.append("\n");
        builder.append("gradle_channel_name = ").append(AppUtil.getMetaValue(context, Configs.META_UMENG_CHANNEL)).append("\n");
        builder.append("gradle_tc_key = ").append(AppUtil.getMetaValue(context, Configs.META_TC_KEY)).append("\n");
        builder.append("gradle_umeng_appkey = ").append(AppUtil.getMetaValue(context, Configs.META_UMENG_APPKEY)).append("\n");

        builder.append("\n");
        builder.append("**** BuildConfig.class ****").append("\n");
        builder.append(ConvertUtil.parseString(BuildConfig.class));

        return builder.toString();
    }
}
