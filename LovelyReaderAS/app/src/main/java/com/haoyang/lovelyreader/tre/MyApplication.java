package com.haoyang.lovelyreader.tre;

import com.haoyang.lovelyreader.tre.helper.BuglyHelper;
import com.haoyang.reader.sdk.ReaderApplication;
import com.mjiayou.trecorelib.base.TCApp;

/**
 * Created by xin on 18/9/22.
 */
public class MyApplication extends ReaderApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        TCApp.onCreateManual(this);

        BuglyHelper.init(getApplicationContext());
    }
}
