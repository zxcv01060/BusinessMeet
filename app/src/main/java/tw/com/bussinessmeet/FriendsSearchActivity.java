package tw.com.bussinessmeet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import tw.com.bussinessmeet.bean.UserInformationBean;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import tw.com.bussinessmeet.helper.AvatarHelper;
import tw.com.bussinessmeet.helper.DBHelper;

public class FriendsSearchActivity extends AppCompatActivity {
    private TextView searchbar;
    private ImageButton back;
    private ImageButton filter;
    private tw.com.bussinessmeet.dao.UserInformationDAO userInformationDAO;
    private DBHelper DH = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_search);
        openDB();
        back = (ImageButton) findViewById(R.id.friendsFilter_toolbar_backIcon);
        back.setOnClickListener(backFriendsClick);
        filter = (ImageButton) findViewById(R.id.friendsFilter_toolbar_filterIcon);
        filter.setOnClickListener(friendsFilterClick);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_friends);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        Menu BVMenu = bottomNavigationView.getMenu();
        AvatarHelper avatarHelper = new AvatarHelper();
        UserInformationBean ufb = new UserInformationBean();
        Cursor result = userInformationDAO.searchAll(ufb);

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));
    }

    private void openDB() {
        Log.d("add", "openDB");
        DH = new DBHelper(this);
        userInformationDAO = new tw.com.bussinessmeet.dao.UserInformationDAO(DH);
        //matchedDAO = new tw.com.bussinessmeet.dao.MatchedDAO(DH);
    }

    public View.OnClickListener backFriendsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToFriendsActivityPage();
        }
    };

    public void changeToFriendsActivityPage() {
        Intent intent = new Intent();
        intent.setClass(FriendsSearchActivity.this, FriendsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("blueToothAddress", getIntent().getStringExtra("blueToothAddress"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public View.OnClickListener friendsFilterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToFriendsFilterActivityPage();
        }
    };

    public void changeToFriendsFilterActivityPage() {
        Intent intent = new Intent();
        intent.setClass(FriendsSearchActivity.this, FriendsFilterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("blueToothAddress", getIntent().getStringExtra("blueToothAddress"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navListener =
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
                            return true;
                    }
                    return false;
                }
            });

}
