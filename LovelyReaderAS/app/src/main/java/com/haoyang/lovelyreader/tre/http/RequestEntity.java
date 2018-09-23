package com.haoyang.lovelyreader.tre.http;

import android.text.TextUtils;

import com.mjiayou.trecorelib.helper.GsonHelper;
import com.mjiayou.trecorelib.util.LogUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * 网络请求参数实体类
 */
public class RequestEntity implements Serializable {

    private static final long serialVersionUID = -2329339308310833689L;

    private boolean enableCookie = false;
    private RequestMethod method;
    private String url = "";
    private String requestBody = "";
    private JSONObject jsonObject = new JSONObject();
    private Map<String, String> headers = new TreeMap<>();
    private Map<String, String> params = new TreeMap<>();
    private Map<String, File> files = new TreeMap<>();
    private String filePath;

    /**
     * 构造函数
     */
    public RequestEntity(String url) {
        this.enableCookie = false; // 默认不保存cookie
        this.method = RequestMethod.POST; // 默认post请求
        this.url = url;

        // 公共参数
        //headers.put("key", "value");
        //params.put(Key.ParamKey.TOKEN_CITY_ID, String.valueOf(User.getInstance().cityId));
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    // getter and setter

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod methodCode) {
        this.method = methodCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setRequestBody(Object object) {
        if (object != null) {
            this.requestBody = GsonHelper.get().toJson(object);
        }
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, File> getFiles() {
        return files;
    }

    public void setFiles(Map<String, File> files) {
        this.files = files;
    }

    public boolean isEnableCookie() {
        return enableCookie;
    }

    public void setEnableCookie(boolean enableCookie) {
        this.enableCookie = enableCookie;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // operation

    public void addHeader(String key, String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        headers.put(key, value);
    }

    public void addParam(String key, String value) {
        if (value == null) {
            value = "";
        }
        params.put(key, value);
    }

    public void addFile(String name, File file) {
        if (file == null) {
            return;
        }
        files.put(name, file);
    }

    public void addParams(Map<String, String> params) {
        this.params.putAll(params);
    }

    public void fillParams(Object obj) {
        Class<? extends Object> clazz = obj.getClass();
        ArrayList<Field> fieldList = new ArrayList<>();
        do {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);
        try {
            for (Field field : fieldList) {
                field.setAccessible(true);
                String name = field.getName();
                Object value = field.get(obj);
                if (!Modifier.isStatic(field.getModifiers()) && field.getType() != String[].class) {
                    if (value == null) {
                        params.put(name.toLowerCase(), "");
                    } else {
                        params.put(name.toLowerCase(), String.valueOf(value));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            LogUtils.printStackTrace(e);
        } catch (IllegalArgumentException e) {
            LogUtils.printStackTrace(e);
        }
    }
}