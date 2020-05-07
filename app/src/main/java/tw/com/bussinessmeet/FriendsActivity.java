package tw.com.bussinessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AvatarHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;

public class FriendsActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_friends);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem switchButton = menu.findItem(R.id.menu_friends);
        boolean searchScriptDisplayed = false;
        if(searchScriptDisplayed){
            switchButton.setIcon(R.drawable.ic_people_blue_24dp);
        }else{
            switchButton.setIcon(R.drawable.ic_people_outline_blue_24dp);
        }
        return super.onPrepareOptionsMenu(menu);

    }
    //Perform ItemSelectedListener
    BottomNavigationView.OnNavigationItemSelectedListener navListener =
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

                    return true;
            }
            return false;
        }
    });




}
