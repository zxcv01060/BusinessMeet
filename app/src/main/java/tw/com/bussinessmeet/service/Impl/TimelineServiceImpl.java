package tw.com.bussinessmeet.service.Impl;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.TimelineBean;
import tw.com.bussinessmeet.service.TimelineService;

import java.util.List;

import static tw.com.bussinessmeet.network.RetrofitConfig.retrofit;

public class TimelineServiceImpl implements TimelineService{
    private static TimelineService TimelineApi = retrofit.create(TimelineService.class);

    @Override
    public Call<ResponseBody<List<TimelineBean>>> search(TimelineBean timelineBean) {
        return TimelineApi.search(timelineBean);
    }

    @Override
    public Call<ResponseBody<TimelineBean>> add(TimelineBean timelineBean) {
        return TimelineApi.add(timelineBean);
    }

    @Override
    public Call<ResponseBody<TimelineBean>> update(TimelineBean timelineBean) {
        return TimelineApi.update(timelineBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer timelineNo) {
        return TimelineApi.delete(timelineNo);
    }
}
