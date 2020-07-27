package tw.com.bussinessmeet.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.FriendBean;
import tw.com.bussinessmeet.bean.ResponseBody;

public interface FriendService {
    String baseRoute = "friend/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<FriendBean>>> search(@Body FriendBean friendBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<FriendBean>> add(@Body FriendBean friendBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<FriendBean>> update (@Body FriendBean friendBean);
}
