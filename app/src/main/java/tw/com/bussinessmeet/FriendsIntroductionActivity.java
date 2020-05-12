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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import tw.com.bussinessmeet.bean.MatchedBean;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tw.com.bussinessmeet.helper.DBHelper;
import tw.com.bussinessmeet.service.UserInformationService;

import java.util.List;

public class FriendsIntroductionActivity extends AppCompatActivity {
    private TextView userName, company, position, email, tel, memo;
    private Button editButton;
    private ImageView avatar;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;
    private AvatarHelper avatarHelper = new AvatarHelper();
    private BlueToothHelper blueToothHelper;
    private MatchedDAO matchedDAO;
    private MatchedBean matchedBean = new MatchedBean();
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private MatchedServiceImpl matchedService = new MatchedServiceImpl();
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

    private AsyncTasKHelper.OnResponseListener<MatchedBean, List<MatchedBean>> friendsMemoResponseListener = new AsyncTasKHelper.OnResponseListener<MatchedBean, List<MatchedBean>>() {
        @Override
        public Call<ResponseBody<List<MatchedBean>>> request(MatchedBean... matchedBeans) {
            return matchedService.search(matchedBeans[0]);
        }

        @Override
        public void onSuccess(List<MatchedBean> matchedBeanList) {
            Log.d("memo", matchedBeanList.get(0).getMemorandum());
            memo.append(matchedBeanList.get(0).getMemorandum());
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
        matchedBean.setMatchedBlueTooth(blueToothAddress);
        blueToothHelper = new BlueToothHelper(this);
        matchedBean.setBlueTooth(blueToothHelper.getMyBuleTooth());
        Log.d("memo", matchedBean.getMatchedBlueTooth());
        Log.d("memo", matchedBean.getBlueTooth());
        AsyncTasKHelper.execute(friendsMemoResponseListener, matchedBean);

        userName = (TextView) findViewById(R.id.friends_name);
        company = (TextView) findViewById(R.id.friends_company);
        position = (TextView) findViewById(R.id.friends_position);
        email = (TextView) findViewById(R.id.friends_email);
        tel = (TextView) findViewById(R.id.friends_tel);
        avatar = (ImageView) findViewById(R.id.friends_photo);
        memo = (TextView) findViewById(R.id.friends_memo);
        avatarHelper = new AvatarHelper();
        editButton = (Button) findViewById(R.id.editFriendsProfileButton);
        editButton.setOnClickListener(editMemoButton);



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

    public View.OnClickListener editMemoButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToFriendsEditIntroductionPage();
        }
    };

    public void changeToFriendsEditIntroductionPage() {
        Intent intent = new Intent();
        intent.setClass(FriendsIntroductionActivity.this, FriendsMemoActivity.class);
        startActivity(intent);
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
                            startActivity(new Intent(getApplicationContext()
                                    ,FriendsActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            });
}
