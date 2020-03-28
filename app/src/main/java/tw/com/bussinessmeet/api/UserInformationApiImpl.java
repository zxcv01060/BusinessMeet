package tw.com.bussinessmeet.api;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.network.RetrofitUserInformationConfig;

public class UserInformationApiImpl {
    private static final RetrofitUserInformationConfig.UserInformationApi userInformationAPI = new RetrofitUserInformationConfig().createRetrofit();

    @Nullable
    public static List<UserInformationBean> searchUserInformation(UserInformationBean userInformationBean) {
        try {
            Log.d("resultblueToothget",userInformationBean.getBlueTooth());
            // Create an instance of our GitHub API interface.


            // Create a call instance for looking up Retrofit Datas.
            Call<ResponseBody<List<UserInformationBean>>> call = userInformationAPI.search(userInformationBean);
            Response<ResponseBody<List<UserInformationBean>>> response =  call.execute();
            ResponseBody<List<UserInformationBean>> responseBody = response.body();
            Log.d("resultcall",String.valueOf(responseBody.getSuccess()));
            // Fetch and print a list of the Datas to the library.
            if(responseBody.getSuccess()) {
                return responseBody.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    public static Call<ResponseBody<UserInformationBean>> add(UserInformationBean userInformationBean) {
        return userInformationAPI.add(userInformationBean);
    }

    @Nullable
    public boolean update(UserInformationBean userinformationBean) {
        try {
            Call<ResponseBody<Empty>> call = userInformationAPI.update(userinformationBean);
            Response<ResponseBody<Empty>> response = call.execute();
            ResponseBody<Empty> responseBody = response.body();
            return responseBody.getSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
