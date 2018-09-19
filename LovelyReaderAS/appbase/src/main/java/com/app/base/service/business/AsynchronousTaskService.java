/**
 * 
 */
package com.app.base.service.business;

import java.util.concurrent.ExecutorService;

import com.java.common.http.DefaultHttpCallBack;
import com.java.common.http.HttpService;
import com.java.common.http.Parameter;
import com.java.common.http.ProgressListener;
import com.java.common.http.ResultListener;

/**
 * 
 * 异步执行任务的服务类。
 * 
 * @author tianyu
 *
 */
public class AsynchronousTaskService implements TaskService {

	/**
	 * 执行器接口。
	 */
	ExecutorService executorService;

	/**
	 * 构造方法。使用者需要传入自己需要的执行器对象。
	 * 
	 * @param executorService
	 */
	public AsynchronousTaskService(ExecutorService executorService) {
		super();
		this.executorService = executorService;
	}

	@Override
	public void execute() {

	}

	@Override
	public void execute(final Parameter parameter,
			final BusinessAspectListener businessAspectListener) {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				if (businessAspectListener != null) {
					businessAspectListener.onStart(parameter);
				}

				HttpService httpService = new HttpService();
				httpService.loadDataByHttPost(parameter);

				if (businessAspectListener != null) {
					businessAspectListener.onEnd();
				}
			}
		};

		if (this.executorService != null) {
			this.executorService.submit(runnable);
		}
	}

	@Override
	public void execute(final Parameter parameter,
			final ResultListener resultListener,
			final BusinessAspectListener businessAspectListener) {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				if (businessAspectListener != null) {
					businessAspectListener.onStart(parameter);
				}

				DefaultHttpCallBack httpCallBack = new DefaultHttpCallBack(
						resultListener);

				HttpService httpService = new HttpService();
				httpService.loadDataByHttPost(parameter, httpCallBack);

				if (businessAspectListener != null) {
					businessAspectListener.onEnd();
				}
			}
		};

		if (this.executorService != null) {
			this.executorService.submit(runnable);
		}
	}

	@Override
	public void execute(final Parameter parameter,
			final ResultListener resultListener,
			final ProgressListener progressListener,
			final BusinessAspectListener businessAspectListener) {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				if (businessAspectListener != null) {
					businessAspectListener.onStart(parameter);
				}

				DefaultHttpCallBack httpCallBack = new DefaultHttpCallBack(
						resultListener, progressListener);

				HttpService httpService = new HttpService();
				httpService.loadDataByHttPost(parameter, httpCallBack);

				if (businessAspectListener != null) {
					businessAspectListener.onEnd();
				}
			}
		};

		if (this.executorService != null) {
			this.executorService.submit(runnable);
		}
	}

}
