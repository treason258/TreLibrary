package com.mjiayou.router.impl;

import android.text.TextUtils;

import com.mjiayou.router.IModuleManager;
import com.mjiayou.router.IPluginModule;

import java.util.HashMap;

public class ModuleManagerImpl implements IModuleManager {

    private final HashMap<String, IPluginModule> mIPluginModuleMap = new HashMap<>();

    @Override
    public final void addModule(String tag, Class<? extends IPluginModule> clazz, IPluginModule module) {
        String key = buildKey(tag, clazz);
        mIPluginModuleMap.put(key, module);
    }

    @Override
    public final IPluginModule getModule(String tag, Class<? extends IPluginModule> clazz) {
        if (TextUtils.isEmpty(tag)) {
            throw new IllegalArgumentException();
        }

        String key = buildKey(tag, clazz);
        IPluginModule pluginModule = mIPluginModuleMap.get(key);

        if (pluginModule != null) {
            return pluginModule;
        }

        return null;
    }

    private String buildKey(String tag, Class<?> clazz) {
        return tag + ":" + (clazz != null ? clazz.getName() : "null");
    }
}
