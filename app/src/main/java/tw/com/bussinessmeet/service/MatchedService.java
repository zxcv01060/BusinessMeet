package tw.com.bussinessmeet.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.MatchedBean;
import tw.com.bussinessmeet.bean.ResponseBody;

public interface MatchedService {
    String baseRoute = "matched/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<MatchedBean>>> search(@Body MatchedBean matchedBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<MatchedBean>> add(@Body MatchedBean matchedBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<Empty>> update (@Body MatchedBean matchedBean);
}
