/**
 * 
 */
package com.app.base.ui.adapter;

import android.view.View;


/**
 * 
 * 将指定的数据发布到view上。
 * @author tianyu
 *
 * email:xjzx_tianyu@yeah.net<br>
 * Date :2014年6月19日
 */
public interface DataPublisher {
	
	View publishToView(int position, View view, Adapter adapter);

}
