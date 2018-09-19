/**
 * 
 */
package com.app.base.ui.adapter;

import android.util.SparseArray;
import android.view.View;


/**
 * 视图缓存。
 * 
 * @author Tianyu
 *
 * email:tianyu912@yeah.net
 */
public class CommonViewHolder {
	
	private final static int key = -1000;
	/**
	 * 这个方法需要确认是否需要线程安全。
	 * @param view
	 * @param id
	 * @return
	 */
	public static <T extends View> T getViewFromCache(View view, int id) {
		
		/*
		 * view的tag中存的是一个SparseArray。
		 */
		SparseArray<View> viewHolder = (SparseArray<View>)view.getTag(key);
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(key, viewHolder);
		}
		
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T)childView;
	}
}
