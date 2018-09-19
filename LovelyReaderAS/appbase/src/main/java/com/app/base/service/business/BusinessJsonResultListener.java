/**
 * 
 */
package com.app.base.service.business;

import com.google.gson.JsonObject;


/**
 * 
 * @author tianyu
 */
public interface BusinessJsonResultListener {
	
	/**
	 *  操作成功时的回调。
	 *
	 *  @param result  成功时的数据。
	 */
	public void OnSuccess(JsonObject jsonObject);

	/**
	 *  操作出错时的的回调。
	 *
	 *  @param error
	 */
	public void OnFail(Error error);
	
}
