package tw.com.bussinessmeet.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Set;


import tw.com.bussinessmeet.AddIntroductionActivity;
import tw.com.bussinessmeet.MainActivity;
import tw.com.bussinessmeet.RequestCode;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.FriendsIntroductionActivity;
import tw.com.bussinessmeet.MatchedDeviceRecyclerViewAdapter;
import tw.com.bussinessmeet.NotificationActivity;
import tw.com.bussinessmeet.R;
import tw.com.bussinessmeet.UnmatchedDeviceRecyclerViewAdapter;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class BlueToothHelper {

    private Activity activity;
    private BluetoothAdapter mBluetoothAdapter;
    private IntentFilter filter;
    private UserInformationDAO userInformationDAO;
    private List<UserInformationBean> unmatchedBeanList;
    private List<UserInformationBean> matchedBeanList;

    private MatchedDeviceRecyclerViewAdapter matchedDeviceRecyclerViewAdapter;
    private UnmatchedDeviceRecyclerViewAdapter unmatchedDeviceRecyclerViewAdapter;
    public BlueToothHelper(Activity activity) {
        this.activity = activity;
    }
    public void searchBlueTooth(UserInformationDAO userInformationDAO, MatchedDeviceRecyclerViewAdapter matchedDeviceRecyclerViewAdapter, UnmatchedDeviceRecyclerViewAdapter unmatchedDeviceRecyclerViewAdapter){
        this.userInformationDAO = userInformationDAO;
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.matchedDeviceRecyclerViewAdapter = matchedDeviceRecyclerViewAdapter;
        this.unmatchedDeviceRecyclerViewAdapter = unmatchedDeviceRecyclerViewAdapter;
        Log.d("resultMainAdapter", String.valueOf(matchedDeviceRecyclerViewAdapter.getItemCount()));
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                UserInformationBean ufb = new UserInformationBean();
                ufb.setBlueTooth(device.getAddress());
                // 遍歷
//                matchedBeanList.add(ufb);
                matchedDeviceRecyclerViewAdapter.dataInsert(ufb);
            }
        }
        activity.registerReceiver(receiver, filter);

        //setting channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("MyNotifications","MyNotifications",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = activity.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 收到的廣播型別
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 從intent中獲取裝置
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String aa = "";
                if (aa.contains(device.getAddress())) {
                    return;
                } else {
                    // 判斷是否配對過
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        // 新增到列表
                        short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                        int iRssi = abs(rssi);
                        // 將藍芽訊號強度換算為距離
                        double power = (iRssi - 59) / 25.0;
//                        String distance = new Formatter().format("%.2f", pow(10, power)).toString();
                        int distance = (int)pow(10, power);
                            if(distance<5){
                                sendMessage();
                            }
                        UserInformationBean ufb = new UserInformationBean();
                        ufb.setBlueTooth(device.getAddress());
//                        Cursor result = userInformationDAO.searchAll(ufb);
//                        result.moveToFirst();
//                        ufb.setAvatar(result.getString(result.getColumnIndex("avatar")));
                        unmatchedDeviceRecyclerViewAdapter.dataInsert(ufb);
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // 關閉進度條
//                activity.setProgressBarIndeterminateVisibility(true);
                openGPS(activity);
                Toast.makeText(activity,"搜尋完成！",Toast.LENGTH_LONG);
                // 用於迴圈掃描藍芽的handler
//                mBLHandler.sendEmptyMessageDelayed(1, 10000);
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

                        bluetooth(activity);
                        break;
                }
            }
        }
    };

    public void startBuleTooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
       if (mBluetoothAdapter == null) {
            //裝置不支援藍芽
            Toast.makeText(activity, "裝置不支援藍芽", Toast.LENGTH_SHORT).show();
            activity.finish();
        } else{
//            while(!isGpsEnable(this) && !mBluetoothAdapter.isEnabled()){
            if (!mBluetoothAdapter.isEnabled()) {
                bluetooth(activity);
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                   intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
//        startActivityForResult(intent, 2);

            } else {
                mBluetoothAdapter.enable();
            }
//            }
            scanBluth();
        }
    }

    public void bluetooth(Context context){
        Intent enableBtIntent = new Intent((BluetoothAdapter.ACTION_REQUEST_ENABLE));
        activity.startActivityForResult(enableBtIntent, RequestCode.REQUEST_ENABLE_BT);
    }
    public  void openGPS(Context context) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCode.REQUEST_LOCATION );
    }
    private void scanBluth() {
// 設定進度條
        activity.setProgressBarIndeterminateVisibility(true);
        activity.setTitle("正在搜尋...");
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


    public String getMyBuleTooth(){
        return mBluetoothAdapter.getAddress();
    }

    Handler mBLHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    scanBluth();
                    break;
                default:
                    break;
            }
        }
    };
    public void requestPermissionsResult(int requestCode, int[] grantResults)  {
        if (requestCode == RequestCode.REQUEST_LOCATION) {
            // 因為這個 method 會帶回任何權限視窗的結果，所以我們要用 requestCode 來判斷這是哪一個權限

            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // 不等於 PERMISSION_GRANTED 代表被按下拒絕
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {

                    Intent intent =new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);

                    Toast.makeText(activity,"這些權限是為了搜尋附近藍芽裝置，拒絕將無法使用本應用程式。",Toast.LENGTH_LONG).show();


                }else{
                    AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("這些權限是為了搜尋附近藍芽裝置，拒絕將無法使用本應用程式。").setPositiveButton("我需要此权限!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           openGPS(activity);
                        }
                    }).show();
                }

            }else{
                checkPermission();

            }
        }
    }
    public boolean checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setClass(activity, AddIntroductionActivity.class);
            activity.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
    private void sendMessage() {

        String message = "This is a notific.";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                activity,"MyNotifications"
        )
                .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                .setContentText("New Notification")
                .setContentText(message)
                .setAutoCancel(true);

        //宣告Intent物件 跳至friends_introduction
        Intent intent = new Intent(activity,
                FriendsIntroductionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message",message);

        // 宣告一個 PendingIntent 的物件(執行完並不會馬上啟動,點訊息的時候才會跳到別的 Activity)
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,
                0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        //定義一個訊息管理者 和系統要 取得訊息管理者的物件
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(
                Context.NOTIFICATION_SERVICE
        );

        //要求傳送一個訊息
        notificationManager.notify(0,builder.build());
    }

}
