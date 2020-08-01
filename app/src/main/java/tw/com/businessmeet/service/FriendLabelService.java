package tw.com.businessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.FriendLabelBean;
import tw.com.businessmeet.bean.ResponseBody;

import java.util.List;

public interface FriendLabelService {
    String baseRoute = "friendlabel/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<FriendLabelBean>>> search(@Body FriendLabelBean friendLabelBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<FriendLabelBean>> add(@Body FriendLabelBean friendLabelBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<FriendLabelBean>> update (@Body FriendLabelBean friendLabelBean);
    @POST(baseRoute+"{friendLabelNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("friendLabelNo") Integer friendLabelNo);
}
