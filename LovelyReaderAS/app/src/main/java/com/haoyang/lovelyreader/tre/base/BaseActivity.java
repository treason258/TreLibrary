package com.haoyang.lovelyreader.tre.base;

import com.mjiayou.trecorelib.base.TCActivity;

/**
 * Created by xin on 18/9/22.
 */

public abstract class BaseActivity extends TCActivity {

    @Override
    protected boolean checkHideTitleBar() {
        return true;
    }
}
