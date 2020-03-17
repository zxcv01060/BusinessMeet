package tw.com.bussinessmeet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tw.com.bussinessmeet.Bean.UserInformationBean;
import tw.com.bussinessmeet.DAO.UserInformationDAO;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;

//https://codertw.com/android-%E9%96%8B%E7%99%BC/332688/
public class MainActivity extends AppCompatActivity /*implements ThematicListAdapter.DevicesClickListener*/ {

    private  String TAG = "MainActivity";
    //    private RecyclerView recyclerViewThrmatic;
    private TextView  userName,company,position,email,tel;
    private TextView matched;
//    private
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


//        //啟動藍芽
        blueTooth = new BlueToothHelper(this);
        blueTooth.startBuleTooth();
        String myBlueTooth = blueTooth.getMyBuleTooth();
        Log.d("resultmyBlueTooth",myBlueTooth);
//        String myBlueTooth = "1";
        String result = userInformationDAO.getById(myBlueTooth);
        Log.d("result","getBlueTooth"+result);
        if( result !=null && !result.equals("") ) {
            changeToAnotherPage(SearchActivity.class);
        }
        confirm.setOnClickListener(confirmClick);
    }
    private void openDB(){
        Log.d("add","openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }
    public View.OnClickListener confirmClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            UserInformationBean ufb = new UserInformationBean();
            ufb.setBlueTooth("1");
//            ufb.setBlueTooth(blueTooth.getMyBuleTooth());
            ufb.setCompany(company.getText().toString());
            ufb.setPosition(position.getText().toString());
            ufb.setUserName(userName.getText().toString());
            ufb.setEmail(email.getText().toString());
            ufb.setTel(tel.getText().toString());
            ufb.setAvatar("1");
            Log.d("add",DH.toString());
            Log.d("add",ufb.getCompany());

            if(checkData(ufb)) {
                userInformationDAO.add(ufb);
                changeToAnotherPage(SelfIntroductionActivity.class);
            }
        }
    };
    public void changeToAnotherPage(Class classname){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,classname);
        startActivity(intent);
    }
    private boolean checkData(UserInformationBean userInformationBean){
        String positionStr = userInformationBean.getPosition();
        String companyStr = userInformationBean.getCompany();
        String emailStr = userInformationBean.getEmail();
        String telStr = userInformationBean.getTel();
        String userNameStr = userInformationBean.getUserName();
        if(positionStr ==null || positionStr.equals("")){
            Toast.makeText(this,"請輸入職稱",Toast.LENGTH_LONG).show();
        }else if(companyStr == null || companyStr.equals("")){
            Toast.makeText(this,"請輸入公司",Toast.LENGTH_LONG).show();
        }else if(emailStr == null || emailStr.equals("")){
            Toast.makeText(this,"請輸入信箱",Toast.LENGTH_LONG).show();
        }else if(telStr == null || telStr.equals("")){
            Toast.makeText(this,"請輸入電話",Toast.LENGTH_LONG).show();
        }else if(userNameStr == null || userNameStr.equals("")){
            Toast.makeText(this,"請輸入姓名",Toast.LENGTH_LONG).show();
        }else{
            return true;
        }
        return false;
    }

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
