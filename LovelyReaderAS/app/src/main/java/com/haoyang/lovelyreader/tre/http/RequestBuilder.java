package com.haoyang.lovelyreader.tre.http;

import com.mjiayou.trecorelib.util.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * 网络请求构建类
 */
public class RequestBuilder {

    public static final String TAG = RequestBuilder.class.getSimpleName();

    private static RequestBuilder mInstance;

    public RequestBuilder() {
    }

    /**
     * 单例模式获取实例
     */
    public static RequestBuilder get() {
        if (mInstance == null) {
            synchronized (RequestBuilder.class) {
                if (mInstance == null) {
                    mInstance = new RequestBuilder();
                }
            }
        }
        return mInstance;
    }

    // ******************************** OkHttp 网络请求 ********************************

    /**
     * 异步请求
     */
    public void send(RequestEntity requestEntity, final BaseCallback callback) {
        logRequest("send", requestEntity);

        switch (requestEntity.getMethod()) {
            case POST:
                OkHttpUtils
                        .postString()
                        .url(requestEntity.getUrl())
                        .content(requestEntity.getRequestBody())
                        .mediaType(MediaType.parse("application/json; charset=utf-8"))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onBefore(Request request, int id) {
                                super.onBefore(request, id);
                                if (callback != null) {
                                    callback.onStart();
                                }
                            }

                            @Override
                            public void onAfter(int id) {
                                super.onAfter(id);
                            }

                            @Override
                            public void inProgress(float progress, long total, int id) {
                                super.inProgress(progress, total, id);
                            }

                            @Override
                            public void onError(Call call, Exception ex, int id) {
                                if (callback != null) {
                                    callback.onException(ex);
                                }
                            }

                            @Override
                            public void onResponse(String responseData, int id) {
                                logResponse("send", responseData);

                                if (callback != null) {
                                    callback.onResult(responseData);
                                }
                            }
                        });
                break;
            //case GET:
            //OkHttpUtils
            //    .get()
            //    .url(requestEntity.getUrl())
            //    .params(paramWithApiKey(requestEntity).getParams())
            //    .build()
            //    .execute(new StringCallback() {
            //      @Override
            //      public void onBefore(Request request, int id) {
            //        super.onBefore(request, id);
            //        if (callback != null) {
            //          callback.onStart();
            //        }
            //      }
            //
            //      @Override
            //      public void onAfter(int id) {
            //        super.onAfter(id);
            //      }
            //
            //      @Override
            //      public void inProgress(float progress, long total, int id) {
            //        super.inProgress(progress, total, id);
            //      }
            //
            //      @Override
            //      public void onError(Call call, Exception e, int id) {
            //        if (callback != null) {
            //          callback.onException(e, RequestCallback.MSG_REQUEST_ERROR);
            //        }
            //      }
            //
            //      @Override
            //      public void onResponse(String responseData, int id) {
            //        logResponse("send", responseData);
            //
            //        if (callback != null) {
            //          callback.onResult(responseData);
            //        }
            //      }
            //    });
            //break;
            default:
                break;
        }
    }

    // ******************************** CookieStore ********************************

    // ******************************** 上传图片 ********************************

    // ******************************** 工具 ********************************

    /**
     * 网络请求打印LOG
     */
    private void logRequest(String method, RequestEntity requestEntity) {
        String requestInfo = method + " -> request_info | " + "\n" +
                "request_url -> " + requestEntity.getUrl() + "\n" +
                "request_method -> " + requestEntity.getMethod().toString() + "\n" +
                "request_body -> " + requestEntity.getRequestBody() + "\n" +
                "request_headers -> " + requestEntity.getHeaders() + "\n" +
                "request_params -> " + requestEntity.getParams() + "\n";
        LogUtils.d(TAG, requestInfo);
    }

    /**
     * 网络返回打印LOG
     */
    private void logResponse(String method, String responseData) {
        String responseInfo = method + " -> response_info | " + "\n" +
                "response_data -> " + responseData + "\n";
        LogUtils.d(TAG, responseInfo);
    }
}