/**
 * 
 */
package com.haoyang.reader.popup;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.app.base.service.android.AndroidAppService;
import com.app.base.ui.AnimatorDefaultListener;

/**
 * 阅读内的意见反馈功能。
 * 
 * @author tianyu912@yeah.net
 */
public class FeedBackPopup extends PopupPanel {

	private EditText contentEditText, contactEditText;

	public final static String ID = "FeedBackPopup";

	private int feedbackAreaId;
	private int feedbackMengId;

	private int feedBackCancelId;
	private int feedBackFinishId;

	private AndroidAppService androidAppService;

	public FeedBackPopup(ReaderPopupService readerPopupService) {

		super(ID, readerPopupService);
	}

	public void initUI(RelativeLayout relativeLayout) {

		if (this.windowView != null && this.activity == this.windowView.getContext()) {
			return;
		}

		this.androidAppService = new AndroidAppService(activity);

		int panel_feed_backLayout = this.androidAppService.getLayoutResource("hy_panel_feed_back");

		// 加载弹出的选择框布局。
		activity.getLayoutInflater().inflate(panel_feed_backLayout, relativeLayout);

		int feed_back_panel = this.androidAppService.getIdResource("feed_back_panel");

		this.windowView = relativeLayout.findViewById(feed_back_panel);

		int feedBackContentId = androidAppService.getIdResource("feedBackContent");
		int feedBackcontactId = androidAppService.getIdResource("feedBackcontact");

		contentEditText = (EditText) this.windowView.findViewById(feedBackContentId);
		contactEditText = (EditText) this.windowView.findViewById(feedBackcontactId);

		feedBackCancelId = androidAppService.getIdResource("feedBackCancel");
		feedBackFinishId = androidAppService.getIdResource("feedBackFinish");

		feedbackAreaId = androidAppService.getIdResource("feedbackAreaHY");
		feedbackMengId = androidAppService.getIdResource("feedbackMengHY");

		this.setPosition(-10000, -10000);

		setupButton(feedbackAreaId, windowView);
		setupButton(feedbackMengId, windowView);

		setupButton(feedBackCancelId, windowView);
		setupButton(feedBackFinishId, windowView);
	}


	private ObjectAnimator objectAnimator;
	private FeedBackAnimatorListener feedBackAnimatorListener = new FeedBackAnimatorListener();

	@Override
	public void show() {

		final View view = windowView.findViewById(feedbackAreaId);
		view.bringToFront();

		int height = view.getHeight();
		int screenHeight = this.configServiceSDK.getScreenHeight();

		view.setY(screenHeight); // 让界面从下边平移到指定位置。
		objectAnimator = ObjectAnimator.ofFloat(view, "Y", screenHeight
				- height);
		objectAnimator.setDuration(300);
		objectAnimator.start();
	}

	@Override
	public void hide() {

		if (windowView == null) {
			return;
		}

		final View view = windowView.findViewById(feedbackAreaId);
		int screenHeight = this.configServiceSDK.getScreenHeight();

		objectAnimator = ObjectAnimator.ofFloat(view, "Y", screenHeight);
		objectAnimator.addListener(feedBackAnimatorListener);
		objectAnimator.setDuration(300);
		objectAnimator.start();
	}

	public void onClick(View view) {

		if (view.getId() == feedbackAreaId) {
			return;
		} else if (view.getId() == feedbackMengId) {

			hideSoftKeyword(this.activity, contentEditText);
		}
		if (view.getId() == feedBackFinishId) {

//			final String content = contentEditText.getText().toString();
//			if ("".equals(content.trim())) {
//				return;
//			}
//
//			final String contact = contactEditText.getText().toString();
//			if ("".equals(contact.trim())) {
//				return;
//			}
//
//			final String bookId = ReaderService.Instance().bookModel.bookEntity.bookId;
//
//			FeedBack feedBack = new FeedBack();
//			feedBack.bookId = bookId;
//			feedBack.userId = ReaderService.Instance().readerSDKParameterInfo.userId;
//			feedBack.content = content;
//			feedBack.contact = contact;
//
//			ReaderDeveloperService.getInstance().feedback(feedBack);
//
//			new Thread() {
//				public void run() {
//					saveFeedBack(bookId, content, contact); // 调用本地代码存保数据到服务器。
//				}
//			}.start();
		}

		hideSoftKeyword(this.activity, contentEditText);

		this.readerPopupService.hideCurrentPopup();
	}

	class FeedBackAnimatorListener extends AnimatorDefaultListener {

		public void onAnimationEnd(Animator animation) {

			if (windowView == null) {
				return;
			}

			// setPosition(-10000, -10000);
		}
	}

    private native int saveFeedBack(String bookId, String content,
			String contact);



	protected void hideSoftKeyword(Activity activity, EditText contentEditText) {
		// 隐藏键盘
		InputMethodManager imm = (InputMethodManager) (activity
				.getSystemService(Context.INPUT_METHOD_SERVICE));

		// 如果软键盘已经显示，则隐藏，反之则显示
		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		imm.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
	}


}