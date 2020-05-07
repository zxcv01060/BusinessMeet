package tw.com.bussinessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import retrofit2.Call;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AsyncTasKHelper;
import tw.com.bussinessmeet.helper.AvatarHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;
import tw.com.bussinessmeet.service.Impl.UserInformationServiceImpl;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class FriendsIntroductionActivity extends AppCompatActivity {
    private TextView userName,company,position,email,tel;
    private Button editButton;
    private ImageView avatar;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;
    private AvatarHelper avatarHelper = new AvatarHelper();
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private AsyncTasKHelper.OnResponseListener<UserInformationBean, List<UserInformationBean>> searchResponseListener =
            new AsyncTasKHelper.OnResponseListener<UserInformationBean, List<UserInformationBean>>() {
                @Override
                public Call<ResponseBody<List<UserInformationBean>>> request(UserInformationBean... userInformationBeans) {

                    return userInformationService.search(userInformationBeans[0]);
                }

                @Override
                public void onSuccess(List<UserInformationBean> userInformationBeanList) {
                    Log.d("nameeee", userInformationBeanList.get(0).getUserName());
                    UserInformationBean userInformationBean = userInformationBeanList.get(0);
/*                    String userName = userInformationBean.getUserName();
                    String company = userInformationBean.getCompany();
                    String position = userInformationBean.getPosition();
                    String email = userInformationBean.getEmail();
                    String tel  = userInformationBean.getTel();*/

                }

                @Override
                public void onFail(int status) {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_introduction);


        userName = (TextView) findViewById(R.id.friends_name);
        company = (TextView) findViewById(R.id.friends_company);
        position = (TextView) findViewById(R.id.friends_position);
        email = (TextView) findViewById(R.id.friends_email);
        tel = (TextView) findViewById(R.id.friends_tel);
        avatar = (ImageView) findViewById(R.id.friends_photo);
        avatarHelper = new AvatarHelper();



        //searchUserInformation();

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_friends);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        String blueToothAddress = getIntent().getStringExtra("blueToothAddress");
//        Log.e("blueToothAddress",blueToothAddress);
        //notification
        TextView friendName = findViewById(R.id.friends_name);
        String title = getIntent().getStringExtra("title");
        friendName.setText(title);
        TextView friendCompany = findViewById(R.id.friends_company);
        String company = getIntent().getStringExtra("company");
        friendCompany.setText(company);
        TextView friendPosition = findViewById(R.id.friends_position);
        String position = getIntent().getStringExtra("position");
        friendPosition.setText(position);
        TextView friendEmail = findViewById(R.id.friends_email);
        String email = getIntent().getStringExtra("email");
        friendEmail.setText(email);
        TextView friendTel = findViewById(R.id.friends_tel);
        String tel = getIntent().getStringExtra("tel");
        friendTel.setText(tel);


        if(getIntent().hasExtra("avatar")) {
            ImageView photo = findViewById(R.id.friends_photo);
            Bitmap profilePhoto = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("avatar"), 0, getIntent().getByteArrayExtra("avatar").length);
            photo.setImageBitmap(profilePhoto);
        }
    }

/*    public void searchUserInformation(){
        UserInformationBean ufb = new UserInformationBean();
        BlueToothHelper blueToothHelper = new BlueToothHelper(this);
        blueToothHelper.startBuleTooth();
        ufb.setBlueTooth(blueToothHelper.getMyBuleTooth());


        Cursor result = userInformationDAO.searchAll(ufb);
        Log.d("result",String.valueOf(result.getColumnCount()));
        Log.d("result",String.valueOf(result.getColumnIndex("user_name")));

        for(int i = 0; i<result.getColumnCount(); i++){
            Log.d("result",result.getColumnName(i));
        }


        if (result.moveToFirst()) {
            userName.append(result.getString(result.getColumnIndex("user_name")));
            company.append(result.getString(result.getColumnIndex("company")));
            position.append(result.getString(result.getColumnIndex("position")));
            email.append(result.getString(result.getColumnIndex("email")));
            tel.append(result.getString(result.getColumnIndex("tel")));
            avatar.setImageBitmap(avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))));

        }
        result.close();

    }*/

    //Perform ItemSelectedListener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            (new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.menu_home:
                            startActivity(new Intent(getApplicationContext()
                                    ,SelfIntroductionActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.menu_search:
                            startActivity(new Intent(getApplicationContext()
                                    ,SearchActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.menu_friends:
                            menuItem.setIcon(R.drawable.ic_people_blue_24dp);
                            return true;
                    }
                    return false;
                }
            });
}
