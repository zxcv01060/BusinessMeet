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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import tw.com.bussinessmeet.background.NotificationService;
import tw.com.bussinessmeet.bean.FriendBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.FriendDAO;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.service.Impl.MatchedServiceImpl;
import tw.com.bussinessmeet.service.Impl.UserInformationServiceImpl;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
public class BlueToothHelper { 


    private Activity activity;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private IntentFilter filter;
    private UserInformationDAO userInformationDAO;
    private AvatarHelper avatarHelper;
    private ImageView avatar;

    private static final String CHANNEL_1_ID = "channel1" ;
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl() ;
    private MatchedServiceImpl matchedService = new MatchedServiceImpl();
    private MatchedDeviceRecyclerViewAdapter matchedDeviceRecyclerViewAdapter;
    private UnmatchedDeviceRecyclerViewAdapter unmatchedDeviceRecyclerViewAdapter;

    private  Map<String,UserInformationBean> userInformationBeanMap = new HashMap<>();
    private  Map<String,UserInformationBean> repeatBeanMap = new HashMap<>();

    private  Map<String,UserInformationBean> backgroundBeanMap = new HashMap<>();
    private int distance = 0;
    private int backgroundDistance = 0;
    private boolean first = true;
    //private BluetoothDevice device = null;
    private FriendBean friendBean;
    private NotificationHelper notificationHelper;
    private AsyncTasKHelper.OnResponseListener<String, UserInformationBean> getByIdResponseListener = new AsyncTasKHelper.OnResponseListener<String, UserInformationBean>() {
        @Override
        public Call<ResponseBody<UserInformationBean>> request(String... blueTooth) {
            return userInformationService.getByBlueTooth(blueTooth[0]);
        }
        public void onSuccess(UserInformationBean responseBean) {
            Log.d("blueToothSearch","success");
                String searchId = responseBean.getUserId();

                userInformationBeanMap.put(searchId,responseBean);
                String userId = userInformationDAO.getId(responseBean.getBluetooth());
                if(userId==null)
                    userInformationDAO.add(responseBean);
                friendBean = new FriendBean();
                friendBean.setMatchmakerId(getUserId());
                friendBean.setFriendId(userInformationBeanMap.get(searchId).getUserId());
                AsyncTasKHelper.execute(matchedResponseListener, friendBean);
        }
        @Override
        public void onFail(int status) {

        };
    };

    private AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>> matchedResponseListener = new AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>>() {
        @Override
        public Call<ResponseBody<List<FriendBean>>> request(FriendBean... friendBeans) {
            return matchedService.search(friendBeans[0]);
        }
        @Override
        public void onSuccess(List<FriendBean> friendBeanList) {
            FriendBean friendBean = friendBeanList.get(0);
            UserInformationBean userInformationBean = userInformationBeanMap.get(friendBean.getFriendId());
            if (friendBeanList.size() > 1 || (friendBeanList.size() == 1 && (friendBeanList.get(0).getCreateDate() != null && !friendBeanList.get(0).equals("")))) {
               Toast.makeText(activity,"success",Toast.LENGTH_SHORT);
                matchedDeviceRecyclerViewAdapter.dataInsert(userInformationBean);
                if (distance <= 5000 && first) {
                    Log.d("sendmess", String.valueOf("==========================="));
//                    notificationHelper.sendMessage(userInformationBean, friendBeanList.get(0).getMemorandum());
                }
            }else{
                unmatchedDeviceRecyclerViewAdapter.dataInsert(userInformationBean);
            }
        }
        @Override
        public void onFail(int status) {
            Log.d("intomatched","success");
            //unmatchedDeviceRecyclerViewAdapter.dataInsert(userInformationBean);
        }
    };

    // UUID，蓝牙建立链接需要的


    // 选中发送数据的蓝牙设备，全局变量，否则连接在方法执行完就结束了
    private BluetoothDevice selectDevice;
    // 获取到选中设备的客户端串口，全局变量，否则连接在方法执行完就结束了
    private BluetoothSocket clientSocket;
    // 获取到向设备写的输出流，全局变量，否则连接在方法执行完就结束了
    private OutputStream outputStream = null;

    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private AcceptThreadHelper acceptThread;

