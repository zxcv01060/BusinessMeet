package tw.com.bussinessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import androidx.core.content.ContextCompat;
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
    private BottomNavigationView menu ;
    private BlueToothHelper blueToothHelper;
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
        menu = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //this.personal = personal;
        blueToothHelper = new BlueToothHelper(this);
        avatarHelper = new AvatarHelper();

        openDB();
        searchUserInformation();

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //searchUserInformation();
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
//        avatar.setImageBitmap(avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))));

        //Menu profilephoto =(Menu) findViewById(R.id.menu_home);
        //bottomNavigationView.getMenu().getItem().setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        //get menu
        Menu BVMenu = bottomNavigationView.getMenu();
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        //personal.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_search_blod));

        //////////////////////////////
        //set menu
        Menu menu = bottomNavigationView.getMenu();
        //MenuItem profilephoto = menu.findItem(R.id.menu_home);
        //searchUserInformation();
        AvatarHelper avatarHelper = new AvatarHelper();
        blueToothHelper.startBuleTooth();
        Log.d("seedmess","ness");
        UserInformationBean ufb = new UserInformationBean();
        ufb.setBlueTooth(blueToothHelper.getMyBuleTooth());
        Cursor result = userInformationDAO.searchAll(ufb);
        Log.e("result",String.valueOf(result));

/*            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_navigation, menu);
            MenuItem userItem = menu.findItem(R.id.menu_home);
            Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
                    userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));*/

            //return menu;



        ////////////////////////////////
        //avatar.setImageBitmap(avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))))
        //profilephoto.setIcon(R.drawable.ic_search_blod);

/*        public boolean onPrepareOptionsMenu(Menu menu) {
            MenuItem switchButton = menu.findItem(R.id.menu_home);
            return super.onPrepareOptionsMenu(menu);

        }*/



        // ImageView avatar = (ImageView) findViewById(R.id.menu_home);
        /*
        JLabel label=new JLabel();
        ImageIcon icon=new ImageIcon("1.png");
        label.setIcon(icon);
        */


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
