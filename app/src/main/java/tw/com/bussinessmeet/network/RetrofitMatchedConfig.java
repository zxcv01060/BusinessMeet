package tw.com.bussinessmeet.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ResponseBody;


public class RetrofitMatchedConfug {
    private static final String BASE_URL = ApiConfig.API_MATCHED_URL;
    private static final Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) // 使用 Gson 解析
            .baseUrl(BASE_URL)
            .build();
    public static RetrofitMatchedConfug.MatchedApi createRetrofit(){
        return retrofit.create(RetrofitMatchedConfug.MatchedApi.class);
    }
    public interface MatchedApi {
        @POST("search")
        Call<ResponseBody<List<Matched>>> search(@Body UserInformationBean userInformationBean);
        @POST("add")
        Call<ResponseBody<UserInformationBean>> add(@Body UserInformationBean userInformationBean);
        @POST("update")
        Call<ResponseBody<Empty>> update (@Body UserInformationBean userinformationBean);
    }
}
