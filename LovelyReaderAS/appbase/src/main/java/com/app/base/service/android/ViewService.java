package com.app.base.service.android;

import android.app.Activity;
import android.view.View;


/**
 * View服务类。 对View对象功能的封装。
 * 
 * @author tianyu912@yeah.net
 */
public class ViewService extends AbstractAndroidService {

	/**
	 * 当前被封装的view对象。
	 */
	private View view;

	public ViewService(Activity activity) {
		super(activity);
		View view = activity.getWindow().getDecorView();
		this.init(view);
	}

	public ViewService(View view) {
		this.init(view);
	}

	private void init(View view) {
		this.view = view;
	}

	public <T extends View> T getViewById(int id, View.OnClickListener l) {

		@SuppressWarnings("unchecked")
		T view = (T) this.view.findViewById(id);
		if (view != null) {
			view.setOnClickListener(l);
		}
		return view;

	}

}
