package tw.com.bussinessmeet.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;


import retrofit2.Call;
import tw.com.bussinessmeet.AddIntroductionActivity;
import tw.com.bussinessmeet.RequestCode;
import tw.com.bussinessmeet.bean.MatchedBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.MatchedDAO;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.FriendsIntroductionActivity;
import tw.com.bussinessmeet.MatchedDeviceRecyclerViewAdapter;
import tw.com.bussinessmeet.NotificationActivity;
import tw.com.bussinessmeet.R;
import tw.com.bussinessmeet.UnmatchedDeviceRecyclerViewAdapter;
import tw.com.bussinessmeet.helper.NotificationHelper;
import tw.com.bussinessmeet.service.Impl.MatchedServiceImpl;
import tw.com.bussinessmeet.service.Impl.UserInformationServiceImpl;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import android.graphics.BitmapFactory;

public class BlueToothHelper {


    private Activity activity;
    private BluetoothAdapter mBluetoothAdapter;
    private IntentFilter filter;
    private UserInformationDAO userInformationDAO;
    private AvatarHelper avatarHelper;
    private ImageView avatar;

    private static final String CHANNEL_1_ID = "channel1" ;
    private MatchedDeviceRecyclerViewAdapter matchedDeviceRecyclerViewAdapter;
    private UnmatchedDeviceRecyclerViewAdapter unmatchedDeviceRecyclerViewAdapter;
    private NotificationHelper notificationHelper;
    private Cursor cursor;
    private String tableName = "Matched";
    private String[] column = new String[]{"blue_tooth","matched_blue_tooth","memorandum","create_date","modify_date"};
    private String whereClause = "m_sno = ?";
    //private MatchedDAO db;
    private SQLiteDatabase db;


    private MatchedBean matchedBean = new MatchedBean();
    private MatchedServiceImpl matchedService = new MatchedServiceImpl();
    private AsyncTasKHelper.OnResponseListener<MatchedBean, List<MatchedBean>> searchResponseListener =
            new AsyncTasKHelper.OnResponseListener<MatchedBean, List<MatchedBean>>() {
                @Override
                public Call<ResponseBody<List<MatchedBean>>> request(MatchedBean... matchedBeans) {

                    return matchedService.search(matchedBeans[0]);
                }

                @Override
                public void onSuccess(List<MatchedBean> MatchedBeanList) {
                    matchedBean = MatchedBeanList.get(0);
                }

                @Override
                public void onFail(int status) {

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
    private  AcceptThreadHelper acceptThread;



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
        notificationHelper = new NotificationHelper(activity);
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

        //notificationHelper = new NotificationHelper();
        //createNotificationChannel
        //notificationHelper.createNotificationChannel();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 =
                    new NotificationChannel(
                            CHANNEL_1_ID,
                            "channel1",
                            NotificationManager.IMPORTANCE_HIGH
                    );
            channel1.setDescription("This is channel 1");

            NotificationManager manager = activity.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }
    }




    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {


            // 收到的廣播型別

                String action = intent.getAction();
                Log.d("into",action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 從intent中獲取裝置
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String aa = "";
                Log.d("intomatch",device.getAddress());
                if (aa.contains(device.getAddress())) {
                    return;
                } else {
                    // 判斷是否配對過
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        // 新增到列表

                        UserInformationBean ufb = new UserInformationBean();
                        ufb.setBlueTooth(device.getAddress());
//                        Cursor result = userInformationDAO.searchAll(ufb);
//                        result.moveToFirst();
//                        ufb.setAvatar(result.getString(result.getColumnIndex("avatar")));
                        unmatchedDeviceRecyclerViewAdapter.dataInsert(ufb);
                    }else{
                        Log.d("intomatchmatched",device.getAddress());
                        DBHelper Dh = new DBHelper(context);
                        MatchedDAO matchedDAO = new MatchedDAO(Dh);
                        MatchedBean matchedBean = new MatchedBean();
                        matchedBean.setBlueTooth(getMyBuleTooth());
                        matchedBean.setMatchedBlueTooth(device.getAddress());
                        Cursor cursor = matchedDAO.search(matchedBean);

                       /* String where = "";
                        ArrayList<String> args = new ArrayList<>();
                        Cursor cursor = db.query(tableName, column, where, args.toArray(new String[0]),null,null,null);*/

                        //String result = matchedDAO.getById(matchedBlueTooth);
                        //cursor.moveToFirst();
                        Log.d("bluetoothmatched",device.getAddress());
                        Log.d("bluetoothmatched",cursor.toString());
                        if(cursor.moveToNext()) {
                            short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                            int iRssi = abs(rssi);
                            // 將藍芽訊號強度換算為距離
                            double power = (iRssi - 59) / 25.0;
//                        String distance = new Formatter().format("%.2f", pow(10, power)).toString();
                            int distance = (int)pow(10, power);
                            if(distance<5000){

                                notificationHelper.sendMessage(device.getAddress());
                            }
                        }

                    }

                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // 關閉進度條
//                activity.setProgressBarIndeterminateVisibility(true);
                TextView search_title = activity.findViewById(R.id.search_title);
                search_title.setText("搜尋完成!");
                Toast.makeText(activity,"搜尋完成！",Toast.LENGTH_LONG);
                openGPS(activity);

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

        }
    }

