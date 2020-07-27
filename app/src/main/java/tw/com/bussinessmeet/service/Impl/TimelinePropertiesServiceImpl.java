package tw.com.bussinessmeet.service.Impl;

import retrofit2.Call;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.TimelinePropertiesBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.service.TimelinePropertiesService;

import java.util.List;

import static tw.com.bussinessmeet.network.RetrofitConfig.retrofit;

public class TimelinePropertiesServiceImpl implements TimelinePropertiesService{
    private static TimelinePropertiesService TimelinePropertiesApi = retrofit.create(TimelinePropertiesService.class);

    @Override
    public Call<ResponseBody<List<TimelinePropertiesBean>>> search(TimelinePropertiesBean timelinePropertiesBean) {
        return TimelinePropertiesApi.search(timelinePropertiesBean);
    }

    @Override
    public Call<ResponseBody<TimelinePropertiesBean>> add(TimelinePropertiesBean timelinePropertiesBean) {
        return TimelinePropertiesApi.add(timelinePropertiesBean);
    }

    @Override
    public Call<ResponseBody<TimelinePropertiesBean>> update(TimelinePropertiesBean timelinePropertiesBean) {
        return TimelinePropertiesApi.update(timelinePropertiesBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer timelinePropertiesNo) {
        return TimelinePropertiesApi.delete(timelinePropertiesNo);
    }
}
