package tw.com.bussinessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AvatarHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;

public class SelfIntroductionActivity extends AppCompatActivity {
    private TextView userName,company,position,email,tel;
    private Button editButton;
    private ImageView avatar;
    private UserInformationDAO userInformationDAO;
    private  DBHelper DH;
    private AvatarHelper avatarHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_introduction);
        userName = (TextView) findViewById(R.id.profile_name);
        company = (TextView) findViewById(R.id.profile_company);
        position = (TextView) findViewById(R.id.profile_position);
        email = (TextView) findViewById(R.id.profile_email);
        tel = (TextView) findViewById(R.id.profile_tel);
        avatar = (ImageView) findViewById(R.id.edit_person_photo);
        editButton = (Button) findViewById(R.id.editPersonalProfileButton);
        editButton.setOnClickListener(editButtonClick);
        avatarHelper = new AvatarHelper();
        openDB();
        searchUserInformation();

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

    }
    private void openDB(){
        Log.d("add","openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    public void searchUserInformation(){
        UserInformationBean ufb = new UserInformationBean();
        BlueToothHelper blueToothHelper = new BlueToothHelper(this);
        blueToothHelper.startBuleTooth();
        ufb.setBlueTooth(blueToothHelper.getMyBuleTooth());


        Cursor result = userInformationDAO.searchAll(ufb);
        Log.d("result",String.valueOf(result.getColumnCount()));
        Log.d("result",String.valueOf(result.getColumnIndex("user_name")));


        if (result.moveToFirst()) {
            userName.append(result.getString(result.getColumnIndex("user_name")));
            company.append(result.getString(result.getColumnIndex("company")));
            position.append(result.getString(result.getColumnIndex("position")));
            email.append(result.getString(result.getColumnIndex("email")));
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
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
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
