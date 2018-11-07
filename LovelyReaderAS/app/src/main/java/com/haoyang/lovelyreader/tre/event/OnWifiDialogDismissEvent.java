package com.haoyang.lovelyreader.tre.event;

/**
 * Created by treason on 2018/11/8.
 */

public class OnWifiDialogDismissEvent {

    private boolean dismiss;

    public OnWifiDialogDismissEvent(boolean dismiss) {
        this.dismiss = dismiss;
    }

    public boolean isDismiss() {
        return dismiss;
    }

    public void setDismiss(boolean dismiss) {
        this.dismiss = dismiss;
    }
}