    public void bluetooth(Context context){
        Intent enableBtIntent = new Intent((BluetoothAdapter.ACTION_REQUEST_ENABLE));
        activity.startActivityForResult(enableBtIntent, RequestCode.REQUEST_ENABLE_BT);
    }
    public  void openGPS(Context context) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCode.REQUEST_LOCATION );
    }
    public void scanBluth() {
// 設定進度條
        activity.setProgressBarIndeterminateVisibility(true);
        TextView search_title =  activity.findViewById(R.id.search_title);
        Log.e("search",String.valueOf(activity));
        Log.e("search",String.valueOf(search_title));
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

                    }).setCancelable(false).show();
//                    dialog.setCanceledOnTouchOutside(false);
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

    public void sendMessage() {

        //avatar = (ImageView) findViewById(R.id.edit_person_photo);
        AvatarHelper avatarHelper = new AvatarHelper();
        Log.d("seedmess","ness");
        UserInformationBean ufb = new UserInformationBean();
        ufb.setBlueTooth(getMyBuleTooth());
        Cursor result = userInformationDAO.searchAll(ufb);
        result.moveToFirst();

        //Bitmap profilePhoto = avatar.setImageBitmap(avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))));
        String userName;

        userName = "邱殷龍";
        String title1 = userName;
        String message1 = "原音股份有限公司";
        String title2 = "李赫宰";
        String message2 = " SM娛樂公司";



        NotificationCompat.Builder notification1 = new NotificationCompat.Builder(
                activity, CHANNEL_1_ID
        )
                .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                .setContentTitle(title1)
                .setContentText(message1)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setColor(Color.rgb(4,42,88))
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setLargeIcon(avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))))
                .setGroup("group");


        NotificationCompat.Builder notification2 = new NotificationCompat.Builder(
                activity, CHANNEL_1_ID
        )
                .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                .setContentTitle(title2)
                .setContentText(message2)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setColor(Color.rgb(4,42,88))
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setLargeIcon(avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))))
                .setGroup("group");
        NotificationCompat.Builder summaryNotification = new NotificationCompat.Builder(
                activity, CHANNEL_1_ID
        )
                .setSmallIcon(R.drawable.ic_insert_comment_black_24dp)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(title2 + " " + message2)
                        .addLine(title1 + " " + message1)
                        .setBigContentTitle("2 new messages"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL) //設置鈴聲和振動 Android 7.1（API級別25）及更低版本上
                //.setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setColor(Color.rgb(4,42,88))
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setGroup("group")
                .setColor(Color.BLUE)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true);


        //宣告Intent物件 跳至friends_introduction
        Intent intent = new Intent(activity,
                FriendsIntroductionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", title1);

        // 宣告一個 PendingIntent 的物件(執行完並不會馬上啟動,點訊息的時候才會跳到別的 Activity)
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification1.setContentIntent(pendingIntent);
        notification2.setContentIntent(pendingIntent);
        summaryNotification.setContentIntent(pendingIntent);

        //定義一個訊息管理者 和系統要 取得訊息管理者的物件
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(
                Context.NOTIFICATION_SERVICE
        );

        //要求傳送一個訊息
        //id若一樣，則為更新通知，之前的通知會不見
        SystemClock.sleep(1000);
        notificationManager.notify(2, notification1.build());
        SystemClock.sleep(1000);
        notificationManager.notify(3, notification2.build());
        SystemClock.sleep(1000);
        notificationManager.notify(4, summaryNotification.build());
    }
    public String getMyBuleTooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = "";
        //確認版本號
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
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
    public void cancelDiscovery(){
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    public void matched(String blueToothAddress,String userName){
        userName = "darkplume";
        // 实例接收客户端传过来的数据线程

        Log.d("blueTooth",blueToothAddress);
        // 判斷是否在搜尋,如果在搜尋，就取消搜尋
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        createConnect(blueToothAddress,false);
            Log.d("outputStream","success~~~");
            String message = getMyBuleTooth()+",ask";
            try {
                outputStream.write(message.getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public void matchedSuccessReturn(String blueToothAddress){
        String message = getMyBuleTooth()+",accept";

        try {
            createConnect(blueToothAddress,true);
            Log.e("successreturn",message);
            outputStream.write(message.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createConnect(String blueToothAddress,boolean connect){
        selectDevice = mBluetoothAdapter.getRemoteDevice(blueToothAddress);
        Log.d("selectDevice",String.valueOf(selectDevice));
        try{
//            if(clientSocket == null){
            try {
                clientSocket = selectDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (Exception e) {Log.e("error","Error creating socket");}

            try {
                clientSocket.connect();
                outputStream = clientSocket.getOutputStream();
                Log.e("","Connected");
            } catch (IOException e) {
                Log.e("error",e.getMessage());
                try {
                    Log.e("","trying fallback...");

                    clientSocket =(BluetoothSocket) selectDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(selectDevice,2);
                    clientSocket.connect();
                    outputStream = clientSocket.getOutputStream();
                    Log.e("",String.valueOf(selectDevice));
                }
                catch (Exception e2) {
                    Log.e("", "Couldn't establish Bluetooth connection!");
                }
            }


            Log.d("outputStream",String.valueOf(outputStream));

            if(!connect) {
                Toast.makeText(activity, "已發送配對請求，請等待對方同意", Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"配對失敗，請稍後再試",Toast.LENGTH_LONG).show();
        }
    }
    public void startThread(Handler handler){
        acceptThread = new AcceptThreadHelper(mBluetoothAdapter,MY_UUID,activity,handler);
        acceptThread.start();
    }
}
