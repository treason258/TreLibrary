///**
// *
// */
//package com.haoyang.lovelyreader.ui;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//
///**
// * @author tianyu
// *
// */
//public class ProgressDialogService {
//
//	private Activity activity;
//
//	private volatile ProgressDialog myProgress;
//	private String message;
//
//	public ProgressDialogService(String message, Activity activity) {
//
//		this.message = message;
//	}
//
//	public void show() {
//
//		myProgress = ProgressDialog.show(activity, null, message, true, false);
//
//	}
//
//	public void hide() {
//		myProgress.dismiss();
//		myProgress = null;
//	}
//
//}
