package tw.com.bussinessmeet.service.Impl;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.MatchedBean;
import tw.com.bussinessmeet.bean.ResponseBody;
import tw.com.bussinessmeet.network.RetrofitConfig;
import tw.com.bussinessmeet.service.MatchedService;

import static tw.com.bussinessmeet.network.RetrofitConfig.retrofit;

public class MatchedServiceImpl {
    private static  MatchedService matchedApi = retrofit.create(MatchedService.class);
    @Nullable
    public static Call<ResponseBody<List<MatchedBean>>> search(MatchedBean matchedBean) {
        return matchedApi.search(matchedBean);
    }
    @Nullable
    public static Call<ResponseBody<MatchedBean>> add(MatchedBean matchedBean) {
        return matchedApi.add(matchedBean);
    }
    @Nullable
    public static Call<ResponseBody<Empty>> update(MatchedBean matchedBean) {
        return matchedApi.update(matchedBean);
    }

}
