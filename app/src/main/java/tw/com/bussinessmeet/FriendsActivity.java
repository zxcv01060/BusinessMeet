package tw.com.bussinessmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import tw.com.bussinessmeet.adapter.FriendsRecyclerViewAdapter;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.MatchedBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.helper.AsyncTasKHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.service.Impl.MatchedServiceImpl;
import tw.com.bussinessmeet.service.Impl.UserInformationServiceImpl;
import tw.com.bussinessmeet.service.MatchedService;
import tw.com.bussinessmeet.dao.MatchedDAO;
import tw.com.bussinessmeet.dao.UserInformationDAO;
import tw.com.bussinessmeet.helper.AvatarHelper;
import tw.com.bussinessmeet.helper.BlueToothHelper;
import tw.com.bussinessmeet.helper.DBHelper;
public class FriendsActivity extends AppCompatActivity implements FriendsRecyclerViewAdapter.ClickListener {
    private Button button;
    private UserInformationDAO userInformationDAO;
    private DBHelper DH = null;
    private MatchedDAO matchedDAO;
    private MatchedServiceImpl matchedService = new MatchedServiceImpl() ;
    private UserInformationServiceImpl userInformationService = new UserInformationServiceImpl();
    private BlueToothHelper blueToothHelper;
    private RecyclerView recyclerViewFriends;
    private FriendsRecyclerViewAdapter friendsRecyclerViewAdapter;
    private List<UserInformationBean> userInformationBeanList = new ArrayList<>();
    private AsyncTasKHelper.OnResponseListener<MatchedBean, List<MatchedBean>> searchResponseListener =
            new AsyncTasKHelper.OnResponseListener<MatchedBean, List<MatchedBean>>() {
                @Override
                public Call<ResponseBody<List<MatchedBean>>> request(MatchedBean... matchedBean) {

                    return matchedService.search(matchedBean[0]);
                }

                @Override
                public void onSuccess(List<MatchedBean> matchedBeanList) {
                    Log.e("MatchedBean","success");
                    if (matchedBeanList.size() > 1 || (matchedBeanList.size() == 1 && (matchedBeanList.get(0).getCreateDate() != null && !matchedBeanList.get(0).equals("")))) {

                        for(MatchedBean matchedBean : matchedBeanList) {
                        AsyncTasKHelper.execute(getByIdResponseListener,matchedBean.getMatchedBlueTooth());
                        Log.e("MatchedBean", String.valueOf(matchedBean));
                        Log.e("MatchedBean", String.valueOf(matchedBean.getBlueTooth()));
                        }
                    }
                }

                @Override
                public void onFail(int status) {

                }
            };
    private AsyncTasKHelper.OnResponseListener<String,UserInformationBean> getByIdResponseListener =
            new AsyncTasKHelper.OnResponseListener<String,UserInformationBean>() {
                @Override
                public Call<ResponseBody<UserInformationBean>> request(String... blueTooth) {

                    return userInformationService.getById(blueTooth[0]);
                }

                @Override
                public void onSuccess(UserInformationBean userInformationBean) {
                    Log.e("MatchedBean","success");
                    friendsRecyclerViewAdapter.dataInsert(userInformationBean);
                }

                @Override
                public void onFail(int status) {

                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        recyclerViewFriends = findViewById(R.id.friendsView);
        //bottomNavigationView
        //Initialize And Assign Variable
        openDB();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.menu_friends);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setItemIconTintList(null);  //顯示頭像
        Menu BVMenu = bottomNavigationView.getMenu();
        AvatarHelper avatarHelper = new AvatarHelper();
        UserInformationBean ufb = new UserInformationBean();
        Cursor result = userInformationDAO.searchAll(ufb);
        Log.e("result",String.valueOf(result));

        MenuItem userItem = BVMenu.findItem(R.id.menu_home);
        Bitmap myPhoto = avatarHelper.getImageResource(result.getString(result.getColumnIndex("avatar")));
        userItem.setIcon(new BitmapDrawable(getResources(), myPhoto));
        createRecyclerViewFriends();
        blueToothHelper = new BlueToothHelper(this);
        MatchedBean matchedBean = new MatchedBean();
        matchedBean.setBlueTooth(blueToothHelper.getMyBuleTooth());
        Log.e("matched",matchedBean.getBlueTooth());
        AsyncTasKHelper.execute(searchResponseListener,matchedBean);

    }
    private void openDB(){
        Log.d("add","openDB");
        DH = new DBHelper(this);
        userInformationDAO = new UserInformationDAO(DH);
        matchedDAO = new MatchedDAO(DH);

    }
    private void createRecyclerViewFriends() {
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerViewAdapter = new FriendsRecyclerViewAdapter(this, this.userInformationBeanList);
        friendsRecyclerViewAdapter.setClickListener(this);
        recyclerViewFriends.setAdapter(friendsRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewFriends.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewFriends.addItemDecoration(dividerItemDecoration);
        Log.d("resultMainAdapter", String.valueOf(friendsRecyclerViewAdapter.getItemCount()));
    }
    public void onClick(View view, int position){
        Intent intent = new Intent();
        intent.setClass(this,FriendsIntroductionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("blueToothAddress",friendsRecyclerViewAdapter.getUserInformation(position).getBlueTooth());
        intent.putExtras(bundle);
        startActivity(intent);

/*    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem switchButton = menu.findItem(R.id.menu_friends);
        boolean searchScriptDisplayed = false;
        if(searchScriptDisplayed){
            switchButton.setIcon(R.drawable.ic_people_blue_24dp);
        }else{
            switchButton.setIcon(R.drawable.ic_people_outline_blue_24dp);
        }
        return super.onPrepareOptionsMenu(menu);

    }*/
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
