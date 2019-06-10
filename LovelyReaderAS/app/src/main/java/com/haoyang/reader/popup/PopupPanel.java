package com.haoyang.reader.popup;

import android.app.Activity;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import android.widget.RelativeLayout;
import android.view.inputmethod.InputMethodManager;

import com.haoyang.reader.sdk.ReaderDynamicSDK;
import com.haoyang.reader.sdk.ConfigServiceSDK;

public abstract class PopupPanel implements View.OnClickListener {

	protected View windowView;

	protected Activity activity;
	protected RelativeLayout relativeLayout;
	protected ConfigServiceSDK configServiceSDK;
	protected ReaderDynamicSDK readerDynamicSDK;

	protected ReaderPopupService readerPopupService;

	protected boolean isParseBookFinish;

	public PopupPanel(String id, ReaderPopupService readerPopupService) {

		this.readerPopupService = readerPopupService;

		this.isParseBookFinish = false;

		this.readerPopupService.putPopup(id, this);
	}

	public void parseBookFinish(boolean isParseBookFinish) {

		this.isParseBookFinish = isParseBookFinish;
	}

	public void init(Activity activity,
							  RelativeLayout relativeLayout) {

		this.activity = activity;
		this.relativeLayout = relativeLayout;

		this.initUI(relativeLayout);
	}

	public abstract void initUI(RelativeLayout relativeLayout);

	public abstract void hide();

	public abstract void show();


	public void setPosition(int x, int y) {

		if (windowView == null) {
			return;
		}

		windowView.setX(x);
		windowView.setY(y);
	}


	public void setConfigServiceSDK(ConfigServiceSDK configServiceSDK) {
		this.configServiceSDK = configServiceSDK;
	}

	public void setReaderDynamicSDK(ReaderDynamicSDK readerDynamicSDK) {
		this.readerDynamicSDK = readerDynamicSDK;
	}

	// 获取当前popup是显示还是隐藏。
	// true:显示,false:未显示
	public boolean getShowStatus() {

		return this.windowView.getVisibility() == View.VISIBLE;
	}

	protected void setupButton(int buttonId, View view) {

		final View button = view.findViewById(buttonId);

		if (button == null) {
			return;
		}

		button.setOnClickListener(this);
	}

	protected void hideSoftKeyword(Activity activity, EditText contentEditText) {

		// 隐藏键盘
		InputMethodManager imm = (InputMethodManager) (activity
				.getSystemService(Context.INPUT_METHOD_SERVICE));

		// 如果软键盘已经显示，则隐藏，反之则显示
		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

		imm.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
	}

}
