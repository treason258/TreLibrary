package com.haoyang.lovelyreader.tre.event;

import android.net.NetworkInfo;

/**
 * Created by xin on 2019/1/7.
 */

public class OnWifiChangedEvent {

    private NetworkInfo.State state;

    public OnWifiChangedEvent(NetworkInfo.State state) {
        this.state = state;
    }

    public NetworkInfo.State getState() {
        return state;
    }

    public void setState(NetworkInfo.State state) {
        this.state = state;
    }
}
