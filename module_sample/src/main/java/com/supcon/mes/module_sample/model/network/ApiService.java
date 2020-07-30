package com.supcon.mes.module_sample.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportSubmitEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;
import com.supcon.mes.module_sample.model.bean.SampleEntity;
import com.supcon.mes.module_sample.model.bean.SampleReportSubmitEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * author huodongsheng
 * on 2020/7/8
 * class name
 */
@ApiFactory(name = "SampleHttpClient")
public interface ApiService {

    @POST("msService/LIMSSample/sampleReport/sampleReport/{query}")
    Flowable<SurveyReportListEntity> getSampleSurveyReportList(@Path("query") String query, @Body Map<String, Object> map);

    @GET("/msService/LIMSSample/sampleReport/sampleReport/data/{id}")
    Flowable<BAP5CommonEntity<SurveyReportEntity>> getSampleReport(@Path("id") Long id);

    /**
     * 获取质量检验中检验报告单pt的质量标准
     * @param params
     * @return
     */
    @POST("/msService/LIMSSample/sampleReport/reportCom/getTestComList")
    Flowable<StdJudgeSpecListEntity> getReportComList(@Body Map<String,Object> params);

    @GET("/msService/LIMSSample/sampleReport/sampleReport/data/{moduleId}")
    Flowable<BAP5CommonEntity<SurveyReportEntity>> getSampleReportByPending(@Path("moduleId") long moduleId,@Query("pendingId") Long pendingId);


    /**
     * 提交样品检验报告单工作流
     * @param path
     * @param params
     * @param reportSubmitEntity
     * @return
     */
    @POST("/msService/LIMSSample/sampleReport/sampleReport/{sampleReportView}/submit")
    Flowable<SubmitResultEntity> submitSampleReport(@Path("sampleReportView") String path, @QueryMap Map<String,Object> params, @Body SampleReportSubmitEntity reportSubmitEntity);

    /**
     * 按样品结果录入 获取样品列表接口
     * @param pageType
     * @param map
     * @return
     */
    @POST("/msService/LIMSSample/sample/sampleInfo/getPendingSample")
    Flowable<BAP5CommonEntity<CommonListEntity<SampleEntity>>> getSampleList(@Query("pageType") String pageType,
                                                                             @Body Map<String, Object> map);

}
