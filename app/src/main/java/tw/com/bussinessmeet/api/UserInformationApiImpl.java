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
    public static  Call<ResponseBody<List<UserInformationBean>>> searchUserInformation(UserInformationBean userInformationBean) {
        return userInformationAPI.search(userInformationBean);
    }

    @Nullable
    public static Call<ResponseBody<UserInformationBean>> add(UserInformationBean userInformationBean) {
        Log.d("resultadd",userInformationBean.getUserName());
        return userInformationAPI.add(userInformationBean);
    }

    @Nullable
    public static Call<ResponseBody<Empty>> update(UserInformationBean userinformationBean) {
        return userInformationAPI.update(userinformationBean);
    }
}
