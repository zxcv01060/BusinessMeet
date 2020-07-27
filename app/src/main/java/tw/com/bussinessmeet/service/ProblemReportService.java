package tw.com.bussinessmeet.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import tw.com.bussinessmeet.bean.Empty;
import tw.com.bussinessmeet.bean.ProblemReportBean;
import tw.com.bussinessmeet.bean.ResponseBody;

import java.util.List;

public interface ProblemReportService {
    String baseRoute = "problemreport/";
    @POST(baseRoute+"search")
    Call<ResponseBody<List<ProblemReportBean>>> search(@Body ProblemReportBean problemReportBean);
    @POST(baseRoute+"add")
    Call<ResponseBody<ProblemReportBean>> add(@Body ProblemReportBean problemReportBean);
    @POST(baseRoute+"update")
    Call<ResponseBody<ProblemReportBean>> update (@Body ProblemReportBean problemReportBean);
    @POST(baseRoute+"{problemReportNo}/delete")
    Call<ResponseBody<Empty>> delete (@Path("problemReportNo") Integer problemReportNo);
}
