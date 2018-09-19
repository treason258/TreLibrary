/**
 * 
 */
package com.app.base.service.android;

import android.content.Context;


/**
 * 服务类的基类。
 * 这个基类是为只能得到Context对象而设计的服务定义的。
 *
 * @author tianyu912@yeah.net
 */
public class AbstractContextService {
	
	protected Context context;

	/**
	 * @param context
	 */
	public AbstractContextService() {
		super();
	}
	
	/**
	 * @param context
	 */
	public AbstractContextService(Context context) {
		super();
		this.context = context;
	}
}
