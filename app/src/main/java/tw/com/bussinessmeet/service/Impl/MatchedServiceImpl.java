package tw.com.bussinessmeet.service.Impl;

import androidx.annotation.Nullable;

import java.util.List;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.FriendBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.service.MatchedService;

import static tw.com.bussinessmeet.network.RetrofitConfig.retrofit;

public class MatchedServiceImpl {
    private static  MatchedService matchedApi = retrofit.create(MatchedService.class);
    @Nullable
    public static Call<ResponseBody<List<FriendBean>>> search(FriendBean friendBean) {
        return matchedApi.search(friendBean);
    }
    @Nullable
    public static Call<ResponseBody<FriendBean>> add(FriendBean friendBean) {
        return matchedApi.add(friendBean);
    }
    @Nullable
    public static Call<ResponseBody<FriendBean>> update(FriendBean friendBean) {
        return matchedApi.update(friendBean);
    }

}
