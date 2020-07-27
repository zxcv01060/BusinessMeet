package tw.com.bussinessmeet.service.Impl;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.ActivityLabelBean;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.service.ActivityLabelService;

import java.util.List;

import static tw.com.bussinessmeet.network.RetrofitConfig.retrofit;

public class ActivityLabelServiceImpl implements ActivityLabelService{
    private static ActivityLabelService ActivityLabelApi = retrofit.create(ActivityLabelService.class);

    @Override
    public Call<ResponseBody<List<ActivityLabelBean>>> search(ActivityLabelBean activityLabelBean) {
        return ActivityLabelApi.search(activityLabelBean);
    }

    @Override
    public Call<ResponseBody<ActivityLabelBean>> add(ActivityLabelBean activityLabelBean) {
        return ActivityLabelApi.add(activityLabelBean);
    }

    @Override
    public Call<ResponseBody<ActivityLabelBean>> update(ActivityLabelBean activityLabelBean) {
        return ActivityLabelApi.update(activityLabelBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer activityLabelNo) {
        return ActivityLabelApi.delete(activityLabelNo);
    }
}
