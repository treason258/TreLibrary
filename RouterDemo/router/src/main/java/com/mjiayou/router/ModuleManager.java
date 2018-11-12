package com.mjiayou.router;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by xin on 18/11/12.
 */

public class ModuleManager implements IModuleManager {
    private final HashMap<String, IPluginModule> mBuiltinModules = new HashMap();
    private final HashMap<String, IModuleFactory> mBuiltinFactory = new HashMap();

    public ModuleManager() {
    }

    public final void addModule(String packageName, Class<? extends IPluginModule> c, IPluginModule module) {
        String key = this.buildKey(packageName, c);
        this.mBuiltinModules.put(key, module);
    }

    public final void addModuleFactory(String packageName, Class<? extends IModuleFactory> c, IModuleFactory factory) {
        String key = this.buildKey(packageName, c);
        this.mBuiltinFactory.put(key, factory);
    }

    @Override
    public final IPluginModule getModule(String packageName, Class<? extends IPluginModule> c, Object custom) {
        if (TextUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException();
        } else {
            String key = this.buildKey(packageName, c);
            IPluginModule m = (IPluginModule) this.mBuiltinModules.get(key);
            if (m != null) {
                return m;
            } else {
                IModuleFactory f = (IModuleFactory) this.mBuiltinFactory.get(key);
                return f != null ? f.getModule(packageName, c, custom) : null;
            }
        }
    }

    private String buildKey(String packageName, Class<?> c) {
        String key = packageName + ":" + (c != null ? c.getName() : "null");
        return key;
    }
}
