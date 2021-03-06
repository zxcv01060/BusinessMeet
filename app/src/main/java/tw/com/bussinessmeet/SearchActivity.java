package tw.com.bussinessmeet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import tw.com.bussinessmeet.helper.AvatarHelper;
import tw.com.bussinessmeet.helper.NotificationHelper;
import tw.com.bussinessmeet.adapter.MatchedDeviceRecyclerViewAdapter;
import tw.com.bussinessmeet.adapter.UnmatchedDeviceRecyclerViewAdapter;
import tw.com.bussinessmeet.service.Impl.MatchedServiceImpl;
import tw.com.bussinessmeet.bean.MatchedBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.dao.MatchedDAO;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AsyncTasKHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.bean.UserInformationBean;

public class SearchActivity extends AppCompatActivity implements MatchedDeviceRecyclerViewAdapter.SearchClickListener, UnmatchedDeviceRecyclerViewAdapter.MatchedClickListener {
    private DBHelper DH = null;
    private UserInformationDAO userInformationDAO;
    private BlueToothHelper blueTooth;
    private RecyclerView recyclerViewMatched,recyclerViewUnmatched;
    private MatchedDeviceRecyclerViewAdapter matchedRecyclerViewAdapter;
    private UnmatchedDeviceRecyclerViewAdapter unmatchedRecyclerViewAdapter;
    private List<UserInformationBean> matchedList = new ArrayList<>();
    private List<UserInformationBean> unmatchedList = new ArrayList<>();
    private MatchedServiceImpl matchedApi = new MatchedServiceImpl();
    private MatchedServiceImpl matchedService = new MatchedServiceImpl();
    private TextView search_title;
    private MatchedDAO matchedDAO;
    private BlueToothHelper blueToothHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // 通过msg传递过来的信息，吐司一下收到的信息
            try {
                String[] message = ((String) msg.obj).split(",");
                String myBlueToothAddress = blueTooth.getMyBuleTooth();
                String matchedAddress = message[0];

                Toast.makeText(SearchActivity.this, matchedAddress, Toast.LENGTH_LONG).show();
                MatchedBean matchedBean = new MatchedBean();
                Log.d("getblueTooth",blueTooth.getMyBuleTooth());
                Log.d("getblueTooth",matchedAddress);
                matchedBean.setBlueTooth(blueTooth.getMyBuleTooth());
                matchedBean.setMatchedBlueTooth(matchedAddress);
                AsyncTasKHelper.execute(addResponseListener, matchedBean);
                matchedDAO.add(matchedBean);
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this,FriendsIntroductionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("blueToothAddress",matchedAddress);
                intent.putExtras(bundle);
                startActivity(intent);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };


    private AsyncTasKHelper.OnResponseListener<MatchedBean, MatchedBean> addResponseListener =
            new AsyncTasKHelper.OnResponseListener<MatchedBean, MatchedBean>() {
                @Override
                public Call<ResponseBody<MatchedBean>> request(MatchedBean... matchedBean) {

                    return matchedService.add(matchedBean[0]);
                }

                @Override
                public void onSuccess(MatchedBean matchedBean) {
                    Log.e("MatchedBean",String.valueOf(matchedBean));
                    Log.e("MatchedBean",String.valueOf(matchedBean.getBlueTooth()));
                }

                @Override
                public void onFail(int status) {

                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        openDB();

        recyclerViewMatched = findViewById(R.id.matched);
        recyclerViewUnmatched = findViewById(R.id.unmatched);
        createRecyclerViewUnmatched();
        createRecyclerViewMatched();
        TextView search_title =  findViewById(R.id.search_title);
        Log.e("searhTitle",String.valueOf(search_title));
        blueTooth = new BlueToothHelper(this);
        blueTooth.startBuleTooth();
        blueTooth.scanBluth();
        blueTooth.searchBlueTooth(userInformationDAO,matchedRecyclerViewAdapter,unmatchedRecyclerViewAdapter);
        blueTooth.startThread(handler);

        //bottomNavigationView
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像

        Menu BVMenu = bottomNavigationView.getMenu();
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        AvatarHelper avatarHelper = new AvatarHelper();
        //blueToothHelper.startBuleTooth();
        Log.d("seedmess","ness");
        UserInformationBean ufb = new UserInformationBean();
        //ufb.setBlueTooth(blueToothHelper.getMyBuleTooth());
        Cursor result = userInformationDAO.searchAll(ufb);
        Log.e("result",String.valueOf(result));

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));



    }

    private void openDB(){
        Log.d("add","openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
        matchedDAO = new MatchedDAO(DH);

    }
    private void createRecyclerViewMatched() {
        recyclerViewMatched.setLayoutManager(new LinearLayoutManager(this));
        matchedRecyclerViewAdapter = new MatchedDeviceRecyclerViewAdapter(this, this.matchedList);
        matchedRecyclerViewAdapter.setClickListener(this);
        recyclerViewMatched.setAdapter(matchedRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewMatched.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewMatched.addItemDecoration(dividerItemDecoration);
        Log.d("resultMainAdapter", String.valueOf(matchedRecyclerViewAdapter.getItemCount()));
    }
    private void createRecyclerViewUnmatched() {
        recyclerViewUnmatched.setLayoutManager(new LinearLayoutManager(this));
        unmatchedRecyclerViewAdapter = new UnmatchedDeviceRecyclerViewAdapter(this, this.unmatchedList);
        unmatchedRecyclerViewAdapter.setClickListener(this);
        recyclerViewUnmatched.setAdapter(unmatchedRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewUnmatched.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewUnmatched.addItemDecoration(dividerItemDecoration);
        Log.d("resultMainAdapter", String.valueOf(unmatchedRecyclerViewAdapter.getItemCount()));
    }
    @Override
    public void onSearchClick(View view, int position) {
        Log.d("results","success");
        Log.d("results",String.valueOf(position));
//        UserInformationBean userInformationBean = matchedRecyclerViewAdapter.getUserInformation(position);
//        String address = userInformationBean.getBlueTooth();
//        String userName = userInformationBean.getUserName();
//        blueTooth.matched(address,userName,addResponseListener,matchedDAO);
        blueTooth.cancelDiscovery();
        Intent intent = new Intent();
        intent.setClass(SearchActivity.this,FriendsIntroductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("blueToothAddress",matchedRecyclerViewAdapter.getUserInformation(position).getBlueTooth());
        intent.putExtras(bundle);
        startActivity(intent);
        Log.e("send","============================");
        //NotificationHelper notificationHelper = new NotificationHelper(this);
        //notificationHelper.sendMessage(address);
    }
    @Override
    public void onMatchedClick(View view, int position) {
        Log.d("results","success");
        Log.d("results",String.valueOf(position));
        blueTooth.cancelDiscovery();
        UserInformationBean userInformationBean = unmatchedRecyclerViewAdapter.getUserInformation(position);
        String address = userInformationBean.getBlueTooth();
        String userName = userInformationBean.getUserName();
        blueTooth.matched(address,userName,addResponseListener,matchedDAO);
        Intent intent = new Intent();
        intent.setClass(SearchActivity.this,FriendsIntroductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("blueToothAddress",unmatchedRecyclerViewAdapter.getUserInformation(position).getBlueTooth());
        intent.putExtras(bundle);
        startActivity(intent);
    }


    //button_nav Perform ItemSelectedListener
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
                    return true;
                case R.id.menu_friends:
                    //menuItem.setIcon(R.drawable.ic_people_black_24dp);
                    startActivity(new Intent(getApplicationContext()
                            ,FriendsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        }
    });

}
