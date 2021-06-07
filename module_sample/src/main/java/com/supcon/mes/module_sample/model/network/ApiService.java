package com.supcon.mes.module_sample.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.module_lims.model.bean.BaseSystemBackEntity;
import com.supcon.mes.module_lims.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_lims.model.bean.SampleEntity;
import com.supcon.mes.module_sample.model.bean.SampleResultCheckProjectDetailEntity;
import com.supcon.mes.module_sample.model.bean.SampleResultCheckProjectEntity;
import com.supcon.mes.module_sample.model.bean.SampleTestMaterialEntity;
import com.supcon.mes.module_sample.model.bean.SanpleResultCheckItemEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;
import com.supcon.mes.module_lims.model.bean.WareStoreEntity;
import com.supcon.mes.module_sample.model.bean.FileAnalyseListEntity;
import com.supcon.mes.module_sample.model.bean.FileDataEntity;
import com.supcon.mes.module_sample.model.bean.InspectionItemsEntity;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.model.bean.SampleAccountEntity;
import com.supcon.mes.module_sample.model.bean.SampleReportSubmitEntity;
import com.supcon.mes.module_sample.model.bean.SampleSignatureEntity;
import com.supcon.mes.module_sample.model.bean.SingleInspectionItemListEntity;
import com.supcon.mes.module_sample.model.bean.TestDeviceEntity;
import com.supcon.mes.module_sample.model.bean.TestMaterialEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

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

    /**
     * 按样品结果录入 获取样品检测项目
     * @param dealMode
     * @param sampleId
     * @param map
     * @return
     */
    @POST("/msService/LIMSSample/sample/sampleTest/findSampleTest")
    Flowable<BAP5CommonEntity<CommonListEntity<InspectionItemsEntity>>> getInspectionItemList(@Query("dealMode") String dealMode,
                                                                                              @Query("sampleId") String sampleId,
                                                                                              @Body Map<String, Object> map);

    /**
     * 获取检验分项列表数据
     * @param sampleTestIds
     * @return
     */
    @GET("/msService/LIMSSample/sample/sampleCom/getSampleCom")
    Flowable<BAP5CommonEntity<CommonListEntity<InspectionSubEntity>>> getInspectionSubProjectList(@Query("sampleTestIds") String sampleTestIds);


    /**
     * 获取检验分项列表的列名
     * @param sampleTestIds
     * @return
     */
    @GET("/msService/LIMSSample/sample/sampleTest/getStdGradeColumns")
    Flowable<BAP5CommonListEntity<InspectionItemColumnEntity>> getInspectionSubProjectColumn(@Query("sampleTestIds") String sampleTestIds);

    /**
     * 获取检验分项关联设备列表
     * @param sampleTestId
     * @return
     */
    @GET("/msService/LIMSSample/sample/testDevice/getTestDevice")
    Flowable<BAP5CommonEntity<CommonListEntity<TestDeviceEntity>>> getTestDevice(@Query("sampleTestId") String sampleTestId);

    /**
     * 获取检验分项关联材料列表
     * @param sampleTestId
     * @return
     */
    @GET("/msService/LIMSSample/sample/testMaterial/getTestMaterial")
    Flowable<BAP5CommonEntity<CommonListEntity<TestMaterialEntity>>> getTestMaterial(@Query("sampleTestId") String sampleTestId);


    /**
     * 获取检验分项关联材料列表(按样品结果复核接口中，返回的bean类格式变了)
     * @param sampleTestId
     * @return
     */
    @GET("/msService/LIMSSample/sample/testMaterial/getTestMaterial")
    Flowable<BAP5CommonEntity<CommonListEntity<SampleTestMaterialEntity>>> getSampleTestMaterial(@Query("sampleTestId") String sampleTestId);


    /**
     * 获取单样品结果录入检验分项列表
     * @param id
     * @return
     */
    @GET("/msService/LIMSSample/sample/sampleCom/getSampleCom")
    Flowable<CommonBAP5ListEntity<InspectionSubEntity>> getSampleCom(@Query("sampleId") Long id);


    @GET("/msService/LIMSSample/sample/sampleTest/getStdGradeColumns")
    Flowable<SingleInspectionItemListEntity> getSampleInspectItem(@Query("sampleId") Long sampleId);

    /**
     * 获取样品检验分项的文件
     * @param id
     * @return
     */
    @GET("/inter-api/file-server/v1/file/download?entityCode=LIMSSample_5.0.0.0_sample&methodType=GET&url=/LIMSSample/baseService/workbench/download/validate&serverName=LIMS")
    Flowable<ResponseBody> getSampleInspectItemFile(@Query("id") String id);

    @FormUrlEncoded
    @POST("/msService/LIMSSample/sample/sampleTest/recordResultSubmit")
    Flowable<BAP5CommonEntity> recordResultSubmit(@FieldMap Map<String,Object> paramsMap);

    @GET("/inter-api/signature/buttonSignature/getSignatureEnabled")
    Flowable<BAP5CommonEntity<SampleSignatureEntity>> getSignatureEnabled(@Query("buttonCode") String buttonCode);

    @POST("/msService/baseService/workbench/uploadFile")
    @Multipart
    Flowable<BAP5CommonEntity<FileDataEntity>> bapUploadFile(@Part List<MultipartBody.Part> partList);

    /**
     * 获取样品台账列表数据
     * @param map
     * @return
     */
    @POST("/msService/LIMSSample/sample/sampleInfo/sampleInfoListPart-query")
    Flowable<CommonBAP5ListEntity<SampleAccountEntity>> getSampleAccountList(@Body Map<String, Object> map);

    /**
     * 扫描采样点id
     * @param pickSiteId
     * @return
     */
    @GET("/msService/LIMSSample/sample/sampleInfo/createSampleByPsId")
    Flowable<BAP5CommonEntity> scanSamplingPointId(@Query("pickSiteId") String pickSiteId);

    /**
     * 获取参照视图的解析文件
     * @param queryMap
     * @return
     */
    @POST("/msService/LIMSDC/analysisFile/analysisFile/analysisFileRef-query")
    Flowable<FileAnalyseListEntity> getAnalyseList(@Body Map<String,Object> queryMap);

    /**
     * :9410/lims-collection-web/ws/rs/analysisDataWS/getFormatDataByCollectCode?collectCode=30
     * @param url
     * @return
     */
    @POST
    Flowable<List<Map<String,Object>>> getFormatDataByCollectCode(@Url String url,@QueryMap Map<String, Object> map);

    /**
     * 获取样品参照列表
     * @param params
     * @return
     */
    @POST("/msService/LIMSSample/sample/sampleInfo/sampleInfoRefPart-query")
    Flowable<BAP5CommonEntity<CommonListEntity<SampleEntity>>> getSampleRefInfo(@Body Map<String,Object> params);

    /**
     * 获取样品参照的单位列表
     * @param params
     * @return
     */
    @POST("/msService/BaseSet/unit/unit/unitRef-query")
    Flowable<BAP5CommonEntity<CommonListEntity<BaseSystemBackEntity>>> getSampleUnitRefInfo(@Body Map<String,Object> params);

    /**
     * 获取仓库货位参照列表
     * @param params
     * @return
     */
    @POST("/msService/BaseSet/warehouse/storeSet/storeSetFilterRef-query")
    Flowable<BAP5CommonEntity<CommonListEntity<WareStoreEntity>>> getWareStoreRefInfo(@Body Map<String,Object> params);
    /**
     * 获取上一个接口所需的一个参数，本来是写死的，这是偷懒滴行为
     * @return
     */
    @GET("inter-api/systemconfig/v1/config/catalog/by/module?moduleCode=LIMSBasic&key=LIMSBasic.LIMSWareType&profilescAtive=")
    Flowable<BAP5CommonEntity> getLIMSWareType();

    /**
     * 获取样品结果复核中的样品列表
     * @param params
     * @return
     */
    @POST("/msService/LIMSSample/sample/sampleInfo/getPendingSample")
    Flowable<BAP5CommonEntity<CommonListEntity<SanpleResultCheckItemEntity>>> getSampleResultCheckList(@Query("pageType") String pageType, @Body Map<String,Object> params);
    /**
     * 获取样品结果复核中的样品检测项目列表
     * @param params
     * @return
     */
    @POST("/msService/LIMSSample/sample/sampleTest/findSampleTest")
    Flowable<BAP5CommonEntity<CommonListEntity<SampleResultCheckProjectEntity>>> getSampleResultCheckList(@Query("dealMode") String dealMode, @Query("sampleId") long sampleId, @Body Map<String,Object> params);


    /**
     * 获取样品结果复核中的某个检测项目中的具体项目信息
     * @return
     */
    @GET("/msService/LIMSSample/sample/sampleCom/getSampleCom")
    Flowable<BAP5CommonEntity<CommonListEntity<SampleResultCheckProjectDetailEntity>>> getSampleResultCheckList(@Query("sampleTestIds") long pageType);
}
