package tw.com.bussinessmeet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AvatarHelper;
import tw.com.bussinessmeet.helper.DBHelper;

public class MenuNavigationActivity extends AppCompatActivity {
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;
    private AvatarHelper avatarHelper;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.menu.menu_navigation);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

/*        AvatarHelper avatarHelper = new AvatarHelper();
        UserInformationBean ufb = new UserInformationBean();
        Cursor result = userInformationDAO.searchAll(ufb);*/

        //Bitmap profilePhoto = avatar.setImageBitmap(avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar"))));

/*
        Bitmap bitmap = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        bottomNavigationView.setItemBackground(drawable);
*/


        ImageView avatar = (ImageView) findViewById(R.id.menu_home);
        avatarHelper = new AvatarHelper();
  /*      AvatarHelper avatarHelper = new AvatarHelper();
        UserInformationBean ufb = new UserInformationBean();
        //ufb.setMatchmaker(getMyBuleTooth());
        Cursor result = userInformationDAO.searchAll(ufb);*/

        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

    }
}