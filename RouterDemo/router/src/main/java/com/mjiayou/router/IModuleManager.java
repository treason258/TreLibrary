package com.mjiayou.router;

/**
 * Created by xin on 18/11/12.
 */

public interface IModuleManager {
    IPluginModule getModule(String var1, Class<? extends IPluginModule> var2, Object var3);
}
