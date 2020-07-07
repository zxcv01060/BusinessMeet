package tw.com.bussinessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.FriendBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.FriendDAO;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AsyncTasKHelper;
import tw.com.bussinessmeet.helper.AvatarHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;
import tw.com.bussinessmeet.service.Impl.MatchedServiceImpl;
import tw.com.bussinessmeet.service.Impl.UserInformationServiceImpl;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class FriendsIntroductionActivity extends AppCompatActivity {
    private TextView userName, profession, gender, email, tel, remark;
    private Button editButton;
    private ImageView avatar;
    private String friendId;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;
    private AvatarHelper avatarHelper = new AvatarHelper();
    private BlueToothHelper blueToothHelper;
    private FriendDAO friendDAO;
    private FriendBean friendBean = new FriendBean();
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private MatchedServiceImpl matchedService = new MatchedServiceImpl();
    private AsyncTasKHelper.OnResponseListener<String, UserInformationBean> userInfoResponseListener = new AsyncTasKHelper.OnResponseListener<String, UserInformationBean>() {
        @Override
        public Call<ResponseBody<UserInformationBean>> request(String... userId) {
            return userInformationService.getById(userId[0]);
        }

        @Override
        public void onSuccess(UserInformationBean userInformationBean) {
            if(userInformationBean ==null){
                Cursor cursor = userInformationDAO.getById(friendId);
                userInformationBean.setName(cursor.getString(cursor.getColumnIndex("name")));
                userInformationBean.setProfession(cursor.getString(cursor.getColumnIndex("profession")));
                userInformationBean.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                userInformationBean.setMail(cursor.getString(cursor.getColumnIndex("mail")));
                userInformationBean.setTel(cursor.getString(cursor.getColumnIndex("tel")));
                userInformationBean.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
            }
            userName.append(userInformationBean.getName());
            profession.append(userInformationBean.getProfession());
            gender.append(userInformationBean.getGender());
            email.append(userInformationBean.getMail());
            tel.append(userInformationBean.getTel());
            avatar.setImageBitmap(avatarHelper.getImageResource(userInformationBean.getAvatar()));
        }

        @Override
        public void onFail(int status) {
        }
    };

    private AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>> friendsMemoResponseListener = new AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>>() {
        @Override
        public Call<ResponseBody<List<FriendBean>>> request(FriendBean... friendBeans) {
            return matchedService.search(friendBeans[0]);
        }

        @Override
        public void onSuccess(List<FriendBean> friendBeanList) {
           System.out.println(friendBeanList.get(0).getRemark()+"=============================");
           System.out.println(friendBeanList.size()+"=============================");
           if(friendBeanList.get(0).getRemark()!=null)
               remark.append(friendBeanList.get(0).getRemark());
        }

        @Override
        public void onFail(int status) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_introduction);
         friendId = getIntent().getStringExtra("friendId");
        Log.e("friendId: ",friendId );
        AsyncTasKHelper.execute(userInfoResponseListener, friendId);
        friendBean.setFriendId(friendId);
        blueToothHelper = new BlueToothHelper(this);
        friendBean.setMatchmakerId(blueToothHelper.getUserId());
        AsyncTasKHelper.execute(friendsMemoResponseListener, friendBean);

        userName = (TextView) findViewById(R.id.friends_name);
        profession = (TextView) findViewById(R.id.friends_profession);
        gender = (TextView) findViewById(R.id.friends_gender);
        email = (TextView) findViewById(R.id.friends_mail);
        tel = (TextView) findViewById(R.id.friends_tel);
        avatar = (ImageView) findViewById(R.id.friends_photo);
        remark = (TextView) findViewById(R.id.friends_memo);
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
        friendDAO = new FriendDAO(DH);

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
        Bundle bundle = new Bundle();
        bundle.putString("friendId",getIntent().getStringExtra("friendId"));
        intent.putExtras(bundle);
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
