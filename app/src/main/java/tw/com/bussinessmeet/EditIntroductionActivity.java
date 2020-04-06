package tw.com.bussinessmeet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import tw.com.bussinessmeet.service.Impl.UserInformationServiceImpl;

import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AsyncTasKHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;
import tw.com.bussinessmeet.helper.AvatarHelper;

public class EditIntroductionActivity extends AppCompatActivity {

    private TextView userName, company, position, email, tel;
    private String name, com, pos, mail, phone;
    private ImageView avatar;
    private ImageButton editConfirmButtom;
    private AvatarHelper avatarHelper;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private BlueToothHelper blueToothHelper;
    private DBHelper DH;
    private UserInformationDAO userInformationDAO;
    private UserInformationServiceImpl userInformationApi;
    private AsyncTasKHelper.OnResponseListener<UserInformationBean, Empty> updateResponseListener =
            new AsyncTasKHelper.OnResponseListener<UserInformationBean, Empty>() {
                @Override
                public Call<ResponseBody<Empty>> request(UserInformationBean... userInformationBeans) {
                    return userInformationApi.update(userInformationBeans[0]);
                }

                @Override
                public void onSuccess(Empty Empty) {

                }

                @Override
                public void onFail(int status) {

                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_introduction);
        editConfirmButtom = (ImageButton) findViewById(R.id.editProfileConfirmButtom);
        editConfirmButtom.setOnClickListener(editConfirmClick);
        avatar = (ImageView) findViewById(R.id.profilePhoto);
        avatar.setOnClickListener(choseAvatar);
        userName = (TextView) findViewById(R.id.profileName);
        company = (TextView) findViewById(R.id.profileCompany);
        position = (TextView) findViewById(R.id.profilePosition);
        tel = (TextView) findViewById(R.id.profileTel);
        email = (TextView) findViewById(R.id.profileEmail);
        avatarHelper = new AvatarHelper();

        openDB();
        searchUserInformation();
        userName.append(name);
        company.append(com);
        position.append(pos);
        email.append(mail);
        tel.append(phone);
    }

    private void openDB() {
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
    }

    public void searchUserInformation() {
        SQLiteDatabase db = DH.getWritableDatabase();
        UserInformationBean ufb = new UserInformationBean();
        blueToothHelper = new BlueToothHelper(this);
        blueToothHelper.startBuleTooth();
        ufb.setBlueTooth(blueToothHelper.getMyBuleTooth());
        Cursor cursor = userInformationDAO.searchAll(ufb);

        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex("user_name"));
                Log.d("edit", name);
                com = cursor.getString(cursor.getColumnIndex("company"));
                pos = cursor.getString(cursor.getColumnIndex("position"));
                mail = cursor.getString(cursor.getColumnIndex("email"));
                phone = cursor.getString(cursor.getColumnIndex("tel"));
                avatar.setImageBitmap(avatarHelper.getImageResource(cursor.getString(cursor.getColumnIndex("avatar"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public View.OnClickListener editConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserInformationBean ufb = new UserInformationBean();
            ufb.setBlueTooth(blueToothHelper.getMyBuleTooth());
            ufb.setUserName(userName.getText().toString());
            ufb.setCompany(company.getText().toString());
            ufb.setPosition(position.getText().toString());
            ufb.setEmail(email.getText().toString());
            ufb.setTel(tel.getText().toString());
            ufb.setAvatar(avatarHelper.setImageResource(avatar));
            userInformationDAO.update(ufb);
            AsyncTasKHelper.execute(updateResponseListener,ufb);
            changeToSelfIntroductionPage();
        }
    };

    public void changeToSelfIntroductionPage() {
        Intent intent = new Intent();
        intent.setClass(EditIntroductionActivity.this, SelfIntroductionActivity.class);
        startActivity(intent);
    }

    private Button.OnClickListener choseAvatar = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if(intent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            Log.d("resultUri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try{
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                avatar.setImageBitmap(avatarHelper.toCircle(bitmap));
            }catch(Exception e){
                Log.d("Exception",e.getMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}