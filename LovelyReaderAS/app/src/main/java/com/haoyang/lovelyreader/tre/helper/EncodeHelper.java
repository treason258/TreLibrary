package com.haoyang.lovelyreader.tre.helper;

import android.text.TextUtils;

import com.mjiayou.trecorelib.util.LogUtils;
import com.mjiayou.trecorelib.util.MD5Utils;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by xin on 18/9/29.
 */

public class EncodeHelper {

    // TAG
    protected static final String TAG = "EncodeHelper";

    private static final String MD5_SECRET = "111";
    private static final String KEY_RANDOM_CHAR = "randomChar";

    // ******************************** 获取sign ********************************

    private static String getSign(String key) {
        return MD5Utils.md5(MD5_SECRET + key + MD5_SECRET);
    }

    // ******************************** 获取临时token ********************************

    private static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(base.length());
            builder.append(base.charAt(index));
        }
        return builder.toString();
    }

    public static String getRandomChar() {
        return getRandomString(6); // UUID.randomUUID().toString();
    }

    public static String getSignForToken(String randomChar) {
        return getSign(KEY_RANDOM_CHAR + "=" + randomChar);
    }

    // ******************************** 公共参数 ********************************

    private static TreeMap<String, String> getParams(String token, String type, String jsonData) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("token", token);
        params.put("type", type); // LOGIN_TOKEN("1", "登陆token"), TEMP_TOKEN("2", "临时token");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("terminal", "ANDROID_PHONE");
        params.put("jsonData", jsonData);
        return params;
    }

    private static String getParamStr(TreeMap<String, String> params) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!TextUtils.isEmpty(builder.toString())) {
                builder.append("&");
            }
            builder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return builder.toString();
    }

    public static String getUrlWithSign(String url, String token, String type, String jsonData) {
        TreeMap<String, String> params = getParams(token, type, jsonData);
        String paramStr = getParamStr(params);
        LogUtils.d(TAG, "paramStr -> " + paramStr);

        String paramStr2 = paramStr.replace("&", "");
        LogUtils.d(TAG, "paramStr2 -> " + paramStr2);

        String sign = getSign(paramStr2);
        LogUtils.d(TAG, "sign -> " + sign);

        params.put("sign", sign);
        String paramStrWithSign = getParamStr(params);
        LogUtils.d(TAG, "paramStrWithSign -> " + paramStrWithSign);

        String urlWithSign = url + "?" + paramStrWithSign;
        LogUtils.d(TAG, "urlWithSign -> " + urlWithSign);
        return urlWithSign;
    }
}