package com.supcon.mes.module_sample.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * author huodongsheng
 * on 2020/7/8
 * class name
 */
@ApiFactory(name = "SampleHttpClient")
public interface ApiService {

    @POST("msService/LIMSSample/sampleReport/sampleReport/{query}")
    Flowable<SurveyReportListEntity> getSampleSurveyReportList(@Path("query") String query,
                                                               @Body Map<String, Object> map);
}
