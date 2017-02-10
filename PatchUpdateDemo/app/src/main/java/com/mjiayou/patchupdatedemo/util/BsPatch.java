package com.mjiayou.patchupdatedemo.util;

/**
 * Created by zhy on 16/10/7.
 */
public class BsPatch {

    static {
        System.loadLibrary("bspatch");
    }

    public static native int bspatch(String oldApk, String newApk, String patch);

}
