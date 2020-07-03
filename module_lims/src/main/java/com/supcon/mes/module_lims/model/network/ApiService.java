package com.supcon.mes.module_lims.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationListEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name
 */
@ApiFactory(name = "BaseLimsHttpClient")
public interface ApiService {
    @POST("/msService/QCS/inspect/inspect/{query}")
    Flowable<InspectionApplicationListEntity> inspectionRequestList(@Path("query") String query,
                                                                    @Body Map<String, Object> map);

    @POST("/msService/LIMSSample/sampleReport/sampleReport/{query}")
    Flowable<SurveyReportListEntity> surveyReportList(@Path("query") String query,
                                                      @Body Map<String, Object> map);
}
