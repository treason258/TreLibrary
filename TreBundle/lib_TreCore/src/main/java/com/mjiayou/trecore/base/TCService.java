package com.mjiayou.trecore.base;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mjiayou.trecorelib.util.LogUtil;

/**
 * Created by treason on 2017/2/7.
 */

public class TCService extends Service {

    // TAG
    protected final String TAG = this.getClass().getSimpleName();

    // 显示生命周期
    protected final String TAG_LIFE_CYCLE = "life_cycle_service";
    protected boolean SHOW_LIFE_CYCLE = true;

    // var
    protected Context mContext;
    protected Intent mIntent;

    @Override
    public void onCreate() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onCreate");
        }
        super.onCreate();

        // var
        mContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onStartCommand | intent.getAction() -> " + intent.getAction() + " | flags -> " + flags + " | startId -> " + startId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onBind | intent.getAction() -> " + intent.getAction());
        }
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onUnbind | intent.getAction() -> " + intent.getAction());
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if (SHOW_LIFE_CYCLE) {
            LogUtil.i(TAG, TAG_LIFE_CYCLE + " | onDestroy");
        }
        super.onDestroy();
    }
}
