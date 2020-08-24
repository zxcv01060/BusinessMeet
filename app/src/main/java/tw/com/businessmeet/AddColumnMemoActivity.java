package tw.com.businessmeet;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.dao.FriendCustomizationDAO;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.dao.UserInformationDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.FriendCustomizationService;
import tw.com.businessmeet.service.Impl.FriendCustomizationServiceImpl;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;

public class AddColumnMemoActivity extends AppCompatActivity {

    private Button confirm, cancel;
    private EditText addColumnMemo;

    private FriendCustomizationDAO friendCustomizationDAO;
    private FriendDAO friendDAO;
    private DBHelper dh = null;
    private FriendCustomizationServiceImpl friendCustomizationServiceImpl = new FriendCustomizationServiceImpl();
    private FriendServiceImpl friendServiceImpl = new FriendServiceImpl();
    private AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, FriendCustomizationBean> addResponseListener =
            new AsyncTasKHelper.OnResponseListener<FriendCustomizationBean, FriendCustomizationBean>() {
                @Override
                public Call<ResponseBody<FriendCustomizationBean>> request(FriendCustomizationBean... friendCustomizationBean) {
                    return friendCustomizationServiceImpl.add(friendCustomizationBean[0]);
                }

                @Override
                public void onSuccess(FriendCustomizationBean friendCustomizationBean) {
                }

                @Override
                public void onFail(int status) {
                }
            };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_column);

//        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddColumnMemoActivity.this);
//        builder.setView(R.layout.add_column);
//        builder.show();
        addColumnMemo = (EditText) findViewById(R.id.addColumn_dialog_Input);
        confirm = (Button) findViewById(R.id.addColumn_dialog_confirmButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendCustomizationBean fcb = new FriendCustomizationBean();
                fcb.setName(addColumnMemo.getText().toString());
                FriendBean fb = new FriendBean();
                fcb.setFriendNo(getIntent().getIntExtra("friendNo", 0));
                openDB();
                friendCustomizationDAO.add(fcb);
                AsyncTasKHelper.execute(addResponseListener, fcb);
            }
        });
        cancel = (Button) findViewById(R.id.addColumn_dialog_cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void openDB(){
        dh = new DBHelper(this);
        friendCustomizationDAO = new FriendCustomizationDAO(dh);
        friendDAO = new FriendDAO(dh);
    }
}
