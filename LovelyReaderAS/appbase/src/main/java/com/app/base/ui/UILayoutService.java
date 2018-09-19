/**
 * 
 */
package com.app.base.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author tianyu
 *
 */
public class UILayoutService {
	

	/**
	 * 读取指定的布局，并返回对应的View.
	 * 
	 * @param activity
	 * @param layoutID
	 *            需要加载的布局id.
	 * @return
	 */
	public static View getViewFromLayout(Context activity, int layoutID) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(layoutID, null);
		return view;
	}
}
