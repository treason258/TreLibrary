package com.mjiayou.routerdemo.router.impl;

import com.mjiayou.router.IModuleManager;
import com.mjiayou.router.IPluginModule;
import com.mjiayou.thirdparty.router.IAppModule;
import com.mjiayou.thirdparty.router.IThirdPartyModule;

/**
 * Created by treason on 2017/11/28.
 */

public class AppModuleImpl implements IAppModule {

    private static IModuleManager mIModuleManager;
    private static IThirdPartyModule mIThirdPartyModule;

    public AppModuleImpl(IModuleManager iModuleManager) {
        mIModuleManager = iModuleManager;
    }


    public static IThirdPartyModule getThirdPartyModule() {
        if (mIModuleManager != null && mIThirdPartyModule == null) {
            IPluginModule iPluginModule = mIModuleManager.getModule(IThirdPartyModule.TAG, IThirdPartyModule.class);
            if (iPluginModule instanceof IThirdPartyModule) {
                mIThirdPartyModule = (IThirdPartyModule) iPluginModule;
            }
        }
        return mIThirdPartyModule;
    }
}
