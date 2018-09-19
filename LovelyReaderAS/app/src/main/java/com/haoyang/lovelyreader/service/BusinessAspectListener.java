/**
 *
 */
package com.haoyang.lovelyreader.service;

import android.app.Activity;

import com.haoyang.lovelyreader.ui.ProgressDialogService;
import com.java.common.http.Parameter;

/**
 * @author tianyu
 */
public class BusinessAspectListener implements
    com.app.base.service.business.BusinessAspectListener {

  private ProgressDialogService progressDialogService = null;

  public BusinessAspectListener(Activity activity, String message) {

    if (activity == null && (message == null || "".equals(message))) {
      return;
    }

    progressDialogService = new ProgressDialogService(message, activity);
  }

  @Override
  public void onStart(Parameter parameter) {

    if (this.progressDialogService != null) {
      this.progressDialogService.show();
    }
  }

  @Override
  public void onEnd() {

    if (this.progressDialogService != null) {
      this.progressDialogService.hide();
    }
  }
}
