package tw.com.bussinessmeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import tw.com.bussinessmeet.Bean.UserInformationBean;
import tw.com.bussinessmeet.DAO.UserInformationDAO;
import tw.com.bussinessmeet.helper.DBHelper;

public class EditIntroductionActivity extends AppCompatActivity {
    private TextView userName,company,position,email,tel;
    private ImageButton editProfileConfirm;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_introduction);
        userName = (TextView) findViewById(R.id.profile_name);
        company = (TextView) findViewById(R.id.profile_company);
        position = (TextView) findViewById(R.id.profile_position);
        email = (TextView) findViewById(R.id.profile_email);
        tel = (TextView) findViewById(R.id.profile_tel);
        editProfileConfirm = (ImageButton) findViewById(R.id.editProfileConfirmButtom);
        editProfileConfirm.setOnClickListener(confirmButton);
        userName.append("\n");
        company.append("\n");
        position.append("\n");
        email.append("\n");
        tel.append("\n");
        openDB();
        searchUserInformation();
    }

    public void searchUserInformation(){
        UserInformationBean ufb = new UserInformationBean();
        Cursor result = userInformationDAO.searchAll(ufb);
        Log.d("result",String.valueOf(result.getColumnCount()));
        Log.d("result",String.valueOf(result.getColumnIndex("user_name")));

        if (result.moveToFirst()) {
            userName.append(result.getString(result.getColumnIndex("user_name")));
            company.append(result.getString(result.getColumnIndex("company")));
            position.append(result.getString(result.getColumnIndex("position")));
            email.append(result.getString(result.getColumnIndex("email")));
            tel.append(result.getString(result.getColumnIndex("tel")));
        }
        result.close();
    }


    private void openDB(){
        Log.d("add","openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    public View.OnClickListener confirmButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserInformationBean ufb = new UserInformationBean();
            ufb.setBlueTooth("1");
            ufb.setUserName(userName.toString());
            ufb.setCompany(company.toString());
            ufb.setPosition(position.toString());
            ufb.setEmail(email.toString());
            ufb.setTel(tel.toString());
            userInformationDAO.update(ufb);
            changeToSelfIntroductionPage(SelfIntroductionActivity.class);
        }
    };

    public void changeToSelfIntroductionPage(Class classname){
        Intent intent = new Intent();
        intent.setClass(EditIntroductionActivity.this, classname);
        startActivity(intent);
    }
}
