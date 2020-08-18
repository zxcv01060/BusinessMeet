package tw.com.businessmeet.service.Impl;

import retrofit2.Call;
import tw.com.businessmeet.bean.ActivityInviteBean;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.ActivityInviteService;

import java.util.List;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

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
