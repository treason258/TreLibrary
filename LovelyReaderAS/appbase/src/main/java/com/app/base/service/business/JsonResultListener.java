/**
 * 
 */
package com.app.base.service.business;

import java.io.UnsupportedEncodingException;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.java.common.http.HttpConnectionResult;
import com.java.common.http.ResultListener;
import com.java.common.service.CommonKeys;
import com.java.common.service.GsonParserService;

/**
 * 
 * 结果集为Json格式的监听器。
 * 
 * @author tianyu
 *
 */
public class JsonResultListener implements ResultListener {

	public final static String tag = JsonResultListener.class.getName();

	private BusinessJsonResultListener businessJsonResultListener;

	public JsonResultListener(
			BusinessJsonResultListener businessJsonResultListener) {
		super();
		this.businessJsonResultListener = businessJsonResultListener;
	}

	/**
	 * 操作成功时的回调。
	 *
	 * @param result
	 *            成功时的数据。
	 */
	public void OnSuccess(byte[] result) {
	
		String resultUTF8 = null;
		try {
			resultUTF8 = new String(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		GsonParserService gsonParserService = new GsonParserService();

		JsonObject jsonObject = gsonParserService.toJsonOjbect(resultUTF8);
		JsonElement element = jsonObject.get(CommonKeys.STATUS);
		if (element == null) {

			Error error = new Error();
			error.errorType = ErrorType.ServerError;
			// error.errorCode = arg0.statusCode;
			// error.message

			this.businessJsonResultListener.OnFail(error);
			return;
		}

		int code = element.getAsInt();
		
		if (code == 0) {
			this.businessJsonResultListener.OnSuccess(jsonObject);
			return;
		}

		JsonElement messageElement = jsonObject.get(CommonKeys.MESSAGE);
		
		Error error = new Error();
		if (messageElement != null) {
			error.message = messageElement.getAsString();
		}
		
		error.errorType = ErrorType.ServerError;
		error.errorCode = code;
		// error.message
		this.businessJsonResultListener.OnFail(error);
	}

	/**
	 * 操作出错时的的回调。
	 *
	 * @param error
	 */
	public void OnFail(HttpConnectionResult arg0) {

		if (this.businessJsonResultListener == null) {
			Log.i(tag, "None businessJsonResultListener !!");
			return;
		}
		
		Error error = new Error();
		error.errorType = ErrorType.HttpError;
		error.errorCode = arg0.statusCode;
		error.message = arg0.message;

		this.businessJsonResultListener.OnFail(error);
	}
}
