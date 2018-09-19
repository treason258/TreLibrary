/**
 * 
 */
package com.app.base.service.android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.java.common.service.StringService;

/**
 * 
 * @author tianyu912@yeah.net
 */
public class ActionService extends AbstractAndroidService {

	public ActionService() {
		super();
	}

	public ActionService(Activity activity) {
		super(activity);
	}

	/**
	 * 打电话。
	 * 
	 * @param mContext
	 * @param phone
	 * @param isDialView
	 */
	public void callPhone(String phone, boolean isDialView) {
		String action = Intent.ACTION_CALL;
		if (isDialView) {
			// 显示拨号界面
			action = Intent.ACTION_DIAL;
		}

		if (new StringService().isNullOrEmpty(phone)) {
			return;
		}
		Intent intent = new Intent(action, Uri.parse("tel:" + phone));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		super.activity.startActivity(intent);
	}

	/**
	 * 进入到设置网络的页面。
	 */
	public void enterSettingPage() {

		Intent intent = null;

		// //判断手机系统的版本 即API大于10 就是3.0或以上版本
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent = new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		} else {
			intent = new Intent();
			ComponentName component = new ComponentName("com.android.settings",
					"com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}

		activity.startActivity(intent);
	}
	
	/**
	 * 进入到应用管理的页面。
	 */
	public void applicationManager() {
 
		Intent intent =  new Intent();  
		intent.setAction("android.intent.action.MAIN");  
		intent.setClassName("com.android.settings", "com.android.settings.ManageApplications");  
		activity.startActivity(intent);  
	}
	

}
