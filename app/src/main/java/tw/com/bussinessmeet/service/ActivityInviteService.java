package tw.com.bussinessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ActivityInviteBean;
import tw.com.bussinessmeet.bean.ResponseBody;

import java.util.List;

public interface ActivityInviteService {
    String baseRoute = "activityinvite/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<ActivityInviteBean>>> search(@Body ActivityInviteBean activityInviteBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<ActivityInviteBean>> add(@Body ActivityInviteBean activityInviteBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<ActivityInviteBean>> update (@Body ActivityInviteBean activityInviteBean);
    @POST(baseRoute+"{activityInviteNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("activityInviteNo") Integer activityInviteNo);
}
