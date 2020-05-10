package tw.com.bussinessmeet.network;

import android.database.Observable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.MatchedBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.bean.UserInformationBean;

public class RetrofitConfig {
    private static final String BASE_URL = ApiConfig.API_URL;
    private static  Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();
    public static  Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson)) // 使用 Gson 解析
            .baseUrl(BASE_URL)
            .build();
}
