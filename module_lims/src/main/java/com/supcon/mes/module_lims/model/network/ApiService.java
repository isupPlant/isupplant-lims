package com.supcon.mes.module_lims.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationListEntity;
import com.supcon.mes.module_lims.model.bean.SampleInquiryListEntity;
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
    /**
     * 请检单列表接口
     * @param query
     * @param map
     * @return
     */
    @POST("/msService/QCS/inspect/inspect/{query}")
    Flowable<InspectionApplicationListEntity> inspectionRequestList(@Path("query") String query,
                                                                    @Body Map<String, Object> map);

    /**
     * 报告单列表接口
     * @param query
     * @param map
     * @return
     */
    @POST("/msService/QCS/inspectReport/inspectReport/{query}")
    Flowable<SurveyReportListEntity> surveyReportList(@Path("query") String query,
                                                      @Body Map<String, Object> map);

    /**
     * 样品收样取样列表接口
     * @param query
     * @param map
     * @return
     */
    @POST("/msService/LIMSSample/sample/sampleInfo/{query}")
    Flowable<SampleInquiryListEntity> sampleInquiryList(@Path("query") String query,
                                                        @Body Map<String, Object> map);


}
