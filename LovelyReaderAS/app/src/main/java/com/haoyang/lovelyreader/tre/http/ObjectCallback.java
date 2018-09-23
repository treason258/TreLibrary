//package com.haoyang.lovelyreader.tre.http;
//
//import com.google.gson.reflect.TypeToken;
//import com.xin.framework.base.BaseCallback;
//import com.xin.framework.utils.LogUtils;
//import com.xin.sales.global.Global;
//
///**
// * Created by treason on 2017/5/12.
// */
//
//public abstract class ObjectCallback<T> implements BaseCallback {
//
//    /**
//     * 请求返回转成对应的Object
//     *
//     * @param obj 返回对象
//     */
//    public abstract void onObject(T obj) throws Exception;
//
//    @Override
//    public void onResult(String response) {
//        T obj = null;
//        try {
//            obj = Global.json.toObject(response, new TypeToken<T>() {
//            }.getType());
//
//            onObject(obj);
//        } catch (Exception e) {
//            LogUtils.printStackTrace(e);
//        }
//    }
//}
