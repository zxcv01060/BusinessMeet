package tw.com.bussinessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import tw.com.bussinessmeet.background.NotificationService;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AvatarHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;

public class SelfIntroductionActivity extends AppCompatActivity {
    private TextView userName,profession,gender,email,tel;
    private Button editButton;
    private ImageView avatar;
    private UserInformationDAO userInformationDAO;
    private  DBHelper DH;
    private AvatarHelper avatarHelper;
    private BottomNavigationView menu ;
    private BlueToothHelper blueToothHelper;
    private NotificationService notificationService = null;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_introduction);
        userName = (TextView) findViewById(R.id.profile_name);
        profession = (TextView) findViewById(R.id.profile_profession);
        gender = (TextView) findViewById(R.id.profile_gender);
        email = (TextView) findViewById(R.id.profile_mail);
        tel = (TextView) findViewById(R.id.profile_tel);
        avatar = (ImageView) findViewById(R.id.edit_person_photo);
        editButton = (Button) findViewById(R.id.editPersonalProfileButton);
        editButton.setOnClickListener(editButtonClick);
        menu = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //this.personal = personal;
        blueToothHelper = new BlueToothHelper(this);
        avatarHelper = new AvatarHelper();
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbarMenu
        toolbar.inflateMenu(R.menu.toolbarmenu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }

        });


        openDB();
        searchUserInformation();

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //searchUserInformation();
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        Menu BVMenu = bottomNavigationView.getMenu();
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        AvatarHelper avatarHelper = new AvatarHelper();
        blueToothHelper.startBuleTooth();
        Log.d("seedmess","ness");
        Cursor result = userInformationDAO.getById(blueToothHelper.getUserId());
        Log.e("result",String.valueOf(result));

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));
        startBackgroundService();
    }
//    private ServiceConnection notificationServiceConnect = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            notificationService = ((NotificationService.LocalBinder)service).getService();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };
    private void startBackgroundService(){

        Intent it = new Intent(SelfIntroductionActivity.this, NotificationService.class);
        stopService(it);
        startService(it);
    }
    private void openDB(){
        Log.d("add","openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    public void searchUserInformation(){

        Cursor result = userInformationDAO.getById(blueToothHelper.getUserId());
        Log.d("result",String.valueOf(result.getColumnCount()));

        for(int i = 0; i<result.getColumnCount(); i++){
            Log.d("result",result.getColumnName(i));
        }


        if (result.moveToFirst()) {
            userName.append(result.getString(result.getColumnIndex("name")));
            gender.append(result.getString(result.getColumnIndex("gender")));
            profession.append(result.getString(result.getColumnIndex("profession")));
            email.append(result.getString(result.getColumnIndex("mail")));
            tel.append(result.getString(result.getColumnIndex("tel")));
            avatar.setImageBitmap(avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))));

        }
        result.close();



    }

    public View.OnClickListener editButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToEditIntroductionPage();
        }
    };
    public void changeToEditIntroductionPage(){
        Intent intent = new Intent();
        intent.setClass(SelfIntroductionActivity.this,EditIntroductionActivity.class);
        startActivity(intent);
    }



    //Perform ItemSelectedListener
    BottomNavigationView.OnNavigationItemSelectedListener navListener =
            (new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.menu_home:
                    return true;
                case R.id.menu_search:
                    startActivity(new Intent(getApplicationContext()
                            ,SearchActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.menu_friends:
                    //menuItem.setIcon(R.drawable.ic_people_blue_24dp);
                    startActivity(new Intent(getApplicationContext()
                            ,FriendsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        }


    });


}
