package tw.com.bussinessmeet.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
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
    Call<ResponseBody<Empty>> update (@Body UserInformationBean userinformationBean);
}
