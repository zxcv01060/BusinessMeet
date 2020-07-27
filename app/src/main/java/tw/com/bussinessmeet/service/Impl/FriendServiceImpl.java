package tw.com.bussinessmeet.service.Impl;

import androidx.annotation.Nullable;

import java.util.List;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.FriendBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.service.FriendService;

import static tw.com.bussinessmeet.network.RetrofitConfig.retrofit;

public class FriendServiceImpl {
    private static FriendService FriendApi = retrofit.create(FriendService.class);
    @Nullable
    public static Call<ResponseBody<List<FriendBean>>> search(FriendBean friendBean) {
        return FriendApi.search(friendBean);
    }
    @Nullable
    public static Call<ResponseBody<FriendBean>> add(FriendBean friendBean) {
        return FriendApi.add(friendBean);
    }
    @Nullable
    public static Call<ResponseBody<FriendBean>> update(FriendBean friendBean) {
        return FriendApi.update(friendBean);
    }

}
