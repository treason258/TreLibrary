/**
 * 
 */
package com.app.base.service.android;

import android.text.Html;
import android.widget.TextView;


/**
 * 
 * html文本格式化并放入textview中。
 * 
 * @author tianyu912@yeah.net
 */
public class TextViewHtmlService {
	

	public void formatAndSet(TextView textView, String htmlContent) {
		
		if (textView == null) {
			return;
		}
		
		textView.setText(Html.fromHtml(htmlContent));
	}

}
