package com.haoyang.lovelyreader.tre.http;

import com.google.gson.reflect.TypeToken;
import com.haoyang.lovelyreader.tre.bean.BaseJsonBean;
import com.mjiayou.trecorelib.helper.GsonHelper;
import com.mjiayou.trecorelib.util.LogUtils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 网络请求回调类
 */
public abstract class RequestCallback<T> implements BaseCallback {

  // 预留接口，保留 onStart、onResult、onSuccess、onFailure、onException 流程
  // 请求开始调用 onStart（抽象）
  // 请求返回调用 onResult，由此分发 onSuccess（抽象）、onFailure（抽象）
  // 请求异常调用 onException，最终调用 code=CODE_EXCEPTION 的 onFailure

  public static final String TAG = RequestCallback.class.getSimpleName();

  //接口定义
  //SUCCESS(900, "请求成功"),
  //SERVER_ERR(901, "服务异常"),
  //PARAM_ERR(902, "参数异常"),
  //URL_404(903, "404异常"),
  //HTTP_METHOD_ERR(904, "HTTP请求方法异常"),
  //TOKEN_EXPIRE(905, "TOKEN过期");

  private final int CODE_SUCCESS = 900; // 请求成功

  private final int CODE_FAILURE_JSON = -1; // 解析失败
  private final int CODE_FAILURE_EXCEPTION = -2; // 解析失败
  private final int CODE_FAILURE_SERVICE = 901; // 服务异常
  private final int CODE_FAILURE_PARAM = 902; // 参数异常
  private final int CODE_FAILURE_404 = 903; // 404异常
  private final int CODE_FAILURE_HTTP = 904; // HTTP请求方法异常
  private final int CODE_FAILURE_TOKEN = 905; // TOKEN过期

  private final String MSG_FAILURE_JSON = "解析失败"; // 解析失败
  private final String MSG_FAILURE_EXCEPTION = "异常"; // 异常
  private final String MSG_FAILURE_SERVICE = "服务异常"; // 服务异常
  private final String MSG_FAILURE_PARAM = "参数异常"; // 参数异常
  private final String MSG_FAILURE_404 = "404异常"; // 404异常
  private final String MSG_FAILURE_HTTP = "HTTP请求方法异常"; // HTTP请求方法异常
  private final String MSG_FAILURE_TOKEN = "TOKEN过期"; // TOKEN过期

  /**
   * 请求返回
   *
   * @param response 网络请求返回数据
   */
  @Override public void onResult(String response) {
    BaseJsonBean<T> jsonBean = null;
    try {
      jsonBean = GsonHelper.get().fromJson(response, new TypeToken<BaseJsonBean<Object>>() {
      }.getType());
    } catch (Exception e) {
      LogUtils.printStackTrace(e);
    }

    boolean isString = false;
    if (jsonBean == null) {
      onFailure(CODE_FAILURE_JSON, MSG_FAILURE_JSON); // 解析失败
    } else {
      switch (jsonBean.getStatusCode()) {
        case CODE_SUCCESS:
          if (getResultType() == String.class) {
            isString = true;
          } else {
            jsonBean = jsonToBean(response);
          }
          if (jsonBean == null) {
            onFailure(CODE_FAILURE_JSON, MSG_FAILURE_JSON);
            return;
          }
          onSuccess(jsonBean.getStatusCode(), isString ? (T) response : jsonBean.getData());
          break;
        case CODE_FAILURE_SERVICE:
        case CODE_FAILURE_PARAM:
        case CODE_FAILURE_404:
        case CODE_FAILURE_HTTP:
        case CODE_FAILURE_TOKEN:
        default:
          if (jsonBean.getMsg() == null) {
            jsonBean.setMsg("null");
          }
          onFailure(jsonBean.getStatusCode(), jsonBean.getMsg());
          break;
      }
    }
  }

  /**
   * 请求异常
   *
   * @param ex 异常ex
   */
  @Override public void onException(Exception ex) {
    LogUtils.printStackTrace(ex);
    onFailure(CODE_FAILURE_EXCEPTION, MSG_FAILURE_EXCEPTION);
  }

  /**
   * 请求开始
   */
  public abstract void onStart();

  /**
   * 请求返回 - 成功
   *
   * @param code 状态码
   * @param obj 返回对象
   */
  public abstract void onSuccess(int code, T obj);

  /**
   * 请求返回 - 失败
   *
   * @param code 状态码
   * @param msg 错误信息
   */
  public abstract void onFailure(int code, String msg);

  /**
   * json转对象
   */
  protected BaseJsonBean<T> jsonToBean(String result) {
    BaseJsonBean<T> jsonBean = null;
    try {
      Type objectType = getParameterizedType(BaseJsonBean.class, getResultType());
      jsonBean = GsonHelper.get().fromJson(result, objectType);
    } catch (Exception e) {
      LogUtils.printStackTrace(e);
    }
    return jsonBean;
  }

  /**
   * getResultType
   */
  private Type getResultType() {
    return ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  /**
   * getParameterizedType
   */
  protected ParameterizedType getParameterizedType(final Class raw, final Type... args) {
    return new ParameterizedType() {

      public Type getRawType() {
        return raw;
      }

      public Type[] getActualTypeArguments() {
        return args;
      }

      public Type getOwnerType() {
        return null;
      }
    };
  }
}
