package com.mjiayou.router;


/**
 * 可获取其它模块的IPluginModule对象。如果某模块需要调用其它模块（如Block）的接口时，应按下列方式来做：
 * <p>
 * IPluginModule module = mModules.getModule("block", IBlockModule.class);
 * <p>
 * if (module != null && module instanceof IBlockModule) {
 * ...
 * }
 * <p>
 * IModuleManager可有两种途径获得：
 * <p>
 * 1. Activity：需有一个特定的构造函数。如BlockActivity，则为：
 * <p>
 * public BlockActivity(Context mainContext, IModuleManager modules) {
 * mMainContext = mainContext;
 * mModules = modules;
 * }
 * <p>
 * 如果没有该构造函数，该Activity依旧可用，只是无法得到IModuleManager对象，适合没有模块依赖的Activity使用。
 * <p>
 * 2. Service、ContentProvider、BroadcastReceiver等：通过各个模块的Entry对象中的createObject方法进行传递。如：
 * <p>
 * public static final IPlugin createObject(Context context, IModuleManager moduleManager) {
 * ...
 * }
 */
public interface IModuleManager {

    /**
     * 添加指定模块的IPluginModule对象
     *
     * @param tag    要获取的模块的名称。如“骚扰拦截”模块名应为block
     * @param clazz  要获取的接口名称
     * @param module 自定义数据
     * @return IPluginModule对象。详见IPluginModule接口说明。
     */
    void addModule(String tag, Class<? extends IPluginModule> clazz, IPluginModule module);

    /**
     * 获取指定模块的IPluginModule对象
     *
     * @param tag   要获取的模块的名称。如“骚扰拦截”模块名应为block
     * @param clazz 要获取的接口名称
     * @return IPluginModule对象。详见IPluginModule接口说明。
     */
    IPluginModule getModule(String tag, Class<? extends IPluginModule> clazz);
}
