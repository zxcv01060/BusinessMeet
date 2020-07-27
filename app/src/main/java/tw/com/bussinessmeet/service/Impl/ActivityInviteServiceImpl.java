package tw.com.bussinessmeet.service.Impl;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.ActivityInviteBean;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.service.ActivityInviteService;
import tw.com.bussinessmeet.service.FriendService;

import java.util.List;

import static tw.com.bussinessmeet.network.RetrofitConfig.retrofit;

public class ActivityInviteServiceImpl implements ActivityInviteService{
    private static ActivityInviteService ActivityInviteApi = retrofit.create(ActivityInviteService.class);

    @Override
    public Call<ResponseBody<List<ActivityInviteBean>>> search(ActivityInviteBean activityInviteBean) {
        return ActivityInviteApi.search(activityInviteBean);
    }

    @Override
    public Call<ResponseBody<ActivityInviteBean>> add(ActivityInviteBean activityInviteBean) {
        return ActivityInviteApi.add(activityInviteBean);
    }

    @Override
    public Call<ResponseBody<ActivityInviteBean>> update(ActivityInviteBean activityInviteBean) {
        return ActivityInviteApi.update(activityInviteBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer activityInviteNo) {
        return ActivityInviteApi.delete(activityInviteNo);
    }
}
