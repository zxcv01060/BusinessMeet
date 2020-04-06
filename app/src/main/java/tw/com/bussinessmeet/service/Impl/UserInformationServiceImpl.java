package tw.com.bussinessmeet.service.Impl;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.network.RetrofitConfig;
import tw.com.bussinessmeet.service.UserInformationService;

import static tw.com.bussinessmeet.network.RetrofitConfig.retrofit;

public class UserInformationServiceImpl {
    private static final UserInformationService userInformationAPI = retrofit.create(UserInformationService.class);

    @Nullable
    public static  Call<ResponseBody<List<UserInformationBean>>> search(UserInformationBean userInformationBean) {
        return userInformationAPI.search(userInformationBean);
    }

    @Nullable
    public static Call<ResponseBody<UserInformationBean>> add(UserInformationBean userInformationBean) {
        return userInformationAPI.add(userInformationBean);
    }

    @Nullable
    public static Call<ResponseBody<Empty>> update(UserInformationBean userinformationBean) {
        return userInformationAPI.update(userinformationBean);
    }
}
