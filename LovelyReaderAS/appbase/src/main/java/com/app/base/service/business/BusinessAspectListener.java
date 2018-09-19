/**
 * 
 */
package com.app.base.service.business;

import com.java.common.http.Parameter;

/**
 * @author tianyu
 *
 */
public interface BusinessAspectListener {

	void onStart(Parameter parameter);

	void onEnd();

}
