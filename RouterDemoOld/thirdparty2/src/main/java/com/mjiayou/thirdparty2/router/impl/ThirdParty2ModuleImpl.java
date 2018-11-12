package com.mjiayou.thirdparty2.router.impl;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.mjiayou.router.IModuleManager;
import com.mjiayou.router.IPluginModule;
import com.mjiayou.thirdparty2.router.IImageLoader2Module;
import com.mjiayou.thirdparty2.router.IThirdParty2Module;

/**
 * Created by treason on 2017/11/21.
 */
public class ThirdParty2ModuleImpl implements IThirdParty2Module {

    private static IModuleManager mIModuleManager;
    private static IImageLoader2Module mIImageLoaderModule;

    public ThirdParty2ModuleImpl(IModuleManager moduleManager) {
        mIModuleManager = moduleManager;
    }

    public static IImageLoader2Module getImageLoader() {
        if (mIModuleManager != null && mIImageLoaderModule == null) {
            IPluginModule pluginModule = mIModuleManager.getModule(IImageLoader2Module.TAG, IImageLoader2Module.class);
            if (pluginModule instanceof IImageLoader2Module) {
                mIImageLoaderModule = (IImageLoader2Module) pluginModule;
            }
        }
        return mIImageLoaderModule;
    }

    @Override
    public void toOtherActivity(Activity activity, Intent intent) {
        Toast.makeText(activity, "ThirdParty2 - toOtherActivity", Toast.LENGTH_SHORT).show();
    }
}
