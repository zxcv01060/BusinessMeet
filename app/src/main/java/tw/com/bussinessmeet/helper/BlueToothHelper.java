package tw.com.bussinessmeet.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import tw.com.bussinessmeet.AddIntroductionActivity;
import tw.com.bussinessmeet.R;
import tw.com.bussinessmeet.RequestCode;
import tw.com.bussinessmeet.adapter.UnmatchedDeviceRecyclerViewAdapter;
import tw.com.bussinessmeet.adapter.MatchedDeviceRecyclerViewAdapter;
import tw.com.bussinessmeet.bean.MatchedBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.MatchedDAO;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.service.Impl.MatchedServiceImpl;
import tw.com.bussinessmeet.service.Impl.UserInformationServiceImpl;
import tw.com.bussinessmeet.helper.NotificationHelper;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class BlueToothHelper { 


    private Activity activity;
    private BluetoothAdapter mBluetoothAdapter;
    private IntentFilter filter;
    private UserInformationDAO userInformationDAO;
    private AvatarHelper avatarHelper;
    private ImageView avatar;

    private static final String CHANNEL_1_ID = "channel1" ;
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl() ;
    private MatchedServiceImpl matchedService = new MatchedServiceImpl();
    private MatchedDeviceRecyclerViewAdapter matchedDeviceRecyclerViewAdapter;
    private UnmatchedDeviceRecyclerViewAdapter unmatchedDeviceRecyclerViewAdapter;
    private UserInformationBean userInformationBean;
    private int distance = 0;
    //private BluetoothDevice device = null;
    private NotificationHelper notificationHelper;
    private AsyncTasKHelper.OnResponseListener<String, UserInformationBean> getByIdResponseListener = new AsyncTasKHelper.OnResponseListener<String, UserInformationBean>() {
        @Override
        public Call<ResponseBody<UserInformationBean>> request(String... blueTooth) {
            return userInformationService.getById(blueTooth[0]);
        }
    private SQLiteDatabase db;

        public void onSuccess(UserInformationBean responseBean) {
            Log.d("blueToothSearch","success");
            int count = matchedDeviceRecyclerViewAdapter.getItemCount();
            boolean first = true;
            for(int i = 0; i < count; i++ ){
                if(!first)break;
                if(matchedDeviceRecyclerViewAdapter.getUserInformation(i).getBlueTooth().equals(responseBean.getBlueTooth())){
                    first = false;
                }

            }
            count = unmatchedDeviceRecyclerViewAdapter.getItemCount();
            for(int i = 0; i < count; i++ ){
                if(!first)break;
                if(unmatchedDeviceRecyclerViewAdapter.getUserInformation(i).getBlueTooth().equals(responseBean.getBlueTooth())){
                    first = false;
                }

            }
            if(first) {
                userInformationBean = responseBean;
                matchedBean = new MatchedBean();
                matchedBean.setBlueTooth(getMyBuleTooth());
                matchedBean.setMatchedBlueTooth(userInformationBean.getBlueTooth());
                AsyncTasKHelper.execute(matchedResponseListener, matchedBean);
            }
        }
        @Override
        public void onFail(int status) {
    };
        };
    private MatchedBean matchedBean;
    private AsyncTasKHelper.OnResponseListener<MatchedBean, List<MatchedBean>> matchedResponseListener = new AsyncTasKHelper.OnResponseListener<MatchedBean, List<MatchedBean>>() {
        @Override
        public Call<ResponseBody<List<MatchedBean>>> request(MatchedBean... matchedBeans) {
            return matchedService.search(matchedBeans[0]);
        }
        @Override
        public void onSuccess(List<MatchedBean> matchedBeanList) {
            if (matchedBeanList.size() > 0) {
                Log.d("blueToothSearchmatched",String.valueOf(distance));
                matchedDeviceRecyclerViewAdapter.dataInsert(userInformationBean);
                if (distance <= 5000) {
                    //Log.d("sendmess", String.valueOf(device));
                    notificationHelper.sendMessage(userInformationBean.getBlueTooth(), matchedBeanList.get(0).getMemorandum());
                    //device = null;
                }
            }
        }
        @Override
        public void onFail(int status) {
            unmatchedDeviceRecyclerViewAdapter.dataInsert(userInformationBean);
        }
    };
    // UUID，蓝牙建立链接需要的


    // 选中发送数据的蓝牙设备，全局变量，否则连接在方法执行完就结束了
    private BluetoothDevice selectDevice;
    // 获取到选中设备的客户端串口，全局变量，否则连接在方法执行完就结束了
    private BluetoothSocket clientSocket;
    // 获取到向设备写的输出流，全局变量，否则连接在方法执行完就结束了
    private OutputStream outputStream;

    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private AcceptThreadHelper acceptThread;


    public BlueToothHelper(Activity activity) {
        this.activity = activity;
    }

    public void searchBlueTooth(UserInformationDAO userInformationDAO, MatchedDeviceRecyclerViewAdapter matchedDeviceRecyclerViewAdapter, UnmatchedDeviceRecyclerViewAdapter unmatchedDeviceRecyclerViewAdapter) {
        this.userInformationDAO = userInformationDAO;
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.matchedDeviceRecyclerViewAdapter = matchedDeviceRecyclerViewAdapter;
        this.unmatchedDeviceRecyclerViewAdapter = unmatchedDeviceRecyclerViewAdapter;
        notificationHelper = new NotificationHelper(activity);
        Log.d("resultMainAdapter", String.valueOf(matchedDeviceRecyclerViewAdapter.getItemCount()));
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                UserInformationBean ufb = new UserInformationBean();
                ufb.setBlueTooth(device.getAddress());
                // 遍歷
//                matchedBeanList.add(ufb);
                //matchedDeviceRecyclerViewAdapter.dataInsert(ufb);
            }
        }
        activity.registerReceiver(receiver, filter);

        //setting channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel CHANNEL_1_ID =
                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = activity.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(CHANNEL_1_ID);

        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {


            // 收到的廣播型別
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 從intent中獲取裝置
                Log.d("intocode",action);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e("blueToothsearch",device.getAddress());
                    short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    int iRssi = abs(rssi);
                    // 將藍芽訊號強度換算為距離
                    double power = (iRssi - 59) / 25.0;
//                        String distance = new Formatter().format("%.2f", pow(10, power)).toString();
                    distance = (int) pow(10, power);
                    AsyncTasKHelper.execute(getByIdResponseListener,device.getAddress());






            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // 關閉進度條
//                activity.setProgressBarIndeterminateVisibility(true);
                TextView search_title = activity.findViewById(R.id.search_title);
                search_title.setText("搜尋完成!");
                Toast.makeText(activity, "搜尋完成！", Toast.LENGTH_LONG);
                openGPS(activity);

                // 用於迴圈掃描藍芽的handler
//                mBLHandler.sendEmptyMessageDelayed(1, 10000);
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
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

    public void startBuleTooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //裝置不支援藍芽
            Toast.makeText(activity, "裝置不支援藍芽", Toast.LENGTH_SHORT).show();
            activity.finish();
        } else {
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

        }
    }

    public void bluetooth(Context context) {
        Intent enableBtIntent = new Intent((BluetoothAdapter.ACTION_REQUEST_ENABLE));
        activity.startActivityForResult(enableBtIntent, RequestCode.REQUEST_ENABLE_BT);
    }

    public void openGPS(Context context) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCode.REQUEST_LOCATION);
    }

    public void scanBluth() {
// 設定進度條
        activity.setProgressBarIndeterminateVisibility(true);
        TextView search_title = activity.findViewById(R.id.search_title);
        Log.e("search", String.valueOf(activity));
        Log.e("search", String.valueOf(search_title));
        search_title.setText("正在搜尋...");

// 開始搜尋
//        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//
//        // 这个可以用来设置时间

        mBluetoothAdapter.startDiscovery();
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

    public void requestPermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == RequestCode.REQUEST_LOCATION) {
            // 因為這個 method 會帶回任何權限視窗的結果，所以我們要用 requestCode 來判斷這是哪一個權限

            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // 不等於 PERMISSION_GRANTED 代表被按下拒絕
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);

                    Toast.makeText(activity, "這些權限是為了搜尋附近藍芽裝置，拒絕將無法使用本應用程式。", Toast.LENGTH_LONG).show();


                } else {
                    AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("這些權限是為了搜尋附近藍芽裝置，拒絕將無法使用本應用程式。").setPositiveButton("我需要此权限!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openGPS(activity);
                        }

                    }).setCancelable(false).show();
