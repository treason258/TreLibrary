/**
 * 
 */
package com.app.base.service.android;

import android.app.Activity;


/**
 * 服务类的基类。
 * 里面是一些公共的实例变量和方法。
 *
 * @author tianyu912@yeah.net
 */
public class AbstractAndroidService {
	
	protected Activity activity;

	public AbstractAndroidService() {
		super();
	}
	
	/**
	 * @param activity
	 */
	public AbstractAndroidService(Activity activity) {
		super();
		this.activity = activity;
	}
}
