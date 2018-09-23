//package com.haoyang.lovelyreader.tre;
//
//import android.app.Activity;
//import com.app.base.service.business.BusinessJsonResultListener;
//import com.google.gson.Gson;
//import com.haoyang.lovelyreader.entity.User;
//import com.haoyang.lovelyreader.service.AbstractService;
//import com.haoyang.lovelyreader.service.BusinessAspectListener;
//import com.haoyang.lovelyreader.service.DataInterfaceService;
//import com.haoyang.lovelyreader.tre.bean.UserLoginRequest;
//import com.java.common.service.CommonKeys;
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.StringCallback;
//import java.util.HashMap;
//import java.util.Map;
//import okhttp3.Call;
//import okhttp3.MediaType;
//import okhttp3.Request;
//
///**
// * Created by xin on 18/9/23.
// */
//
//public class RequestService extends AbstractService {
//
//  // 用户没有登录的时候，如果有所操作，那么就用这个做为当前操作者的标识
//  private final static String USER_ID = "Android";
//
//  public final static User user = new User();
//
//  static {
//    // 用户没有登录的时候,设置一个默认的id.
//    user.userId = USER_ID;
//  }
//
//  private Activity mActivity;
//
//  public RequestService(Activity activity) {
//    this.mActivity = activity;
//  }
//
//  /**
//   * 登陆
//   */
//  public void login(final UserLoginRequest userLoginRequestBean, BusinessJsonResultListener businessJsonResultListener, String message) {
//    //BusinessAspectListener aspectListener = new BusinessAspectListener(mActivity, message);
//    //Map<String, String> para = new HashMap<String, String>();
//    //para.put(CommonKeys.USERNAME, user.userName);
//    //para.put(CommonKeys.PASSWORD, user.password);
//    //submit(para, DataInterfaceService.LOGIN, businessJsonResultListener, aspectListener);
//
//    OkHttpUtils
//        .postString()
//        .url("")
//        .content("")
//        .mediaType(MediaType.parse("application/json; charset=utf-8"))
//        .build()
//        .execute(new StringCallback() {
//          @Override
//          public void onBefore(Request request, int id) {
//            super.onBefore(request, id);
//            if (callback != null) {
//              callback.onStart();
//            }
//          }
//
//          @Override
//          public void onAfter(int id) {
//            super.onAfter(id);
//          }
//
//          @Override
//          public void inProgress(float progress, long total, int id) {
//            super.inProgress(progress, total, id);
//          }
//
//          @Override
//          public void onError(Call call, Exception e, int id) {
//            if (callback != null) {
//              callback.onException(e, RequestCallback.MSG_REQUEST_ERROR);
//            }
//          }
//
//          @Override
//          public void onResponse(String responseData, int id) {
//            logResponse("send", responseData);
//
//            if (callback != null) {
//              callback.onResult(responseData);
//            }
//          }
//        });
//  }
//}
