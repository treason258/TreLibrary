package com.haoyang.lovelyreader.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

import com.app.base.service.business.BusinessJsonResultListener;
import com.haoyang.lovelyreader.entity.User;
import com.java.common.service.CommonKeys;

/**
 * 
 * 注册的服务类。
 * 
 * @author qyl
 */
public class UserService extends AbstractService {

	// 用户没有登录的时候，如果有所操作，那么就用这个做为当前操作者的标识
	private final static String USER_ID = "Android";

	public final static User user = new User();

	static {

		// 用户没有登录的时候,设置一个默认的id.
		user.userId = USER_ID;
	}

	private Activity activity;

	public UserService(Activity activity) {
		this.activity = activity;
	}

	/**
	 * 注册所需参数
	 * 
	 * @param username
	 * @param truename
	 * @param password
	 */
	public void login(final User user,
			BusinessJsonResultListener businessJsonResultListener,
			String message) {

		BusinessAspectListener aspectListener = new BusinessAspectListener(
				this.activity, message);

		Map<String, String> para = new HashMap<String, String>();

		para.put(CommonKeys.USERNAME, user.userName);
		para.put(CommonKeys.PASSWORD, user.password);

		this.submit(para, DataInterfaceService.LOGIN,
				businessJsonResultListener, aspectListener);
	}

	/**
	 * 注册所需参数
	 * 
	 * @param username
	 * @param truename
	 * @param password
	 */
	public void findPassword(final User user, final String checkCode,
			BusinessJsonResultListener businessJsonResultListener,
			String message) {

		BusinessAspectListener aspectListener = new BusinessAspectListener(
				this.activity, message);

		Map<String, String> para = new HashMap<String, String>();

		para.put(CommonKeys.USERNAME, user.userName);
		para.put(CommonKeys.PASSWORD, user.password);

		this.submit(para, DataInterfaceService.FINDPASSWORD,
				businessJsonResultListener, aspectListener);
	}

	/**
	 * 注册所需参数
	 * 
	 * @param username
	 * @param truename
	 * @param password
	 */
	public void register(final User user, String mobilCode, String pictureCode,
			BusinessJsonResultListener businessJsonResultListener,
			String message) {

		BusinessAspectListener aspectListener = new BusinessAspectListener(
				this.activity, message);

		Map<String, String> para = new HashMap<String, String>();

		para.put(CommonKeys.USERNAME, user.userName);
		para.put(CommonKeys.PASSWORD, user.password);
		para.put(CommonKeys.PASSWORD, mobilCode);
		para.put(CommonKeys.PASSWORD, pictureCode);

		this.submit(para, DataInterfaceService.REGISTER,
				businessJsonResultListener, aspectListener);
	}

}
