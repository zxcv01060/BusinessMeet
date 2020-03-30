package tw.com.bussinessmeet;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;

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

        avatar = (ImageView) findViewById(R.id.edit_person_photo);
        avatarHelper = new AvatarHelper();
        AvatarHelper avatarHelper = new AvatarHelper();
        UserInformationBean ufb = new UserInformationBean();
        ufb.setBlueTooth(getMyBuleTooth());
        Cursor result = userInformationDAO.searchAll(ufb);
    }
}