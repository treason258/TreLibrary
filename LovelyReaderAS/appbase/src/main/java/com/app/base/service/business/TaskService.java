/**
 * 
 */
package com.app.base.service.business;

import com.java.common.http.Parameter;
import com.java.common.http.ProgressListener;
import com.java.common.http.ResultListener;

/**
 * @author tianyu
 *
 */
public interface TaskService {

	void execute();

	void execute(Parameter parameter,
			final BusinessAspectListener businessAspectListener);

	void execute(Parameter parameter, ResultListener resultListener,
			final BusinessAspectListener businessAspectListener);

	void execute(Parameter parameter, ResultListener resultListener,
			ProgressListener progressListener,
			BusinessAspectListener businessAspectListener);

}
