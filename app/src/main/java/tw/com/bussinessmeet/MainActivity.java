package tw.com.bussinessmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Set;

import tw.com.bussinessmeet.Bean.UserInformationBean;
import tw.com.bussinessmeet.DAO.UserInformationDAO;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

//https://codertw.com/android-%E9%96%8B%E7%99%BC/332688/
public class MainActivity extends AppCompatActivity /*implements ThematicListAdapter.DevicesClickListener*/ {

    private  String TAG = "MainActivity";
    //    private RecyclerView recyclerViewThrmatic;
    private TextView  userName,company,position,email,tel;
    private TextView matched;

    private Button confirm;
    private BlueToothHelper blueTooth;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH = null;
//    private List<DeviceItem> deviceItems ;
//    private ThematicListAdapter thematicListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_introduction);
//        recyclerViewThrmatic = (RecyclerView) findViewById(R.id.recycleViewThematic);
//        deviceItems = new ArrayList<>();
        Log.d("MainActivity","success");
//        tvDevices = (TextView) findViewById(R.id.tvDecives);
////        matched = (TextView) findViewById(R.id.matched);
//
////

        confirm = (Button)findViewById(R.id.confirm_introduction);
        position = (TextView)findViewById(R.id.add_profile_position);
        company = (TextView)findViewById(R.id.add_profile_company);
        userName = (TextView) findViewById(R.id.add_profile_name);
        tel = (TextView) findViewById(R.id.add_profile_tel);
        email = (TextView) findViewById(R.id.add_profile_email);
        openDB();
        confirm.setOnClickListener(confirmClick);
        //啟動藍芽
//        blueTooth = new BlueToothHelper(this);
//        blueTooth.startBuleTooth();


    }
    private void openDB(){
        Log.d("add","openDB");
        DH = new DBHelper(this);


    }
    public View.OnClickListener confirmClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            UserInformationBean ufb = new UserInformationBean();
            ufb.setBlueTooth("1");
            ufb.setCompany(company.getText().toString());
            ufb.setPosition(position.getText().toString());
            ufb.setUserName(userName.getText().toString());
            ufb.setEmail(email.getText().toString());
            ufb.setTel(tel.getText().toString());
            ufb.setAvatar("1");
            Log.d("add",DH.toString());
            Log.d("add",ufb.getCompany());

            userInformationDAO = new UserInformationDAO(DH);
            userInformationDAO.add(ufb);

        }
    };



//    private void createRecyclerViewWeather() {
////        recyclerViewThrmatic.setLayoutManager(new LinearLayoutManager(this));
//
//        thematicListAdapter = new ThematicListAdapter(this,this.deviceItems);
//
//        thematicListAdapter.setClickListener(this);
////        recyclerViewThrmatic.setAdapter(thematicListAdapter);
//
//    }
//    @Override
//    protected void onDestroy() {
////        unregisterReceiver(receiver); super.onDestroy();
//    }
//    @Override
//    public void onDevicesClick(View view, int position) {
//
//    }
}
