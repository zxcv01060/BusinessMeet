package tw.com.businessmeet.service.Impl;

import retrofit2.Call;
import tw.com.businessmeet.bean.Empty;
import tw.com.businessmeet.bean.GroupsBean;
import tw.com.businessmeet.bean.ResponseBody;
import tw.com.businessmeet.service.GroupsService;

import java.util.List;

import static tw.com.businessmeet.network.RetrofitConfig.retrofit;

public class GroupsServiceImpl implements GroupsService{
    private static GroupsService GroupsApi = retrofit.create(GroupsService.class);

    @Override
    public Call<ResponseBody<List<GroupsBean>>> search(GroupsBean groupsBean) {
        return GroupsApi.search(groupsBean);
    }

    @Override
    public Call<ResponseBody<GroupsBean>> add(GroupsBean groupsBean) {
        return GroupsApi.add(groupsBean);
    }

    @Override
    public Call<ResponseBody<GroupsBean>> update(GroupsBean groupsBean) {
        return GroupsApi.update(groupsBean);
    }

    @Override
    public Call<ResponseBody<Empty>> delete(Integer groupsNo) {
        return GroupsApi.delete(groupsNo);
    }
}
