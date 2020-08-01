package tw.com.businessmeet.service.Impl;

import retrofit2.Call;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.FriendRemarkBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.FriendRemarkService;

import java.util.List;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class FriendLabelServiceImpl implements FriendRemarkService{
    private static FriendRemarkService FriendRemarkApi = retrofit.create(FriendRemarkService.class);

    @Override
    public Call<ResponseBody<List<FriendRemarkBean>>> search(FriendRemarkBean friendRemarkBean) {
        return FriendRemarkApi.search(friendRemarkBean);
    }

    @Override
    public Call<ResponseBody<FriendRemarkBean>> add(FriendRemarkBean friendRemarkBean) {
        return FriendRemarkApi.add(friendRemarkBean);
    }

    @Override
    public Call<ResponseBody<FriendRemarkBean>> update(FriendRemarkBean friendRemarkBean) {
        return FriendRemarkApi.update(friendRemarkBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer friendLabelNo) {
        return FriendRemarkApi.delete(friendLabelNo);
    }
}
