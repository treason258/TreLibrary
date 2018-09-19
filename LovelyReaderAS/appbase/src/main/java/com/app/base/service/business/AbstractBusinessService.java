/**
 *
 */
package com.app.base.service.business;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.java.common.http.Parameter;
import com.java.common.http.ProgressListener;
import com.java.common.http.ResultListener;

/**
 * 和业务相关的服务类都继承这个。
 *
 * @author tianyu912@yeah.net
 */
public abstract class AbstractBusinessService {

  private ExecutorService service = Executors.newCachedThreadPool();

  /**
   * 发启任务执行的实现对象。
   */
  private TaskService taskService;

  public AbstractBusinessService() {
    super();
    this.taskService = new AsynchronousTaskService(service);
  }

  public AbstractBusinessService(TaskService taskService) {
    super();
    this.taskService = taskService;
  }

  public void start() {
    if (this.taskService == null) {
      return;
    }
    this.taskService.execute();
  }

  public void start(Parameter parameter) {

    if (this.taskService == null) {
      return;
    }
    this.taskService.execute(parameter, null);
  }

  public void start(Parameter parameter, ResultListener resultListener) {

    if (this.taskService == null) {
      return;
    }

    this.taskService.execute(parameter, resultListener, null);
  }

  public void start(Parameter parameter, ResultListener resultListener,
      ProgressListener progressListener) {

    if (this.taskService == null) {
      return;
    }

    this.taskService.execute(parameter, resultListener, progressListener,
        null);
  }

  // ==========================================================================
  // ==========================================================================

  public void start(Parameter parameter,
      BusinessAspectListener businessAspectListener) {

    if (this.taskService == null) {
      return;
    }

    this.taskService.execute(parameter, businessAspectListener);
  }

  public void start(Parameter parameter, ResultListener resultListener,
      BusinessAspectListener businessAspectListener) {

    if (this.taskService == null) {
      return;
    }

    this.taskService.execute(parameter, resultListener,
        businessAspectListener);
  }

  public void start(Parameter parameter, ResultListener resultListener,
      ProgressListener progressListener,
      BusinessAspectListener businessAspectListener) {

    if (this.taskService == null) {
      return;
    }

    this.taskService.execute(parameter, resultListener, progressListener,
        businessAspectListener);
  }

  public void setTaskService(TaskService taskService) {
    this.taskService = taskService;
  }
}
