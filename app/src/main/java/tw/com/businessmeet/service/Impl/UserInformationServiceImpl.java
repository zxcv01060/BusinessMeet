package tw.com.businessmeet.service.Impl;

import java.util.List;

import retrofit2.Call;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.bean.UserInformationBean;
import tw.com.businessmeet.service.UserInformationService;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

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
