package tw.com.bussinessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.TimelineBean;
import tw.com.bussinessmeet.bean.ResponseBody;

import java.util.List;

public interface TimelineService {
    String baseRoute = "timeline/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<TimelineBean>>> search(@Body TimelineBean timelineBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<TimelineBean>> add(@Body TimelineBean timelineBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<TimelineBean>> update (@Body TimelineBean timelineBean);
    @POST(baseRoute+"{timelineNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("timelineNo") Integer timelineNo);
}
