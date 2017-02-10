package com.mjiayou.jnidemo;

/**
 * Created by treason on 2017/2/9.
 */

public class JNIUtil {

    public native String getHello();

    private static volatile JNIUtil mInstance;

    /**
     * 单例模式，获取实例
     */
    public static JNIUtil get() {
        if (mInstance == null) {
            synchronized (JNIUtil.class) {
                if (mInstance == null) {
                    mInstance = new JNIUtil();
                }
            }
        }
        return mInstance;
    }

    static {
        /**
         * cd app/build/intermediates/classes/alpha/debug/
         * cd app/src/main/
         * mkdir jni
         * cd app/src/main/jni/
         *
         * cd app/src/main/java/
         * javah -jni com.mjiayou.trecore.test.jni.TestJNIUtil
         */
        System.loadLibrary("testjni");
    }
}
