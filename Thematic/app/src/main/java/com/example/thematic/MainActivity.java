package com.example.thematic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Set;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

//https://codertw.com/android-%E9%96%8B%E7%99%BC/332688/
public class MainActivity extends AppCompatActivity implements ThematicListAdapter.DevicesClickListener {
    private final static int REQUEST_ENABLE_BT = 1;
    private  String TAG = "MainActivity";
    private RecyclerView recyclerViewThrmatic;
    private TextView matched;
//    private TextView available;
    private List<DeviceItem> deviceItems ;
    private ThematicListAdapter thematicListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    Handler mBLHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    available.setText("");

                    matchedDevices();
                    scanBluth();
                    break;
                default:
                    break;
            }
        }
    };
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 收到的廣播型別
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 從intent中獲取裝置

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String aa = "";


                for(int i = 0; i < deviceItems.size(); i++) {
                    matched = recyclerViewThrmatic.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.matched);
                    if(matched.getText() !=null) {
                        aa = matched.getText().toString();
                        break;

                    }
                }

                if (aa.contains(device.getAddress())) {
                    return;
                } else {
                    // 判斷是否配對過
//                    Log.d(TAG,device.getName());
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                        // 新增到列表
                        short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                        int iRssi = abs(rssi);
                        // 將藍芽訊號強度換算為距離
                        double power = (iRssi - 59) / 25.0;
                        String mm = new Formatter().format("%.2f", pow(10, power)).toString();
                        DeviceItem deviceItem = new DeviceItem();

                        deviceItem.setDeviceAddress(device.getAddress());
                        if(device.getName()==null){
                            deviceItem.setDeviceStatus("連線後獲取該裝置名稱");
                        }else{
                            deviceItem.setDeviceName(device.getName());
                        }
                        deviceItems.add(deviceItem);
//                        available.append(device.getName() + " - " + device.getAddress() + " - " + mm + "m" +device.getBluetoothClass()+"\n");
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // 關閉進度條
                setProgressBarIndeterminateVisibility(true);
                setTitle("搜尋完成！");
                Log.d("MainActivity",String.valueOf(deviceItems.size()));
                createRecyclerViewWeather();
                // 用於迴圈掃描藍芽的handler
                mBLHandler.sendEmptyMessageDelayed(1, 10000);
            }else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        scanBluth();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_OFF:
                       bluetooth(MainActivity.this);
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewThrmatic = (RecyclerView) findViewById(R.id.recycleViewThematic);
        deviceItems = new ArrayList<>();
        Log.d("MainActivity","success");
//        tvDevices = (TextView) findViewById(R.id.tvDecives);
//        matched = (TextView) findViewById(R.id.matched);
//        available = (TextView) findViewById(R.id.available) ;
//        tvDevices.append("測試測試~~\n");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            //裝置不支援藍芽
            Toast.makeText(this, "裝置不支援藍芽", Toast.LENGTH_SHORT).show();
            finish();
        } else{

            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

            registerReceiver(receiver, filter);




//            while(!isGpsEnable(this) && !mBluetoothAdapter.isEnabled()){
                if (isGpsEnable(this)) {
                    openGPS(this);
                }
                if (!mBluetoothAdapter.isEnabled()) {
                    bluetooth(this);
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                   intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
//        startActivityForResult(intent, 2);
                    ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3 );
                } else {
                    mBluetoothAdapter.enable();
                }
//            }
            matchedDevices();
            scanBluth();
        }


    }

    private void scanBluth() {
// 設定進度條
        setProgressBarIndeterminateVisibility(true);
        setTitle("正在搜尋...");
// 判斷是否在搜尋,如果在搜尋，就取消搜尋
//        if (mBluetoothAdapter.isDiscovering()) {
//            mBluetoothAdapter.cancelDiscovery();
//        }
// 開始搜尋
//        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//
//        // 这个可以用来设置时间

        mBluetoothAdapter.startDiscovery();
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver); super.onDestroy();
    }
    public static final boolean isGpsEnable(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
//    private void buildAlertMessageNoGps() {
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                        dialog.cancel();
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();
//    }

    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void bluetooth(Context context){
        Intent enableBtIntent = new Intent((BluetoothAdapter.ACTION_REQUEST_ENABLE));
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }
    public void matchedDevices(){
//        matched.setText("");
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//
//            // 判斷是否有配對過的裝置
            List<String> mDevicesList = new ArrayList<>();
//
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    // 遍歷
                    DeviceItem deviceItem = new DeviceItem();
                    if(device.getName() == null){
                        deviceItem.setDeviceName("連線後取得該裝置名稱");
                    }else{
                        deviceItem.setDeviceName(device.getName());
                    }
                    deviceItem.setDeviceAddress(device.getAddress());
                    deviceItems.add(deviceItem);
//                    mDevicesList.add(device.getAddress());
//                    matched.append(device.getName() + " - " + device.getAddress() + "\n");
                }
            }
    }
    private void createRecyclerViewWeather() {
        recyclerViewThrmatic.setLayoutManager(new LinearLayoutManager(this));

        thematicListAdapter = new ThematicListAdapter(this,this.deviceItems);

        thematicListAdapter.setClickListener(this);
        recyclerViewThrmatic.setAdapter(thematicListAdapter);

    }

    @Override
    public void onDevicesClick(View view, int position) {

    }
}
