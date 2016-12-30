package com.example.frank.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.main.util.FitListView;
import com.example.frank.main.util.ToastUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000; // Stops scanning after 10 seconds.

    private TextView mTvNumber;
    private Button mBtnDevice;
    private FitListView mLvDevice;
    private FitListView mLvDeviceContainer;
    private ScrollView mSvData;
    private TextView mTvData;

    private boolean mScanning;
    private String deviceText = null;
    private BluetoothLeService mBluetoothLeService = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private LinkedList<BluetoothDevice> mDeviceContainer = new LinkedList<>();
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<>();

    private ArrayAdapter<String> mArrayAdapter;
    private List<String> mAddressList = new ArrayList<>();
    private ArrayAdapter<String> mArrayAdapterContainer;
    private List<String> mAddressListContainer = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceText = "接收设备数：";
        initUI();
        initBLE();

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        Log.d(TAG, "Try to bindService=" + bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE));
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);

        if (mBluetoothLeService != null) {
            mBluetoothLeService.close();
            mBluetoothLeService = null;
        }

        Log.i(TAG, "MainActivity closed!!!");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        scanLeDevice(true);
        mScanning = true;
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                clearDevice();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(250);
                            scanLeDevice(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                clearDevice();
                break;
        }
        return true;
    }

    private void initUI() {
        // Sets up UI references.
        mTvNumber = (TextView) findViewById(R.id.tv_number);
        mBtnDevice = (Button) findViewById(R.id.btn_device);
        mLvDevice = (FitListView) findViewById(R.id.lv_device);
        mLvDeviceContainer = (FitListView) findViewById(R.id.lv_device_container);
        mSvData = (ScrollView) findViewById(R.id.sv_data);
        mTvData = (TextView) findViewById(R.id.tv_data);

        mTvNumber.setText(deviceText + "0");
        mBtnDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        mArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, mAddressList);
        mLvDevice.setAdapter(mArrayAdapter);
        mLvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ToastUtil.show(MainActivity.this, "position -> " + position);
            }
        });

        mArrayAdapterContainer = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, mAddressListContainer);
        mLvDeviceContainer.setAdapter(mArrayAdapterContainer);
        mLvDeviceContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ToastUtil.show(MainActivity.this, "position -> " + position);
            }
        });
    }

    private void initBLE() {
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "您的设备不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                if (mBluetoothAdapter.isEnabled()) {
                    scanLeDevice(true);
                    mScanning = true;
                } else {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        }).start();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("接收列表");
        if (!mDeviceList.isEmpty()) {
            String[] strDevice = new String[mDeviceList.size()];
            for (int i = 0; i < mDeviceList.size(); i++) {
                BluetoothDevice device = mDeviceList.get(i);
                strDevice[i] = device.getName() + " - " + device.getAddress();
            }
            builder.setItems(strDevice, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this, "address -> " + mDeviceList.get(i).getAddress(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            builder.setItems(new String[]{"No Device"}, null);
        }
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(SCAN_PERIOD);

                        if (mScanning) {
                            mScanning = false;
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                            invalidateOptionsMenu();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

        invalidateOptionsMenu();
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!mDeviceContainer.isEmpty()) { //
                                if (!isEquals(device)) {
                                    connectBle(device);
                                }
                            } else {
                                connectBle(device);
                            }
                        }
                    });
                }
            };

    private boolean isEquals(BluetoothDevice device) {
        for (BluetoothDevice mDdevice : mDeviceContainer) {
            if (mDdevice.equals(device)) {
                return true;
            }
        }
        return false;
    }

    private void connectBle(BluetoothDevice device) {
        mDeviceContainer.add(device);
        while (true) {
            if (mBluetoothLeService != null) {
                mBluetoothLeService.connect(device.getAddress());
                break;
            } else {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            Log.e(TAG, "mBluetoothLeService is okay");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.e(TAG, "Only gatt, just wait");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                if (!mDeviceList.isEmpty()) {
                    String strAddress = intent.getStringExtra(BluetoothLeService.EXTRA_ADDRESS);
                    if (removeDevice(strAddress)) {
                        int deviceNum = mDeviceList.size() - 1;
                        mTvNumber.setText(deviceText + deviceNum);
                        refreshListView();
                    }
                }

                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                if (!mDeviceContainer.isEmpty()) {
                    String strAddress = intent.getStringExtra(BluetoothLeService.EXTRA_ADDRESS);
                    for (BluetoothDevice bluetoothDevice : mDeviceContainer) {
                        if (bluetoothDevice.getAddress().equals(strAddress)) {
                            mDeviceList.add(bluetoothDevice);
                        }
                    }
                }
                mTvNumber.setText(deviceText + mDeviceList.size());
                refreshListView();
                Toast.makeText(MainActivity.this, "Discover GATT Services", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Discover GATT Services");
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.i(TAG, "ACTION_DATA_AVAILABLE");
//                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
//                if (data != null) {
//                    if (mTvData.length() > 500) {
//                        mTvData.setText("");
//                    }
//                    mTvData.append(data);
//
//                    mSvData.post(new Runnable() {
//                        public void run() {
//                            mSvData.fullScroll(ScrollView.FOCUS_DOWN);
//                        }
//                    });
//                }
                String address = intent.getStringExtra(BluetoothLeService.EXTRA_ADDRESS);
                if (address != null) {
                    if (mTvData.length() > 500) {
                        mTvData.setText("");
                    }
                    mTvData.append(address);

                    mSvData.post(new Runnable() {
                        public void run() {
                            mSvData.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }
        }
    };

    private boolean removeDevice(String strAddress) {
        for (final BluetoothDevice bluetoothDevice : mDeviceList) {
            if (bluetoothDevice.getAddress().equals(strAddress)) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(250);
                            mDeviceList.remove(bluetoothDevice);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshListView();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                return true;
            }
        }
        return false;
    }

    private void clearDevice() {
        mBluetoothLeService.disconnect();
        mDeviceContainer.clear();
        mDeviceList.clear();
        mTvData.setText("");
        mTvNumber.setText(deviceText + "0");
        refreshListView();
    }

    private void refreshListView() {
        mAddressList.clear();
        for (int i = 0; i < mDeviceList.size(); i++) {
            BluetoothDevice device = mDeviceList.get(i);
            mAddressList.add(device.getName() + " - " + device.getAddress());
        }
        mArrayAdapter.notifyDataSetChanged();

        mAddressListContainer.clear();
        for (int i = 0; i < mDeviceContainer.size(); i++) {
            BluetoothDevice device = mDeviceContainer.get(i);
            mAddressListContainer.add(device.getName() + " - " + device.getAddress());
        }
        mArrayAdapterContainer.notifyDataSetChanged();
    }
}