    private NotificationService notificationService = null;
    public BlueToothHelper(Activity activity) {
        DBHelper dbHelper = new DBHelper(activity);
        this.userInformationDAO = new UserInformationDAO(dbHelper);
        this.activity = activity;
    }
    public BlueToothHelper(NotificationService notificationService) {
        Log.e("service ","BlueToothHelper");
        DBHelper dbHelper = new DBHelper(notificationService);
        this.userInformationDAO = new UserInformationDAO(dbHelper);
        this.notificationService = notificationService;
    }

    public void settingFilter(){
        filter = null;
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
    }

    public void searchBlueToothInBackground(){
        Log.e("service ","searchBlueToothInBackground");
        settingFilter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel CHANNEL_1_ID =
                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = notificationService.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(CHANNEL_1_ID);

        }
        notificationHelper = new NotificationHelper(notificationService);
        notificationService.registerReceiver(backgroundReceiver,filter);
        mBluetoothAdapter.startDiscovery();
    }
    public void searchBlueTooth(UserInformationDAO userInformationDAO, MatchedDeviceRecyclerViewAdapter matchedDeviceRecyclerViewAdapter, UnmatchedDeviceRecyclerViewAdapter unmatchedDeviceRecyclerViewAdapter) {

        this.matchedDeviceRecyclerViewAdapter = matchedDeviceRecyclerViewAdapter;
        this.unmatchedDeviceRecyclerViewAdapter = unmatchedDeviceRecyclerViewAdapter;
        notificationHelper = new NotificationHelper(activity);
        Log.d("resultMainAdapter", String.valueOf(matchedDeviceRecyclerViewAdapter.getItemCount()));
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                removeMatched(device.getAddress());
            }
        }
        settingFilter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel CHANNEL_1_ID =
                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = activity.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(CHANNEL_1_ID);

        }
        activity.registerReceiver(receiver, filter);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {


            // 收到的廣播型別
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                TextView search_title = activity.findViewById(R.id.search_title);
                search_title.setText("搜尋中...");
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
                    String searchAddress = device.getAddress();
                    first = excludeRepeat(searchAddress);
                    if(first) {
                        AsyncTasKHelper.execute(getByIdResponseListener, searchAddress);
                    }





            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // 關閉進度條
//                activity.setProgressBarIndeterminateVisibility(true);
                TextView search_title = activity.findViewById(R.id.search_title);
                search_title.setText("搜尋完成!");
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
    private final BroadcastReceiver backgroundReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("testbackground","success");

            // 收到的廣播型別
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                int iRssi = abs(rssi);
                // 將藍芽訊號強度換算為距離
                double power = (iRssi - 59) / 25.0;
//                        String distance = new Formatter().format("%.2f", pow(10, power)).toString();
                backgroundDistance = (int) pow(10, power);
                String searchAddress = device.getAddress();
                AsyncTasKHelper.execute(backgroundUserInformationResponseListener, searchAddress);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                mBLHandler.sendEmptyMessageDelayed(1, 100000);
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        mBluetoothAdapter.startDiscovery();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_OFF:

                        bluetooth(notificationService);
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
//        Intent enableBtIntent = new Intent((BluetoothAdapter.ACTION_REQUEST_ENABLE));
//        activity.startActivityForResult(enableBtIntent, RequestCode.REQUEST_ENABLE_BT);
        discoverable();
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
        mBluetoothAdapter.startDiscovery();
    }
    public void discoverable(){
        Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        enable.putExtra(
                BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                3600);
        activity.startActivityForResult(enable,RequestCode.REQUEST_DISCOVERABLE);
        activity.unregisterReceiver(receiver);
    }


    private Handler mBLHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mBluetoothAdapter.startDiscovery();
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
                    AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("這些權限是為了搜尋附近藍芽裝置，拒絕將無法使用本應用程式。").setPositiveButton("我需要此權限!", new DialogInterface.OnClickListener() {
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
        }else if(requestCode == RequestCode.REQUEST_DISCOVERABLE){
//            Toast.makeText(activity,"test",Toast.LENGTH_LONG);
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
//        String bluetoothMacAddress = "F0:EE:10:FC:C5:2D";
        String bluetoothMacAddress = "02:00:00:00:00:00";
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
                e.printStackTrace();

            }
        } else {
            bluetoothMacAddress = bluetoothAdapter.getAddress();
        }

        return bluetoothMacAddress;
    }
    public String getUserId() {
        String bluetoothMacAddress = getMyBuleTooth();
        String userId = "";
        System.out.println("======================"+bluetoothMacAddress+"================");
        if(bluetoothMacAddress!=null && !bluetoothMacAddress.equals("02:00:00:00:00:00")){
            userId = userInformationDAO.getId(bluetoothMacAddress);
        }
        System.out.println(userId);
        return userId;
    }

    public void cancelDiscovery() {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    public void matched(String blueToothAddress, String userName, AsyncTasKHelper.OnResponseListener<FriendBean, FriendBean> addResponseListener, FriendDAO friendDAO) {
        userName = "darkplume";
        // 实例接收客户端传过来的数据线程

        // 判斷是否在搜尋,如果在搜尋，就取消搜尋
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        createConnect(blueToothAddress, addResponseListener, friendDAO);

    }

    public void createConnect(String matchedAddress, AsyncTasKHelper.OnResponseListener<FriendBean, FriendBean> addResponseListener, FriendDAO friendDAO) {
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

                String message = getUserId();
                outputStream.write(message.getBytes("UTF-8"));
                FriendBean friendBean = new FriendBean();
            friendBean.setMatchmakerId(getUserId());
            friendBean.setFriendId(userInformationDAO.getId(matchedAddress));
            AsyncTasKHelper.execute(addResponseListener, friendBean);

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
    public boolean excludeRepeat(String bluetoothAddress){
        first = false;

        if(repeatBeanMap.get(bluetoothAddress)==null){
            first = true;
            UserInformationBean userInformationBean = new UserInformationBean();
            userInformationBean.setBluetooth(bluetoothAddress);
            repeatBeanMap.put(bluetoothAddress,userInformationBean);
        };

        return  first;
    }
    public void removeMatched(String matchedAddress){
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(matchedAddress);
        try {
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private FriendBean backgroundBean;
    private AsyncTasKHelper.OnResponseListener<String, UserInformationBean> backgroundUserInformationResponseListener = new AsyncTasKHelper.OnResponseListener<String, UserInformationBean>() {
        @Override
        public Call<ResponseBody<UserInformationBean>> request(String... blueTooth) {
            return userInformationService.getByBlueTooth(blueTooth[0]);
        }
        public void onSuccess(UserInformationBean responseBean) {
            Log.d("blueToothSearch","success");
            String searchId = responseBean.getUserId();
            backgroundBeanMap.remove(searchId);
            backgroundBeanMap.put(searchId,responseBean);
            backgroundBean = new FriendBean();
            backgroundBean.setMatchmakerId(getUserId());
            backgroundBean.setFriendId(backgroundBeanMap.get(searchId).getUserId());
            AsyncTasKHelper.execute(backgroundMatchedResponseListener, backgroundBean);
        }
        @Override
        public void onFail(int status) {

        };
    };
    private AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>> backgroundMatchedResponseListener = new AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>>() {
        @Override
        public Call<ResponseBody<List<FriendBean>>> request(FriendBean... friendBeans) {
            return matchedService.search(friendBeans[0]);
        }
        @Override
        public void onSuccess(List<FriendBean> friendBeanList) {
            FriendBean friendBean = friendBeanList.get(0);
            UserInformationBean userInformationBean = backgroundBeanMap.get(friendBean.getFriendId());
            if (friendBeanList.size() > 1 || (friendBeanList.size() == 1 && (friendBeanList.get(0).getCreateDate() != null && !friendBeanList.get(0).equals("")))) {

                if (backgroundDistance <= 10000 ) {
                    Log.d("sendmess", String.valueOf("==========================="));
                    notificationHelper.sendBackgroundMessage(userInformationBean, friendBeanList.get(0).getRemark());
                }
            }
        }
        @Override
        public void onFail(int status) {
            Log.d("intomatched","success");
            //unmatchedDeviceRecyclerViewAdapter.dataInsert(userInformationBean);
        }
    };
}
