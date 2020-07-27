package tw.com.bussinessmeet.service.Impl;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.FriendGroupBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.service.FriendGroupService;

import java.util.List;

import static tw.com.bussinessmeet.network.RetrofitConfig.retrofit;

public class FriendGroupServiceImpl implements FriendGroupService{
    private static FriendGroupService FriendGroupApi = retrofit.create(FriendGroupService.class);

    @Override
    public Call<ResponseBody<List<FriendGroupBean>>> search(FriendGroupBean friendGroupBean) {
        return FriendGroupApi.search(friendGroupBean);
    }

    @Override
    public Call<ResponseBody<FriendGroupBean>> add(FriendGroupBean friendGroupBean) {
        return FriendGroupApi.add(friendGroupBean);
    }

    @Override
    public Call<ResponseBody<FriendGroupBean>> update(FriendGroupBean friendGroupBean) {
        return FriendGroupApi.update(friendGroupBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer friendGroupNo) {
        return FriendGroupApi.delete(friendGroupNo);
    }
}
