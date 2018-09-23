package com.haoyang.lovelyreader.tre.util;

import com.google.gson.Gson;

/**
 * Created by xin on 18/9/23.
 */
public class GsonUtil {

  private static Gson mGson;

  public static Gson get() {
    if (mGson == null) {
      mGson = new Gson();
    }
    return mGson;
  }
}
