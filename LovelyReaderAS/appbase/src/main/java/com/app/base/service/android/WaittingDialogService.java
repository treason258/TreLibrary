/**
 * 
 */
package com.app.base.service.android;

import android.app.AlertDialog;
import android.widget.TextView;


/**
 * 
 * 等待对话框.如果对话框中要想显示文字，那么在对话框的布局里需要有名字为hint_text的TextView的配置。
 * 
 * @author tianyu912@yeah.net
 * 
 */
public class WaittingDialogService {

	private AlertDialog dialog;

	/**
	 * 等待提示框的布局id.
	 */
	private int styleLayoutId;

	/**
	 * 等待提示框中显示的提示情文字。用户在显示的时候可以自行设置。
	 */
	private String hint;
	private TextView hintTextView;

	/**
	 * @param dialog
	 * @param styleLayoutId
	 */
	public WaittingDialogService(int styleLayoutId) {
		super();
		this.styleLayoutId = styleLayoutId;
	}

	/**
	 * 等待框，全屏透明背景
	 * 
	 */
	private void createWaitintDialog() {

		dialog = new AlertDialog.Builder(App.getContext(),
				android.R.style.Theme_Translucent_NoTitleBar).create();

		dialog.show();
		dialog.setContentView(styleLayoutId);

		//hintTextView = (TextView) dialog.findViewById(R.id.hint_text);
		dialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 展示等待对话框
	 */
	public void showWaitingDialog() {

		if (null == dialog) {
			createWaitintDialog();
			setHint();
		} else if (!dialog.isShowing()) {
			setHint();
			dialog.show();
		}
	}

	/**
	 * 设置对话框中的提示情文字。
	 */
	private void setHint() {

		if (this.hint != null && this.hintTextView != null) {
			this.hintTextView.setText(hint);
		}
	}

	/**
	 * 关闭等待对话框
	 */
	public void closeWaitingDialog() {
		if (null != dialog && dialog.isShowing()) {
			dialog.cancel();
		}
	}

	/**
	 * 更改在等待框上显示的文字信息。
	 * 
	 * @param message 
	 */
	public void setMessageToDialog(String message) {
		this.hint = message;
	}

}
