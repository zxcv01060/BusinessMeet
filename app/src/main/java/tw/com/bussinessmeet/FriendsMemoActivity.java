package tw.com.bussinessmeet;

import android.bluetooth.BluetoothA2dp;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.helper.AsyncTasKHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.service.Impl.MatchedServiceImpl;
import tw.com.bussinessmeet.bean.MatchedBean;


public class FriendsMemoActivity extends AppCompatActivity {
    private TextView memo;
    private MatchedBean matchedBean = new MatchedBean();

    private BlueToothHelper blueToothHelper = new BlueToothHelper(this);

    private MatchedServiceImpl matchedService = new MatchedServiceImpl();
    private AsyncTasKHelper.OnResponseListener<MatchedBean, String> updateMemoResponseListener = new AsyncTasKHelper.OnResponseListener<MatchedBean, String>() {
        @Override
        public Call<ResponseBody<String>> request(MatchedBean... matchedBeans) {
            return null;
        }

        @Override
        public void onSuccess(String s) {

        }

        @Override
        public void onFail(int status) {

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_edit_introduction);

        String blueToothAddress = getIntent().getStringExtra("blueToothAddress");
//        Log.d("memo", blueToothAddress);

        memo = (TextView) findViewById(R.id.friends_memo);
    }
}