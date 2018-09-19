/**
 * 
 */
package com.haoyang.lovelyreader.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

import com.app.base.service.business.BusinessJsonResultListener;
import com.haoyang.lovelyreader.entity.FeedBack;
import com.java.common.service.CommonKeys;

/**
 * 意见返馈.
 * 
 * @author tianyu
 *
 */
public class FeedBackService extends AbstractService {

	private Activity activity;

	public FeedBackService(Activity activity) {

		this.activity = activity;
	}

	/**
	 * 注册所需参数
	 * 
	 * @param username
	 * @param truename
	 * @param password
	 */
	public void feedBack(final FeedBack feedBack,
			BusinessJsonResultListener businessResultListener, BusinessAspectListener aspectListener) {

		Map<String, String> para = new HashMap<String, String>();

		para.put(CommonKeys.USERNAME, feedBack.title);
		para.put(CommonKeys.PASSWORD, feedBack.content);
		para.put(CommonKeys.PASSWORD, feedBack.contact);

		this.submit(para, DataInterfaceService.FEEDBACK, businessResultListener);
	}
}
