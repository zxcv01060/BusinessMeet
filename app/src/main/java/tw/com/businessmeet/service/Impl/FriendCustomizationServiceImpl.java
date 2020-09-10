package tw.com.businessmeet.service.Impl;

import retrofit2.Call;
import tw.com.businessmeet.bean.FriendCustomizationBean;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.FriendCustomizationService;

import java.util.List;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class FriendCustomizationServiceImpl implements FriendCustomizationService{
    private static FriendCustomizationService FriendCustomizationApi = retrofit.create(FriendCustomizationService.class);

    @Override
    public Call<ResponseBody<List<FriendCustomizationBean>>> search(FriendCustomizationBean friendCustomizationBean) {
        return FriendCustomizationApi.search(friendCustomizationBean);
    }

    @Override
    public Call<ResponseBody<FriendCustomizationBean>> add(FriendCustomizationBean friendCustomizationBean) {
        return FriendCustomizationApi.add(friendCustomizationBean);
    }

    @Override
    public Call<ResponseBody<FriendCustomizationBean>> update(FriendCustomizationBean friendCustomizationBean) {
        return FriendCustomizationApi.update(friendCustomizationBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer friendCustomizationNo) {
        return FriendCustomizationApi.delete(friendCustomizationNo);
    }
}
