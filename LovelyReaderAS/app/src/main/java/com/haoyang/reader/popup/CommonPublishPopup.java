/**
 * 
 */
package com.haoyang.reader.popup;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.base.service.android.AndroidAppService;
import com.app.base.service.android.ToastService;
import com.app.base.ui.AnimatorDefaultListener;

import com.haoyang.reader.sdk.BookStress;

import com.haoyang.reader.sdk.PageAnimationService;

/**
 * 点击选中文字后弹出的菜单中"笔记"的功能,弹出笔记输入界面。
 * 
 * @author tianyu912@yeah.net
 */
public class CommonPublishPopup extends PopupPanel implements View.OnClickListener {

	/**
	 * 笔记内容的标题。
	 */
	private TextView titleTextView;

	/**
	 * 用户输入笔记内容的输入框。
	 */
	private EditText contentEditText;

	public final static String CommonPublishId = "CommonPublishId";

	private BookStress bookStress;

	private int commonPublishCancelId;
	private int commonPublishFinishId;
	private int commonPublishPanel;

	private AndroidAppService androidAppService;

	public CommonPublishPopup(String popupId,ReaderPopupService readerPopupService) {

		super(popupId, readerPopupService);
	}

	public void initUI(RelativeLayout relativeLayout) {

		if (this.windowView != null && activity == windowView.getContext()) {
			return;
		}

		this.androidAppService = new AndroidAppService(activity);

		int panel_common_publishLayout = this.androidAppService.getLayoutResource("hy_panel_common_publish");

		// 加载弹出的选择框布局。
		activity.getLayoutInflater().inflate(panel_common_publishLayout, relativeLayout);

		int commonPublishTitlePanelId = androidAppService.getIdResource("commonPublishPanel");

		int commonPublishTitleId = androidAppService.getIdResource("commonPublishTitle");
		int commonPublishContentId = androidAppService.getIdResource("commonPublishContent");

		this.windowView = relativeLayout.findViewById(commonPublishTitlePanelId);

		// 获取笔记的标题框和笔记内容的输入框。
		titleTextView = (TextView) windowView.findViewById(commonPublishTitleId);
		contentEditText = (EditText) windowView
				.findViewById(commonPublishContentId);

		commonPublishCancelId = androidAppService.getIdResource("commonPublishCancel");
		commonPublishFinishId = androidAppService.getIdResource("commonPublishFinish");
		commonPublishPanel = androidAppService.getIdResource("commonPublishPanel");

		this.setPosition(-10000, -10000);

		setButtonListener();
	}

	public BookStress getBookStress() {
		return bookStress;
	}

	public void setBookStress(BookStress bookStress) {
		this.bookStress = bookStress;
	}

	private void setButtonListener() {

		setupButton(commonPublishCancelId, windowView);
		setupButton(commonPublishFinishId, windowView);
		setupButton(commonPublishPanel, windowView);
	}

	private ObjectAnimator publishAnimator;
	private PublishAnimatorListener publishAnimatorListener = new PublishAnimatorListener();

	@Override
	public void show() {

		if (publishAnimator != null && publishAnimator.isRunning()
				&& publishAnimator.isStarted()) { // 动画正在运行时不能处理。
			return;
		}

		if (this.bookStress != null) { // 选中的文字内容。

			String str = this.bookStress.getContent().replace("\n", "");
			titleTextView.setText(str);
			titleTextView.invalidate();
		}
		if (this.bookStress != null) { // 选中的文字内容。

			contentEditText.setText(this.bookStress.getCommentContent());
		}

		// 菜单显示的时候，子菜单不显示。
		final View view = windowView.findViewById(commonPublishPanel);
		windowView.setVisibility(View.VISIBLE);

		view.bringToFront();

		int height = view.getHeight();
		int screenHeight = windowView.getHeight();

		view.setY(screenHeight);
		publishAnimator = ObjectAnimator.ofFloat(view, "Y", screenHeight
				- height);
		publishAnimator.setDuration(300);
		publishAnimator.start();
	}

	@Override
	public void hide() {

		if (publishAnimator != null && publishAnimator.isRunning()
				&& publishAnimator.isStarted()) { // 动画正在运行时不能处理。
			return;
		}

		final View view = windowView.findViewById(commonPublishPanel);

		int screenHeight = windowView.getHeight();

		publishAnimator = ObjectAnimator.ofFloat(view, "Y", screenHeight);
		publishAnimator.addListener(publishAnimatorListener);
		publishAnimator.setDuration(300);

		publishAnimator.start();
	}

	public void onClick(View view) {

		int id = view.getId();

		if (id == commonPublishPanel) {

			return;
		} else if (id == this.commonPublishCancelId) { // 取消制作批注，把标题和内容都清空，用户选中的文字选中状态也要清空。

			this.titleTextView.setText("");
			this.contentEditText.setText("");

		} else if (id == this.commonPublishFinishId) { // 提交批注信息。

			String commentContent = this.contentEditText.getText().toString();

			if (commentContent.length() > 500) {

				ToastService t = new ToastService(this.activity);
				t.showMsg("字数超限，最多只能500个字!!");
				return;
			}

			if (this.bookStress != null) {
				this.bookStress.setCommentContent(commentContent);
			}

			hideSoftKeyword(super.activity, contentEditText);

			this.hide();

			this.readerDynamicSDK.stopSelection();

			this.readerDynamicSDK.paintNote(bookStress);
			this.readerDynamicSDK.saveCloudStress(bookStress);

			ToastService t = new ToastService(this.activity);

			t.showMsg("提交成功");
		}

		hideSoftKeyword(this.activity, contentEditText);

		this.readerPopupService.hideCurrentPopup();
	}

	class PublishAnimatorListener extends AnimatorDefaultListener {

		public void onAnimationEnd(Animator animation) {

			if (windowView == null) {
				return;
			}

			windowView.setVisibility(View.INVISIBLE);

			setPosition(-10000, -10000);
		}
	}

}