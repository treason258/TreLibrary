/**
 * 
 */
package com.app.base.service.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * SharedPreferences存取数据的服务类。
 * 
 * @author tianyu
 * 
 *         email:tianyu912@yeah.net
 * 
 *         date:May 9, 2014
 */
public class SharedPreferenceService {

	private final String setting = "SharedPreference";

	private Context context;

	public SharedPreferenceService(Context context) {
		super();
		this.context = context;
	}

	public void putValue(String key, int value) {
		Editor sp = this.context.getSharedPreferences(setting,
				Context.MODE_PRIVATE).edit();

		sp.putInt(key, value);
		sp.commit();
	}
	public void putValue(String key, long value) {
		
		Editor sp = this.context.getSharedPreferences(setting,
				Context.MODE_PRIVATE).edit();
		
		sp.putLong(key, value);
		sp.commit();
	}

	public void putValue(String key, boolean value) {
		Editor sp = this.context.getSharedPreferences(setting,
				Context.MODE_PRIVATE).edit();
		sp.putBoolean(key, value);
		sp.commit();
	}

	public void putValue(String key, String value) {
		Editor sp = this.context.getSharedPreferences(setting,
				Context.MODE_PRIVATE).edit();
		sp.putString(key, value);
		sp.commit();
	}

	public long getValue(String key, long defValue) {

		SharedPreferences sp = this.context.getSharedPreferences(setting,
				Context.MODE_PRIVATE);
		Long value = sp.getLong(key, defValue);

		if (value == null) {
			return 0;
		}
		
		return value;
	}

	public int getValue(String key, int defValue) {
		SharedPreferences sp = this.context.getSharedPreferences(setting,
				Context.MODE_PRIVATE);
		int value = sp.getInt(key, defValue);
		return value;
	}

	public boolean getValue(String key, boolean defValue) {
		SharedPreferences sp = this.context.getSharedPreferences(setting,
				Context.MODE_PRIVATE);
		boolean value = sp.getBoolean(key, defValue);
		return value;
	}

	public String getValue(String key, String defValue) {
		SharedPreferences sp = this.context.getSharedPreferences(setting,
				Context.MODE_PRIVATE);
		String value = sp.getString(key, defValue);
		return value;
	}
}
