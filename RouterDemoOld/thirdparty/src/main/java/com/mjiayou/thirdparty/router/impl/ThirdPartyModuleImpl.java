package com.mjiayou.thirdparty.router.impl;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.mjiayou.router.IModuleManager;
import com.mjiayou.router.IPluginModule;
import com.mjiayou.thirdparty.router.IImageLoaderModule;
import com.mjiayou.thirdparty.router.IThirdPartyModule;

/**
 * Created by treason on 2017/11/21.
 */
public class ThirdPartyModuleImpl implements IThirdPartyModule {

    private static IModuleManager mIModuleManager;
    private static IImageLoaderModule mIImageLoaderModule;

    public ThirdPartyModuleImpl(IModuleManager moduleManager) {
        mIModuleManager = moduleManager;
    }

    public static IImageLoaderModule getImageLoader() {
        if (mIModuleManager != null && mIImageLoaderModule == null) {
            IPluginModule pluginModule = mIModuleManager.getModule(IImageLoaderModule.TAG, IImageLoaderModule.class);
            if (pluginModule instanceof IImageLoaderModule) {
                mIImageLoaderModule = (IImageLoaderModule) pluginModule;
            }
        }
        return mIImageLoaderModule;
    }

    @Override
    public void toOtherActivity(Activity activity, Intent intent) {
        Toast.makeText(activity, "ThirdParty - toOtherActivity", Toast.LENGTH_SHORT).show();
    }
}
