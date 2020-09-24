package tw.com.businessmeet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import tw.com.businessmeet.bean.FriendBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.dao.FriendDAO;
import tw.com.businessmeet.helper.AsyncTasKHelper;
import tw.com.businessmeet.helper.BlueToothHelper;
import tw.com.businessmeet.helper.DBHelper;
import tw.com.businessmeet.service.Impl.FriendServiceImpl;


public class FriendsMemoActivity extends AppCompatActivity {
    private TextView memo;
    private String friendId;
    private ImageButton editProfileConfirmButtom;
    private BlueToothHelper blueToothHelper;
    private FriendServiceImpl matchedService = new FriendServiceImpl();
    private AsyncTasKHelper.OnResponseListener<FriendBean, FriendBean> updateResponseListener = new AsyncTasKHelper.OnResponseListener<FriendBean, FriendBean>() {
        @Override
        public Call<ResponseBody<FriendBean>> request(FriendBean... friendBeans) {
            return matchedService.update(friendBeans[0]);
        }

        @Override
        public void onSuccess(FriendBean friendBean) {
            friendDAO.update(friendBean);
            Intent intent = new Intent();
            intent.setClass(FriendsMemoActivity.this, FriendsIntroductionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("friendId",friendBean.getFriendId());
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onFail(int status,String message) {

        }
    };
    private AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>> searchResponseListener = new AsyncTasKHelper.OnResponseListener<FriendBean, List<FriendBean>>() {
        @Override
        public Call<ResponseBody<List<FriendBean>>> request(FriendBean... friendBeans) {
            return matchedService.search(friendBeans[0]);
        }

        @Override
        public void onSuccess(List<FriendBean> friendBeanList) {
            if(friendBeanList.get(0).getRemark()!=null)
                memo.append(friendBeanList.get(0).getRemark());
        }

        @Override
        public void onFail(int status,String message) {

        }
    };
    private DBHelper DH;
    private FriendDAO friendDAO;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_edit_introduction);

        friendId = getIntent().getStringExtra("friendId");
//        Log.d("memo", blueToothAddress);
        blueToothHelper = new BlueToothHelper(this);
        memo = (TextView) findViewById(R.id.friends_memo);
        editProfileConfirmButtom = (ImageButton) findViewById(R.id.editProfileConfirmButtom);
        editProfileConfirmButtom.setOnClickListener(editConfirmClick);
        FriendBean friendBean = new FriendBean();
        friendBean.setMatchmakerId(blueToothHelper.getUserId());
        friendBean.setFriendId(friendId);
        AsyncTasKHelper.execute(searchResponseListener,friendBean);
        openDB();
    }
    private void openDB() {
        DH = new DBHelper(this);
        friendDAO = new FriendDAO(DH);
    }
    public View.OnClickListener editConfirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FriendBean friendBean = new FriendBean();
            friendBean.setMatchmakerId(blueToothHelper.getUserId());
            Log.e("getMyBuleTooth()",blueToothHelper.getUserId());
            friendBean.setFriendId(friendId);
            friendBean.setRemark(memo.getText().toString());
            Log.d("String.valueOf",String.valueOf(memo.getText()));
            AsyncTasKHelper.execute(updateResponseListener,friendBean);
            friendDAO.update(friendBean);

        }
    };
}