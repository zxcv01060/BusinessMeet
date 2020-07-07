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

public class UserInformationServiceImpl implements UserInformationService{
    private static final UserInformationService userInformationAPI = retrofit.create(UserInformationService.class);

    @Override
    public  Call<ResponseBody<List<UserInformationBean>>> search(UserInformationBean userInformationBean) {
        return userInformationAPI.search(userInformationBean);
    }

    @Override
    public  Call<ResponseBody<UserInformationBean>> add(UserInformationBean userInformationBean) {
        return userInformationAPI.add(userInformationBean);
    }

    @Override
    public  Call<ResponseBody<UserInformationBean>> update(UserInformationBean userinformationBean) {
        return userInformationAPI.update(userinformationBean);
    }

    @Override
    public Call<ResponseBody<UserInformationBean>> getById(String userId) {
        return userInformationAPI.getById(userId);
    }

    @Override
    public Call<ResponseBody<UserInformationBean>> getByBlueTooth(String bluetooth) {
        return userInformationAPI.getByBlueTooth(bluetooth);
    }
}
