package tw.com.bussinessmeet;

import android.bluetooth.BluetoothA2dp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.MatchedDAO;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AsyncTasKHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;
import tw.com.bussinessmeet.service.Impl.MatchedServiceImpl;
import tw.com.bussinessmeet.bean.MatchedBean;


public class FriendsMemoActivity extends AppCompatActivity {
    private TextView memo;
    private String blueToothAddress;
    private ImageButton editProfileConfirmButtom;
    private BlueToothHelper blueToothHelper;
    private MatchedServiceImpl matchedService = new MatchedServiceImpl();
    private AsyncTasKHelper.OnResponseListener<MatchedBean, Empty> updateResponseListener = new AsyncTasKHelper.OnResponseListener<MatchedBean, Empty>() {
        @Override
        public Call<ResponseBody<Empty>> request(MatchedBean... matchedBeans) {
            return matchedService.update(matchedBeans[0]);
        }

        @Override
        public void onSuccess(Empty empty) {
            Intent intent = new Intent();
            intent.setClass(FriendsMemoActivity.this, FriendsIntroductionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("blueToothAddress",blueToothAddress);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onFail(int status) {

        }
    };
    private AsyncTasKHelper.OnResponseListener<MatchedBean, List<MatchedBean>> searchResponseListener = new AsyncTasKHelper.OnResponseListener<MatchedBean, List<MatchedBean>>() {
        @Override
        public Call<ResponseBody<List<MatchedBean>>> request(MatchedBean... matchedBeans) {
            return matchedService.search(matchedBeans[0]);
        }

        @Override
        public void onSuccess(List<MatchedBean> matchedBeanList) {
            memo.append(matchedBeanList.get(0).getMemorandum());
        }

        @Override
        public void onFail(int status) {

        }
    };
    private DBHelper DH;
    private MatchedDAO matchedDAO;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_edit_introduction);

        blueToothAddress = getIntent().getStringExtra("blueToothAddress");
//        Log.d("memo", blueToothAddress);
        blueToothHelper = new BlueToothHelper(this);
        memo = (TextView) findViewById(R.id.friends_memo);
        editProfileConfirmButtom = (ImageButton) findViewById(R.id.editProfileConfirmButtom);
        editProfileConfirmButtom.setOnClickListener(editConfirmClick);
        MatchedBean matchedBean = new MatchedBean();
        matchedBean.setBlueTooth(blueToothHelper.getMyBuleTooth());
        matchedBean.setMatchedBlueTooth(blueToothAddress);
        AsyncTasKHelper.execute(searchResponseListener,matchedBean);
        openDB();
    }
    private void openDB() {
        DH = new DBHelper(this);
        matchedDAO = new MatchedDAO(DH);
    }
    public View.OnClickListener editConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MatchedBean matchedBean = new MatchedBean();
            matchedBean.setBlueTooth(blueToothHelper.getMyBuleTooth());
            Log.e("getMyBuleTooth()",blueToothHelper.getMyBuleTooth());
            Log.e("getMatchedBuleTooth()",blueToothAddress);
            matchedBean.setMatchedBlueTooth(blueToothAddress);
            matchedBean.setMemorandum(memo.getText().toString());
            Log.d("String.valueOf",String.valueOf(memo.getText()));
            AsyncTasKHelper.execute(updateResponseListener,matchedBean);
            matchedDAO.update(matchedBean);

        }
    };
}