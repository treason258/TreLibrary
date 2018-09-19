/**
 * 
 */
package com.app.base.service.android;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


/**
 * 启动一个activity的服务类.
 * 
 * @author tianyu912@yeah.net
 */
public class PageStarterService extends AbstractAndroidService {

	/**
	 * 组件、模块、程序等等，他们之间传递数据时可以用这个key.
	 * 
	 */
	public static final String KeyForPassObject = "object";

	private boolean forResult = false;
	private int requestCode;

	/**
	 * 
	 * @param activity
	 */
	public PageStarterService(Activity activity) {
		super(activity);
	}

	public void setForResult(int requestCode) {
		this.forResult = true;
		this.requestCode = requestCode;
	}

	private void clear() {
		this.forResult = false;
	}

	/**
	 * 启动指定的页面
	 * 
	 * @param context
	 * @param target
	 */
	public void nextPage(Class<? extends Activity> target) {
		Intent intent = new Intent();
		intent.setClass(activity, target);
		if (this.forResult) {
			activity.startActivityForResult(intent, requestCode);
			clear();
			return;
		}
		activity.startActivity(intent);

	}

	/**
	 * 启动指定的页面
	 * 
	 * @param context
	 * @param target
	 */
	public void nextPage(Class<? extends Activity> target, Serializable value) {
		Intent intent = new Intent();
		intent.putExtra(KeyForPassObject, value);
		intent.setClass(activity, target);
		activity.startActivity(intent);
	}

	/**
	 * 启动指定的页面<br>
	 * 进入到由target指定的Activity中。
	 * 
	 * @param target
	 * @param key
	 *            参数的key
	 * @param value
	 *            参数的value
	 */
	public void nextPage(Class<? extends Activity> target, String key,
			String value) {
		Intent intent = new Intent();
		intent.setClass(activity, target);
		intent.putExtra(key, value);
		activity.startActivity(intent);
	}

	/**
	 * 启动指定的页面<br>
	 * 进入到由target指定的Activity中。
	 * 
	 * @param context
	 * @param target
	 * @param entry
	 *            给Activity传递的参数。只能传一个key=>value值对。参见：com.android.common.utils.
	 *            GeneralEntry
	 */
	public void nextPage(Class<? extends Activity> target,
			Entry<String, String> entry, int flag) {
		Intent intent = new Intent();
		intent.addFlags(flag);
		intent.setClass(activity, target);
		intent.putExtra(entry.getKey(), entry.getValue());
		activity.startActivity(intent);
	}

	/**
	 * 启动指定的页面<br>
	 * 进入到由target指定的Activity中。
	 * 
	 * @param context
	 * @param target
	 * @param map
	 *            给Activity传递的参数。
	 */
	public void nextPage(Class<? extends Activity> target,
			HashMap<String, String> map, boolean toIntent) {
		Intent intent = new Intent();
		intent.setClass(activity, target);
		intent.putExtra(KeyForPassObject, map);
		activity.startActivity(intent);
	}

	/**
	 * 启动指定的页面<br>
	 * 进入到由target指定的Activity中。
	 * 
	 * @param context
	 * @param target
	 * @param map
	 *            给Activity传递的参数。
	 */
	public void nextPage(Class<? extends Activity> target,
			Map<String, String> map) {
		Intent intent = new Intent();
		intent.setClass(activity, target);

		for (String key : map.keySet()) {
			intent.putExtra(key, map.get(key));
		}
		activity.startActivity(intent);
	}

	/**
	 * 启动指定的页面<br>
	 * 进入到由target指定的Activity中。可以传递参数通过Bundle。
	 * 
	 * @param context
	 * @param target
	 * @param bundle
	 *            给Activity传递的参数。
	 */
	public void nextPage(Class<? extends Activity> target, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(activity, target);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		activity.startActivity(intent);
	}

	/**
	 * 调用系统显示web页面。
	 * @param url
	 */
	public void enterWebPage(String url) {

		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		activity.startActivity(intent);
	}

}
