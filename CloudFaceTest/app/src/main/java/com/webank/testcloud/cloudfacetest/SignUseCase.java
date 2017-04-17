package com.webank.testcloud.cloudfacetest;

import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;


/**
 * Created by leoraylei on 16/9/19.
 */

public class SignUseCase {
    private static final String TAG = "SignUseCase";

    private ExecutorService service;
    private AppHandler handler;
    private OkHttpClient httpClient = new OkHttpClient();

    public SignUseCase(ExecutorService service, AppHandler handler) {
        this.service = service;
        this.handler = handler;
    }

    public void execute(final String mode, String appId, String userId, String nonce) {
        final String url = getUrl(appId, userId, nonce);
        service.submit(new Runnable() {

            @Override
            public void run() {
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) { //请求成功
                        String body = response.body().string();
                        processBody(mode, body);
                    } else {
                        requestError(response.code(), response.message());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    requestError(AppHandler.ERROR_LOCAL, "本地操作出错:" + e.getMessage());
                }
            }
        });
    }

    private void requestError(int code, String message) {
        Log.d(TAG, "签名请求失败:code=" + code + ",message=" + message);
        handler.sendSignError(code, message);
    }

    private void processBody(String mode, String body) {
        try {
            JSONObject object = new JSONObject(body);
            String sign = object.optString("sign", "");
            if (TextUtils.isEmpty(sign) || "null".equals(sign)) {
                handler.sendSignError(AppHandler.ERROR_DATA, "sign is null.");
            } else {
                Log.d(TAG, "签名请求成功:" + body);
                handler.sendSignSuccess(mode, sign);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendSignError(AppHandler.ERROR_DATA, "返回数据格式错误");
        }

    }

    private String getUrl(String appId, String userId, String nonce) {
        final String s = "https://ida.webank.com/" + "/ems-partner/cert/signature?appid=" + appId + "&nonce=" + nonce + "&userid=" + userId;
        Log.d(TAG, "get sign url=" + s);
        return s;
    }
}
