package com.mjiayou.routerdemo.router;

import com.mjiayou.router.impl.ModuleManagerImpl;
import com.mjiayou.routerdemo.router.impl.ImageLoader2Impl;
import com.mjiayou.routerdemo.router.impl.ImageLoaderImpl;
import com.mjiayou.thirdparty.router.IImageLoaderModule;
import com.mjiayou.thirdparty.router.IThirdPartyModule;
import com.mjiayou.thirdparty.router.impl.ThirdPartyModuleImpl;
import com.mjiayou.thirdparty2.router.IImageLoader2Module;
import com.mjiayou.thirdparty2.router.IThirdParty2Module;
import com.mjiayou.thirdparty2.router.impl.ThirdParty2ModuleImpl;

/**
 * Created by treason on 2017/11/21.
 */

public class Router {

    private ModuleManagerImpl mModuleManagerImpl;

    public void init() {
        mModuleManagerImpl = new ModuleManagerImpl();
        // ThirdParty
        mModuleManagerImpl.addModule(IThirdPartyModule.TAG, IThirdPartyModule.class, new ThirdPartyModuleImpl(mModuleManagerImpl));
        mModuleManagerImpl.addModule(IImageLoaderModule.TAG, IImageLoaderModule.class, new ImageLoaderImpl());
        // ThirdParty2
        mModuleManagerImpl.addModule(IThirdParty2Module.TAG, IThirdParty2Module.class, new ThirdParty2ModuleImpl(mModuleManagerImpl));
        mModuleManagerImpl.addModule(IImageLoader2Module.TAG, IImageLoader2Module.class, new ImageLoader2Impl());
    }

    public ModuleManagerImpl getModuleManagerImpl() {
        return mModuleManagerImpl;
    }

    private static Router mInstance;

    public static Router get() {
        if (null == mInstance) {
            synchronized (Router.class) {
                if (null == mInstance) {
                    mInstance = new Router();
                }
            }
        }
        return mInstance;
    }
}
