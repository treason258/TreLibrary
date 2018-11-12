package com.mjiayou.thirdparty2.router;

import android.app.Activity;
import android.content.Intent;

import com.mjiayou.router.IPluginModule;

/**
 * 定义提供给外部使用的方法
 */
public interface IThirdParty2Module extends IPluginModule {

    void toOtherActivity(Activity activity, Intent intent);
}
