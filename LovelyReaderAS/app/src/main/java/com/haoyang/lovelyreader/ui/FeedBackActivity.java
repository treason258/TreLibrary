///**
// *
// */
//package com.haoyang.lovelyreader.ui;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.app.base.service.business.BusinessJsonResultListener;
//import com.app.base.service.business.Error;
//import com.google.gson.JsonObject;
//import com.haoyang.lovelyreader.R;
//import com.haoyang.lovelyreader.entity.FeedBack;
//import com.haoyang.lovelyreader.entity.User;
//import com.haoyang.lovelyreader.service.BusinessAspectListener;
//import com.haoyang.lovelyreader.service.FeedBackService;
//
///**
// * @author tianyu
// *
// */
//public class FeedBackActivity extends Activity implements
//		BusinessJsonResultListener {
//
//	// private List<String> imagePathList = new ArrayList<String>();
//
//	private EditText content = null;
//	private EditText contact = null;
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
//		TextView submitView = (TextView) this.findViewById(0);
//		submitView.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				boolean result = checkData(null);
//
//				if (!result) {
//					return;
//				}
//
//				submit(null);
//			}
//		});
//
//	}
//
//	private boolean checkData(User user) {
//
//		String content = this.content.getText().toString();
//
//		if (content == null || "".equals(content)) {
//
//			// 让编缉框振动
//
//			return false;
//		}
//
//		if (user == null) {
//
//			String contact = this.contact.getText().toString();
//			if (contact == null || "".equals(contact)) {
//
//				// 让编缉框振动
//				return false;
//			}
//		}
//
//		return true;
//	}
//
//	private FeedBack makeFeedBack(User user) {
//
//		String content = this.content.getText().toString();
//		String contact = this.contact.getText().toString();
//
//		FeedBack feedBack = new FeedBack();
//
//		if (user != null) {
//
//			feedBack.contact = user.userName;
//		} else {
//
//			feedBack.contact = contact;
//		}
//
//		feedBack.content = content;
//
//		return feedBack;
//	}
//
//	private void submit(User user) {
//
//		FeedBack feedBack = this.makeFeedBack(user);
//
//		FeedBackService feedBackService = new FeedBackService(this);
//
//		String message = "正在保存意见";
//
//		BusinessAspectListener aspectListener = new BusinessAspectListener(
//				this, message);
//		feedBackService.feedBack(feedBack, this, aspectListener);
//	}
//
//	@Override
//	public void OnSuccess(JsonObject jsonObject) {
//
//		// 提交成功
//
//		finish();
//
//	}
//
//	@Override
//	public void OnFail(Error error) {
//
//		// 提示失败.
//
//
//		finish();
//	}
//
//}
