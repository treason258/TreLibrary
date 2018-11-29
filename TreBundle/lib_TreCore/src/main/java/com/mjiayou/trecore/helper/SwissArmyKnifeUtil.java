package com.mjiayou.trecore.helper;

import com.mjiayou.trecore.base.TCApp;
import com.mjiayou.trecorelib.util.LogUtil;
import com.wanjian.sak.LayoutManager;

/**
 * Created by treason on 2016/12/20.
 */

public class SwissArmyKnifeUtil {

    private static final String TAG = SwissArmyKnifeUtil.class.getSimpleName();

    /**
     * 初始化
     */
    public static void init() {
        LogUtil.printInit(TAG);

        LayoutManager.init(TCApp.get());
    }
}
