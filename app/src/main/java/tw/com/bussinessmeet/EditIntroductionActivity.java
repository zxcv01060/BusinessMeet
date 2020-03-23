package tw.com.bussinessmeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.zip.Inflater;

import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;

public class EditIntroductionActivity extends AppCompatActivity {

    private TextView userName, company, position, email, tel;
    private ImageButton confirmEdit;
    private DBHelper DH;
    private UserInformationDAO userInformationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_introduction);
        LayoutInflater inflater = getLayoutInflater();
        View editIntroduction = inflater.inflate(R.layout.edit_introduction, null);
        confirmEdit = (ImageButton) editIntroduction.findViewById(R.id.editProfileConfirmButtom);
        confirmEdit.setOnClickListener(confirmEditButtonClick);
        userName = (TextView) editIntroduction.findViewById(R.id.profileName);
        company = (TextView) editIntroduction.findViewById(R.id.profile_company);
        position = (TextView) editIntroduction.findViewById(R.id.profilePosition);
        tel = (TextView) editIntroduction.findViewById(R.id.profileTel);
        email = (TextView) editIntroduction.findViewById(R.id.profileEmail);

        openDB();
        searchUserInformation();
    }

    private void openDB() {
        DH = new DBHelper(this);
        UserInformationDAO userInformationDAO = new UserInformationDAO(DH);
    }

    public void searchUserInformation() {
        SQLiteDatabase db = DH.getWritableDatabase();
        Cursor cursor = db.query("User_Information", null, "null", null, "null", "null", "null");
        UserInformationBean ufb = new UserInformationBean();
        BlueToothHelper blueToothHelper = new BlueToothHelper(this);
        blueToothHelper.startBuleTooth();
        ufb.setBlueTooth(blueToothHelper.getMyBuleTooth());
        Log.d("edit", ufb.getBlueTooth());
        System.out.println(cursor.getCount());
        if (cursor.moveToFirst()) {
            Log.d("edit", "movetofirst");
            do {
                userName.append(cursor.getString(cursor.getColumnIndex("user_name")));
                company.append(cursor.getString(cursor.getColumnIndex("company")));
                position.append(cursor.getString(cursor.getColumnIndex("position")));
                tel.append(cursor.getString(cursor.getColumnIndex("tel")));
                email.append(cursor.getString(cursor.getColumnIndex("email")));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public View.OnClickListener confirmEditButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserInformationBean ufb = new UserInformationBean();
            ufb.setUserName(userName.toString());
            ufb.setCompany(company.toString());
            ufb.setPosition(position.toString());
            ufb.setEmail(email.toString());
            ufb.setTel(tel.toString());
            userInformationDAO.update(ufb);
            changeToSelfIntroductionPage(SelfIntroductionActivity.class);
        }
    };

    public void changeToSelfIntroductionPage(Class className) {
        Intent intent = new Intent();
        intent.setClass(EditIntroductionActivity.this, className);
        startActivity(intent);
    }
}
