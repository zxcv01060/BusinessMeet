package tw.com.bussinessmeet.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;

public interface UserInformationService {
    String baseRoute = "userinformation/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<UserInformationBean>>> search(@Body UserInformationBean userInformationBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<UserInformationBean>> add(@Body UserInformationBean userInformationBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<UserInformationBean>> update (@Body UserInformationBean userinformationBean);
    @POST(baseRoute+"get/{userId}")
    Call<ResponseBody<UserInformationBean>> getById(@Path("userId") String userId);
}
