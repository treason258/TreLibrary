//package com.haoyang.lovelyreader.service;
//
//import java.util.Map;
//
//import com.app.base.service.business.AbstractBusinessService;
//import com.app.base.service.business.BusinessJsonResultListener;
//import com.app.base.service.business.JsonResultListener;
//import com.java.common.http.Parameter;
//
//public class AbstractService extends AbstractBusinessService {
//
//	protected void submit(Map<String, String> para, String command,
//			BusinessJsonResultListener businessResultListener) {
//
//		String url = DataInterfaceService.getUrl(command);
//
//		Parameter parameters = Parameter.buildParameter(url, para);
//
//		JsonResultListener resultListener = new JsonResultListener(
//				businessResultListener);
//
//		this.start(parameters, resultListener);
//	}
//
//	protected void submit(Map<String, String> para, String command,
//			BusinessJsonResultListener businessResultListener,
//			BusinessAspectListener aspectListener) {
//
//		String url = DataInterfaceService.getUrl(command);
//
//		Parameter parameters = Parameter.buildParameter(url, para);
//
//		JsonResultListener resultListener = new JsonResultListener(
//				businessResultListener);
//
//		this.start(parameters, resultListener, aspectListener);
//	}
//}
