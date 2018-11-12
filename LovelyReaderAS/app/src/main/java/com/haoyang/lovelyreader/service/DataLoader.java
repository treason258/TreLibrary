///**
// *
// */
//package com.haoyang.lovelyreader.service;
//
//import com.java.common.http.DefaultHttpCallBack;
//import com.java.common.http.HttpCallBack;
//import com.java.common.http.HttpService;
//import com.java.common.http.Parameter;
//import com.java.common.http.ProgressListener;
//import com.java.common.http.ResultListener;
//
///**
// * @author tianyu
// *
// */
//public class DataLoader {
//
//	private HttpService httpService;
//
//	private Parameter parameter;
//	private HttpCallBack callBack;
//
//	public DataLoader(Parameter parameter, ResultListener resultListener) {
//
//		httpService = new HttpService();
//
//		this.parameter = parameter;
//		this.callBack = new DefaultHttpCallBack(resultListener);
//	}
//
//	public DataLoader(Parameter parameter, ResultListener resultListener,
//			ProgressListener progressListener) {
//
//		httpService = new HttpService();
//
//		this.parameter = parameter;
//		this.callBack = new DefaultHttpCallBack(resultListener);
//	}
//
//	public void submit() {
//
//		new Thread() {
//
//			public void run() {
//
//				httpService.loadDataByHttpGet(parameter, callBack);
//			}
//		}.start();
//	}
//
//}
