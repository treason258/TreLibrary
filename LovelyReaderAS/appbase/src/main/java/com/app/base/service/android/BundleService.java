/**
 * 
 */
package com.app.base.service.android;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


/**
 * Bundle服务类,对Bundle对象的封装,简化了一下.
 * @author tianyu912@yeah.net
 */
public class BundleService extends AbstractAndroidService {

	private Bundle bundle;
	 
	public BundleService(Activity activity) {
		super(activity);
		this.bundle = this.getBundle();
	}

	/**
	 * 从Intent中获取bundle对象。
	 * 
	 * @return
	 */
	public Bundle getBundle() {
		Intent intent = this.activity.getIntent();
		Bundle bundle = intent.getExtras();
		return bundle;
	}
	
	/**
	 * 从Intent的Bundle中读取指定key的对象。
	 * @param key
	 * @return
	 */
	public <T extends Object> Object getObjectFromBundle(String key) {
		
		Bundle bundle = getBundle();
		if (bundle == null) {
			return null;
		}
		return  bundle.get(key);
	}
	
	public Boolean getBoolean(String key) {
		return bundle.getBoolean(key);
	}

	public Boolean getBoolean(String key, boolean defaultValue) {
		return bundle.getBoolean(key, defaultValue);
	}

	public int getInt(String key) {
		return bundle.getInt(key);
	}

	public int getInt(String key, int defaultValue) {
		return bundle.getInt(key, defaultValue);
	}

	public long getLong(String key) {
		return bundle.getLong(key);
	}

	public long getLong(String key, long defaultValue) {
		return bundle.getLong(key, defaultValue);
	}

	public float getFloat(String key) {
		return bundle.getFloat(key);
	}

	public float getFloat(String key, float defaultValue) {
		return bundle.getFloat(key, defaultValue);
	}

	public double getDouble(String key) {
		return bundle.getDouble(key);
	}

	public double getDouble(String key, double defaultValue) {
		return bundle.getDouble(key, defaultValue);
	}

	public String getString(String key) {
		return bundle.getString(key);
	}
//  TODO
//	public String getString(String key, String defaultValue) {
//		return bundle.getString(key, defaultValue);
//	}

	public Serializable getSerializable(String key) {
		return bundle.getSerializable(key);
	}

	public ArrayList<Integer> getIntegerArrayList(String key) {
		return bundle.getIntegerArrayList(key);
	}

	public ArrayList<String> getStringArrayList(String key) {
		return bundle.getStringArrayList(key);
	}

	public boolean[] getBooleanArray(String key) {
		return bundle.getBooleanArray(key);
	}

	public int[] getIntArray(String key) {
		return bundle.getIntArray(key);
	}

	public long[] getLongArray(String key) {
		return bundle.getLongArray(key);
	}

	public float[] getFloatArray(String key) {
		return bundle.getFloatArray(key);
	}

	public double[] getDoubleArray(String key) {
		return bundle.getDoubleArray(key);
	}

	public String[] getStringArray(String key) {
		return bundle.getStringArray(key);
	}
}
