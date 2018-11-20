package com.haoyang.lovelyreader.tre.http;

import com.google.gson.reflect.TypeToken;
import com.haoyang.lovelyreader.BuildConfig;
import com.haoyang.lovelyreader.tre.bean.ResponseBean;
import com.haoyang.lovelyreader.tre.event.OnTokenExpiredEvent;
import com.haoyang.lovelyreader.tre.helper.Global;
import com.mjiayou.trecorelib.http.callback.BaseCallback;
import com.mjiayou.trecorelib.json.JsonParser;
import com.mjiayou.trecorelib.util.LogUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import de.greenrobot.event.EventBus;

/**
 * 网络请求回调类
 */
public abstract class RequestCallback<T> extends BaseCallback<T> {

    //接口定义
    //SUCCESS(900, "请求成功"),
    //SERVER_ERR(901, "服务异常"),
    //PARAM_ERR(902, "参数异常"),
    //URL_404(903, "404异常"),
    //HTTP_METHOD_ERR(904, "HTTP请求方法异常"),
    //TOKEN_EXPIRE(905, "TOKEN过期");

    private final int CODE_SUCCESS = 900; // 请求成功
    private final int CODE_FAILURE_SERVICE = 901; // 服务异常
    private final int CODE_FAILURE_PARAM = 902; // 参数异常
    private final int CODE_FAILURE_404 = 903; // 404异常
    private final int CODE_FAILURE_HTTP = 904; // HTTP请求方法异常
    private final int CODE_FAILURE_TOKEN = 905; // TOKEN过期

    /**
     * 请求返回
     *
     * @param response 网络请求返回数据
     */
    @Override
    public void onResponse(String response) {
        ResponseBean<T> responseBean = null;
        try {
            responseBean = JsonParser.get().toObject(response, new TypeToken<ResponseBean<Object>>() {
            }.getType());
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
        }

        if (responseBean == null) {
            onFailure(TC_CODE_FAILURE_JSON, TC_MSG_FAILURE_JSON); // 解析失败
        } else {
            if (BuildConfig.DEBUG && Global.debugTokenExpired) {
                responseBean.setStatusCode(CODE_FAILURE_TOKEN);
                responseBean.setMsg("TOKEN过期");
            }
            switch (responseBean.getStatusCode()) {
                case CODE_SUCCESS:
                    if (getObjectType() == String.class) {
                        T object = null;
                        try {
                            object = responseBean.getData();
                        } catch (Exception e) {
                            LogUtils.printStackTrace(e);
                        }
                        onSuccess(CODE_SUCCESS, object);
                    } else {
                        responseBean = getResponseBean(response);
                        if (responseBean == null) {
                            onFailure(TC_CODE_FAILURE_JSON, TC_MSG_FAILURE_JSON);
                        } else {
                            onSuccess(CODE_SUCCESS, responseBean.getData());
                        }
                    }
                    break;
                case CODE_FAILURE_SERVICE:
                case CODE_FAILURE_PARAM:
                case CODE_FAILURE_404:
                case CODE_FAILURE_HTTP:
                case CODE_FAILURE_TOKEN:
                default:
                    if (responseBean.getMsg() == null) {
                        responseBean.setMsg("null");
                    }
                    onFailure(responseBean.getStatusCode(), responseBean.getMsg());
                    break;
            }
            // token过期发送event
            if (responseBean != null && responseBean.getStatusCode() == CODE_FAILURE_TOKEN) {
                EventBus.getDefault().post(new OnTokenExpiredEvent());
            }
        }
    }

    /**
     * getResponseBean
     */
    private ResponseBean<T> getResponseBean(String response) {
        ResponseBean<T> responseBean = null;
        try {
            if (getObjectType() != String.class) {
                Type type = getParameterizedType(ResponseBean.class, getObjectType());
                responseBean = JsonParser.get().toObject(response, type);
            }
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
        }
        return responseBean;
    }

    /**
     * getObjectType
     */
    private Type getObjectType() {
        ParameterizedType parameterizedType = ((ParameterizedType) this.getClass().getGenericSuperclass());
        if (parameterizedType != null) {
            return parameterizedType.getActualTypeArguments()[0];
        }
        return null;
    }

    /**
     * getParameterizedType
     */
    private ParameterizedType getParameterizedType(final Class raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}
