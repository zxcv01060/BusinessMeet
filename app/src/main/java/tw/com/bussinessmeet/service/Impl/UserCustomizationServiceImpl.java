package tw.com.bussinessmeet.service.Impl;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserCustomizationBean;
import tw.com.bussinessmeet.service.UserCustomizationService;

import java.util.List;

import static tw.com.bussinessmeet.network.RetrofitConfig.retrofit;

public class UserCustomizationServiceImpl implements UserCustomizationService{
    private static UserCustomizationService UserCustomizationApi = retrofit.create(UserCustomizationService.class);

    @Override
    public Call<ResponseBody<List<UserCustomizationBean>>> search(UserCustomizationBean userCustomizationBean) {
        return UserCustomizationApi.search(userCustomizationBean);
    }

    @Override
    public Call<ResponseBody<UserCustomizationBean>> add(UserCustomizationBean userCustomizationBean) {
        return UserCustomizationApi.add(userCustomizationBean);
    }

    @Override
    public Call<ResponseBody<UserCustomizationBean>> update(UserCustomizationBean userCustomizationBean) {
        return UserCustomizationApi.update(userCustomizationBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer userCustomizationNo) {
        return UserCustomizationApi.delete(userCustomizationNo);
    }
}