//                    dialog.setCanceledOnTouchOutside(false);
                }

            } else {
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

    public String getMyBuleTooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = "";
        //確認版本號
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                mServiceField.setAccessible(true);

                Object btManagerService = mServiceField.get(bluetoothAdapter);

                if (btManagerService != null) {
                    bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                }
            } catch (Exception e) {


            }
        } else {
            bluetoothMacAddress = bluetoothAdapter.getAddress();
        }
        return bluetoothMacAddress;
    }

    public void cancelDiscovery() {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    public void matched(String blueToothAddress, String userName, AsyncTasKHelper.OnResponseListener<MatchedBean, MatchedBean> addResponseListener, MatchedDAO matchedDAO) {
        userName = "darkplume";
        // 实例接收客户端传过来的数据线程

        Log.d("blueTooth", blueToothAddress);
        // 判斷是否在搜尋,如果在搜尋，就取消搜尋
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        createConnect(blueToothAddress, addResponseListener, matchedDAO);
        Log.d("outputStream", "success~~~");
        String message = getMyBuleTooth() + ",ask";
        try {
            outputStream.write(message.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public void matchedSuccessReturn(String blueToothAddress){
//        String message = getMyBuleTooth()+",accept";
//
//        try {
//            createConnect(blueToothAddress,true);
//            Log.e("successreturn",message);
//            outputStream.write(message.getBytes("UTF-8"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void createConnect(String matchedAddress, AsyncTasKHelper.OnResponseListener<MatchedBean, MatchedBean> addResponseListener, MatchedDAO matchedDAO) {
        selectDevice = mBluetoothAdapter.getRemoteDevice(matchedAddress);
        Log.d("selectDevice", String.valueOf(selectDevice));
        try {
//            if(clientSocket == null){
            try {
                clientSocket = selectDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (Exception e) {
                Log.e("error", "Error creating socket");
            }

            try {
                clientSocket.connect();
                outputStream = clientSocket.getOutputStream();
                Log.e("", "Connected");
            } catch (IOException e) {
                Log.e("error", e.getMessage());
                try {
                    Log.e("", "trying fallback...");

                    clientSocket = (BluetoothSocket) selectDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(selectDevice, 2);
                    clientSocket.connect();
                    outputStream = clientSocket.getOutputStream();
                    Log.e("", String.valueOf(selectDevice));
                } catch (Exception e2) {
                    Log.e("", "Couldn't establish Bluetooth connection!");
                }
            }
            MatchedBean matchedBean = new MatchedBean();
            matchedBean.setBlueTooth(getMyBuleTooth());
            matchedBean.setMatchedBlueTooth(matchedAddress);
            AsyncTasKHelper.execute(addResponseListener, matchedBean);
            matchedDAO.add(matchedBean);
            Log.d("outputStream", String.valueOf(outputStream));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "配對失敗，請稍後再試", Toast.LENGTH_LONG).show();
        }
    }

    public void startThread(Handler handler) {
        acceptThread = new AcceptThreadHelper(mBluetoothAdapter, MY_UUID, activity, handler);
        acceptThread.start();
    }
}
