package tw.com.bussinessmeet.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;

public class RetrofitUserInformationConfig {
    private static final String BASE_URL = ApiConfig.API_URL;
    private static final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) // 使用 Gson 解析
            .baseUrl(BASE_URL+"/userinformation")
            .build();
    public static UserInformationApi createRetrofit(){
        return retrofit.create(UserInformationApi.class);
    }
    public interface UserInformationApi {
        @POST("/search")
        Call<ResponseBody<List<UserInformationBean>>> search(@Body UserInformationBean userInformationBean);
        @POST("/add")
        Call<ResponseBody<UserInformationBean>> add(@Body UserInformationBean userInformationBean);
        @POST("/update")
        Call<ResponseBody<Empty>> update (@Body UserInformationBean userinformationBean);
    }

}
