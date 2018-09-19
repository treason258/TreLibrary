/**
 * 
 */
package com.app.base.service.android;

import android.content.Context;
import android.widget.Toast;


/**
 * 显示提示消息。
 * 
 * @author tianyu912@yeah.net
 */
public class ToastService extends AbstractContextService {

	private Toast toast;

	public ToastService() {
		this(null);
	}

	public ToastService(Context context) {
		super(context);
	}

	/**
	 * 显示短消息。
	 * @param message
	 */
	public void showMsg(String message) {

		showMsg(message, Toast.LENGTH_SHORT);
	}

	/**
	 * 显示长消息。
	 * @param message
	 */
	public void showMsgLong(String message) {

		showMsg(message, Toast.LENGTH_LONG);
	}

	/**
	 * 显示消息的实现类。
	 * @param message
	 * @param shortOrLongDelay Toast.LENGTH_SHORT 或 Toast.LENGTH_LONG
	 */
	private void showMsg(String message, int shortOrLongDelay) {
		if (null == toast) {
			toast = Toast.makeText(super.context, message, shortOrLongDelay);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		}
		toast.setText(message);
		toast.show();
	}

	/**
	 * 销毁Toast对象。在不需要的时候要调用这个方法来清空对象。
	 */
	public void cancelToast() {
		if (null != toast) {
			toast.cancel();
		}
	}

	/**
	 * 显示提示信息 (短)
	 * 
	 * @param context
	 * @param showContent
	 */
	public static void showMsg(Context context, String showContent) {

		Toast.makeText(context, showContent, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 在android界面上显示提示信息(长).
	 * 
	 * @param context
	 * @param showContent
	 */
	public static void showMsgLong(Context context, String showContent) {

		Toast.makeText(context, showContent, Toast.LENGTH_LONG).show();
	}

}
