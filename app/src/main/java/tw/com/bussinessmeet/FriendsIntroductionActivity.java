package tw.com.bussinessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.MatchedDAO;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AsyncTasKHelper;
import tw.com.bussinessmeet.helper.AvatarHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;
import tw.com.bussinessmeet.service.Impl.MatchedServiceImpl;
import tw.com.bussinessmeet.service.Impl.UserInformationServiceImpl;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import tw.com.bussinessmeet.bean.MatchedBean;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tw.com.bussinessmeet.helper.DBHelper;
import tw.com.bussinessmeet.service.UserInformationService;

import java.util.List;

public class FriendsIntroductionActivity extends AppCompatActivity {
    private TextView userName, company, position, email, tel;
    private Button editButton;
    private ImageView avatar;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;
    private AvatarHelper avatarHelper = new AvatarHelper();
    private MatchedDAO matchedDAO;
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private AsyncTasKHelper.OnResponseListener<String, UserInformationBean> userInfoResponseListener = new AsyncTasKHelper.OnResponseListener<String, UserInformationBean>() {
        @Override
        public Call<ResponseBody<UserInformationBean>> request(String... bluetooth) {
            return userInformationService.getById(bluetooth[0]);
        }

        @Override
        public void onSuccess(UserInformationBean userInformationBean) {
            userName.append(userInformationBean.getUserName());
            company.append(userInformationBean.getCompany());
            position.append(userInformationBean.getPosition());
            email.append(userInformationBean.getEmail());
            tel.append(userInformationBean.getTel());
            avatar.setImageBitmap(avatarHelper.getImageResource(userInformationBean.getAvatar()));
        }

        @Override
        public void onFail(int status) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_introduction);
        String blueToothAddress = getIntent().getStringExtra("blueToothAddress");
        AsyncTasKHelper.execute(userInfoResponseListener, blueToothAddress);

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
        openDB();
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        Menu BVMenu = bottomNavigationView.getMenu();
        AvatarHelper avatarHelper = new AvatarHelper();
        UserInformationBean ufb = new UserInformationBean();
        Cursor result = userInformationDAO.searchAll(ufb);

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));


        String bluetooth = getIntent().getStringExtra("blueToothAddress");
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


        if (getIntent().hasExtra("avatar")) {
            ImageView photo = findViewById(R.id.friends_photo);
            Bitmap profilePhoto = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("avatar"), 0, getIntent().getByteArrayExtra("avatar").length);
            photo.setImageBitmap(profilePhoto);
        }
    }

    private void openDB() {
        Log.d("add", "openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
        matchedDAO = new MatchedDAO(DH);

    }

    //Perform ItemSelectedListener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            (new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_home:
                            startActivity(new Intent(getApplicationContext()
                                    , SelfIntroductionActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.menu_search:
                            startActivity(new Intent(getApplicationContext()
                                    , SearchActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.menu_friends:
                            menuItem.setIcon(R.drawable.ic_people_blue_24dp);
                            return true;
                    }
                    return false;
                }
            });
}
