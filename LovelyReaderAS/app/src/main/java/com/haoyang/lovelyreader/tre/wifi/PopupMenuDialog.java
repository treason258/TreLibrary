package com.haoyang.lovelyreader.tre.wifi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoyang.lovelyreader.R;
import com.haoyang.lovelyreader.tre.event.OnWifiChangedEvent;
import com.haoyang.lovelyreader.tre.event.OnWifiDialogDismissEvent;
import com.haoyang.lovelyreader.tre.helper.Configs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by masel on 2016/10/10.
 */

public class PopupMenuDialog {
    @Bind(R.id.popup_menu_title)
    TextView mTxtTitle;
    @Bind(R.id.popup_menu_subtitle)
    TextView mTxtSubTitle;
    @Bind(R.id.shared_wifi_state)
    ImageView mImgLanState;
    @Bind(R.id.shared_wifi_state_hint)
    TextView mTxtStateHint;
    @Bind(R.id.shared_wifi_address)
    TextView mTxtAddress;
    @Bind(R.id.shared_wifi_settings)
    Button mBtnWifiSettings;
    @Bind(R.id.shared_wifi_button_split_line)
    View mButtonSplitLine;
    WifiConnectChangedReceiver mWifiConnectChangedReceiver = new WifiConnectChangedReceiver();
    private Context context;
    private Dialog dialog;
    private Display display;

    public PopupMenuDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        EventBus.getDefault().register(this);
    }

    public PopupMenuDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_popup_menu_dialog, null);

        view.setMinimumWidth(display.getWidth());

        dialog = new Dialog(context, R.style.PopupMenuDialogStyle);
        dialog.setContentView(view);
        ButterKnife.bind(this, dialog);
        dialog.setOnDismissListener(this::onDialogDismiss);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    public PopupMenuDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public PopupMenuDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        checkWifiState(WifiUtils.getWifiConnectState(context));
        dialog.show();
        registerWifiConnectChangedReceiver();
    }

    @OnClick({R.id.shared_wifi_cancel, R.id.shared_wifi_settings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shared_wifi_cancel:
                dialog.dismiss();
                break;
            case R.id.shared_wifi_settings:
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                break;
        }
    }

    void registerWifiConnectChangedReceiver() {
        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(mWifiConnectChangedReceiver, intentFilter);
    }

    void unregisterWifiConnectChangedReceiver() {
        context.unregisterReceiver(mWifiConnectChangedReceiver);
    }

//    @Subscribe(tags = {@Tag(Constants.RxBusEventType.WIFI_CONNECT_CHANGE_EVENT)})
//    public void onWifiConnectStateChanged(NetworkInfo.State state) {
//        checkWifiState(state);
//    }

    /**
     * onEvent
     */
    public void onEvent(OnWifiChangedEvent onWifiChangedEvent) {
        Log.d("PopupMenuDialog", "onEvent() called with: onWifiChangedEvent = [" + onWifiChangedEvent + "]");
        checkWifiState(onWifiChangedEvent.getState());
    }

    void checkWifiState(NetworkInfo.State state) {
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            if (state == NetworkInfo.State.CONNECTED) {
                String ip = WifiUtils.getWifiIp(context);
                if (!TextUtils.isEmpty(ip)) {
                    onWifiConnected(ip);
                    return;
                }
            }
            onWifiConnecting();
            return;
        }
        onWifiDisconnected();
    }

    void onWifiDisconnected() {
        mTxtTitle.setText(R.string.wlan_disabled);
        mTxtTitle.setTextColor(context.getResources().getColor(android.R.color.black));
        mTxtSubTitle.setVisibility(View.VISIBLE);
        mImgLanState.setImageResource(R.drawable.shared_wifi_shut_down);
        mTxtStateHint.setText(R.string.fail_to_start_http_service);
        mTxtAddress.setVisibility(View.GONE);
        mButtonSplitLine.setVisibility(View.VISIBLE);
        mBtnWifiSettings.setVisibility(View.VISIBLE);
    }

    void onWifiConnecting() {
        mTxtTitle.setText(R.string.wlan_enabled);
        mTxtTitle.setTextColor(context.getResources().getColor(R.color.colorWifiConnected));
        mTxtSubTitle.setVisibility(View.GONE);
        mImgLanState.setImageResource(R.drawable.shared_wifi_enable);
        mTxtStateHint.setText(R.string.retrofit_wlan_address);
        mTxtAddress.setVisibility(View.GONE);
        mButtonSplitLine.setVisibility(View.GONE);
        mBtnWifiSettings.setVisibility(View.GONE);
    }

    void onWifiConnected(String ipAddr) {
        mTxtTitle.setText(R.string.wlan_enabled);
        mTxtTitle.setTextColor(context.getResources().getColor(R.color.colorWifiConnected));
        mTxtSubTitle.setVisibility(View.GONE);
        mImgLanState.setImageResource(R.drawable.shared_wifi_enable);
        mTxtStateHint.setText(R.string.pls_input_the_following_address_in_pc_browser);
        mTxtAddress.setVisibility(View.VISIBLE);
        mTxtAddress.setText(String.format(context.getString(R.string.http_address), ipAddr, Configs.HTTP_PORT));
        mButtonSplitLine.setVisibility(View.GONE);
        mBtnWifiSettings.setVisibility(View.GONE);
    }

    void onDialogDismiss(DialogInterface dialog) {
        Timber.d("dialog dismiss!");
        EventBus.getDefault().post(new OnWifiDialogDismissEvent(true));
        unregisterWifiConnectChangedReceiver();
        EventBus.getDefault().unregister(this);
    }
}
