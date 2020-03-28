package tw.com.bussinessmeet;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import tw.com.bussinessmeet.api.UserInformationApiImpl;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AsyncTasKHelper;
import tw.com.bussinessmeet.helper.AvatarHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;

public class AddIntroductionActivity extends AppCompatActivity {
    private  String TAG = "MainActivity";
    //    private RecyclerView recyclerViewThrmatic;

    private TextView userName,company,position,email,tel;
    private ImageView avatar;
    private Button confirm;
    private BlueToothHelper blueTooth;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH = null;
    private AvatarHelper avatarHelper ;
    private UserInformationApiImpl userInformationApi;
//    private List<DeviceItem> deviceItems ;
//    private ThematicListAdapter thematicListAdapter;
    private AsyncTasKHelper.OnResponseListener<UserInformationBean, UserInformationBean> addResponseListener =
            new AsyncTasKHelper.OnResponseListener<UserInformationBean, UserInformationBean>() {
                @Override
                public Call<ResponseBody<UserInformationBean>> request(UserInformationBean... userInformationBeans) {
                    return userInformationApi.add(userInformationBeans[0]);
                }

                @Override
                public void onSuccess(UserInformationBean userInformationBean) {

                }

                @Override
                public void onFail(int status) {

                }
            };



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
        avatar = (ImageView) findViewById(R.id.add_photo_button);
        avatarHelper = new AvatarHelper();
        openDB();
//        //啟動藍芽
        blueTooth = new BlueToothHelper(this);
        blueTooth.startBuleTooth();
        String myBlueTooth = blueTooth.getMyBuleTooth();
//        Log.d("resultmyBlueTooth",myBlueTooth);
//        String myBlueTooth = "1";
        String result = userInformationDAO.getById(myBlueTooth);
        Log.d("result","getBlueTooth"+result);
        if( result !=null && !result.equals("") ) {
            changeToAnotherPage(SearchActivity.class);
        }
        confirm.setOnClickListener(confirmClick);
        avatar.setOnClickListener(choseAvatar);


    }
    private Button.OnClickListener choseAvatar = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if(intent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(intent, RequestCode.REQUEST_IMAGE_CAPTURE);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            Log.d("resultUri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try{
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                avatar.setImageBitmap(avatarHelper.toCircle(bitmap));
            }catch(Exception e){
                Log.d("Exception",e.getMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
//            ufb.setBlueTooth("1");
            ufb.setBlueTooth(blueTooth.getMyBuleTooth());
            ufb.setCompany(company.getText().toString());
            ufb.setPosition(position.getText().toString());
            ufb.setUserName(userName.getText().toString());
            ufb.setEmail(email.getText().toString());
            ufb.setTel(tel.getText().toString());
            ufb.setAvatar(avatarHelper.setImageResource(avatar));
            Log.d("add",ufb.getCompany());

            if(checkData(ufb)) {
                userInformationDAO.add(ufb);
                AsyncTasKHelper.execute(addResponseListener, ufb);
                changeToAnotherPage(SearchActivity.class);
            }
        }
    };

    public void changeToAnotherPage(Class classname){
        Intent intent = new Intent();
        intent.setClass(AddIntroductionActivity.this,classname);
        startActivity(intent);
    }
    private boolean checkData(UserInformationBean userInformationBean){
        String positionStr = userInformationBean.getPosition();
        String companyStr = userInformationBean.getCompany();
        String emailStr = userInformationBean.getEmail();
        String telStr = userInformationBean.getTel();
        String userNameStr = userInformationBean.getUserName();
        String avatarStr = userInformationBean.getAvatar();
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
        }else if(avatarStr == null || avatarStr.equals("")) {
            Toast.makeText(this,"請上傳圖片",Toast.LENGTH_LONG).show();
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
