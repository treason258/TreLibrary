///**
// *
// */
//package com.haoyang.lovelyreader.service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import android.app.Activity;
//
//import com.app.base.service.business.BusinessJsonResultListener;
//import com.java.common.service.CommonKeys;
//
///**
// * 数据同步下来，需要下载的都列出来，用户需要那个就手动去下载那个。
// *
// * @author tianyu
// *
// */
//public class DataSynService extends AbstractService {
//
//	private Activity activity;
//
//	public DataSynService(Activity activity) {
//		this.activity = activity;
//	}
//
//	/**
//	 * 拿 token 去同步数据。
//	 *
//	 * @param username
//	 * @param truename
//	 * @param password
//	 */
//	public void synData(final String token,
//			BusinessJsonResultListener businessJsonResultListener) {
//
//		Map<String, String> para = new HashMap<String, String>();
//
//		para.put(CommonKeys.USERNAME, token);
//
//		this.submit(para, DataInterfaceService.SYN, businessJsonResultListener);
//	}
//
//}
