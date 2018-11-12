package com.mjiayou.router;

/**
 * 如果模块需要公开接口给其它模块使用，则必须从该接口中继承，并定制想要的接口。
 * <p>
 * 举例来说：Block想要公开一个接口，则它应该做：
 * <p>
 * public interface IBlockModule implements IPluginModule {
 * <p>
 * String foo(int what);
 * <p>
 * }
 * <p>
 * 这个文件必须放到“i”项目中的“com.xin.modules”包中。而实现该接口的类则必须放在子模块中，如Block模块中：
 * <p>
 * public class BlockModule extends IBlockModule {
 * <p>
 * public String foo(int what) {
 * ...
 * }
 * }
 */
public interface IPluginModule {

    String TAG = IPluginModule.class.getClass().getSimpleName();
}
