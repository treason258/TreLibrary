///**
// *
// */
//package com.haoyang.lovelyreader.ui;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.Window;
//
//import com.app.base.service.android.PageStarterService;
//import com.app.base.service.android.SharedPreferenceService;
//import com.app.base.service.business.BusinessJsonResultListener;
//import com.app.base.service.business.Error;
//import com.google.gson.JsonObject;
//import com.haoyang.lovelyreader.R;
//import com.haoyang.lovelyreader.service.DataSynService;
//import com.java.common.service.CommonKeys;
//
///**
// * @author tianyu
// *
// */
//public class StartUpActivity extends Activity implements
//		BusinessJsonResultListener {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		// 去掉TitleBar
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//		setContentView(R.layout.file_browse);
//
//		SharedPreferenceService sharedPreferenceService = new SharedPreferenceService(
//				this);
//		String token = sharedPreferenceService.getValue(CommonKeys.TOKEN, "");
//		if (token == null || "".equals(token)) {
//
//			nextPage("login"); // 打开登录框
//			return;
//		}
//
//		loadData(token);
//	}
//
//	private void loadData(String token) {
//
//		DataSynService dataSynService = new DataSynService(this);
//		dataSynService.synData(token, this);
//	}
//
//	@Override
//	public void OnSuccess(JsonObject jsonObject) {
//
//		// 是否有新版本。
//
//		// 执行升级.
//
//		// 查看token有效情况
//		// token 失效，弹出登录框
//
//		// 有数据更新数据，然后更新界面。
//		// 数据同步下来之后，存入数据库，文件下载下来。
//
//		boolean token = false;
//
//		String nextPage = "Main";
//		if (!token) {
//
//			nextPage = "login";
//		}
//
//		nextPage(nextPage);
//	}
//
//	@Override
//	public void OnFail(Error error) {
//
//		// 进入到main，在main中提示出错。
//
//		String pageName = "error";
//
//		nextPage(pageName);
//	}
//
//	private void nextPage(String pageName) {
//
//		PageStarterService pageStarterService = new PageStarterService(this);
//		pageStarterService.nextPage(MainActivity.class, "Next", pageName);
//	}
//}
