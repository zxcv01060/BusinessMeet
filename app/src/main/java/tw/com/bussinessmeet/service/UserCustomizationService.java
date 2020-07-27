package tw.com.bussinessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserCustomizationBean;

import java.util.List;

public interface UserCustomizationService {
    String baseRoute = "usercustomization/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<UserCustomizationBean>>> search(@Body UserCustomizationBean userCustomizationBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<UserCustomizationBean>> add(@Body UserCustomizationBean userCustomizationBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<UserCustomizationBean>> update (@Body UserCustomizationBean userCustomizationBean);
    @POST(baseRoute+"{userCustomizationNo}/delete")
    Call<ResponseBody<Empty>> delete(@Path("userCustomizationNo") Integer userCustomizationNo);
}
