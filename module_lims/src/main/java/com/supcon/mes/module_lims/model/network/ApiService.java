package com.supcon.mes.module_lims.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.module_lims.model.bean.AvailableStdEntity;

import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;

import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.BusiTypeIdEntity;
import com.supcon.mes.module_lims.model.bean.BusinessTypeListEntity;
import com.supcon.mes.module_lims.model.bean.ConfigEntity;
import com.supcon.mes.module_lims.model.bean.DeviceReferenceEntity;
import com.supcon.mes.module_lims.model.bean.DeviceTypeReferenceEntity;
import com.supcon.mes.module_lims.model.bean.FirstStdVerEntity;
import com.supcon.mes.module_lims.model.bean.IfUploadEntity;
import com.supcon.mes.module_lims.model.bean.InspectApplicationSubmitEntity;
import com.supcon.mes.module_lims.model.bean.InspectHeadReportEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportDetailListEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportSubmitEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationDetailHeaderEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationListEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtListEntity;
import com.supcon.mes.module_lims.model.bean.InspectionItemsListEntity;
import com.supcon.mes.module_lims.model.bean.MaterialReferenceListEntity;
import com.supcon.mes.module_lims.model.bean.PleaseCheckSchemeListEntity;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceListEntity;
import com.supcon.mes.module_lims.model.bean.QualityStdConclusionEntity;
import com.supcon.mes.module_lims.model.bean.SampleInquiryListEntity;
import com.supcon.mes.module_lims.model.bean.SampleMaterialEntity;
import com.supcon.mes.module_lims.model.bean.SamplingPointListEntity;

import com.supcon.mes.module_lims.model.bean.SerialDeviceEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdListEntity;
import com.supcon.mes.module_lims.model.bean.StdVerIdEntity;
import com.supcon.mes.module_lims.model.bean.SupplierReferenceListEntity;

import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;

import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;
import com.supcon.mes.module_lims.model.bean.TableTypeIdEntity;
import com.supcon.mes.module_lims.model.bean.TemporaryQualityStandardEntity;
import com.supcon.mes.module_lims.model.bean.TestNumEntity;
import com.supcon.mes.module_lims.model.bean.TestReportEditHeadEntity;
import com.supcon.mes.module_lims.model.bean.TestReportEditPtEntity;
import com.supcon.mes.module_lims.model.bean.TestReportSubmitEntity;
import com.supcon.mes.module_lims.model.bean.TestRequestNoEntity;

import java.util.Map;
import java.util.Objects;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


import retrofit2.http.Url;


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


    /**
     * 样品收样取样提交接口
     * @param map
     * @return
     */
    @POST("/msService/LIMSSample/sample/sampleInfo/dealSample")
    Flowable<BAP5CommonEntity> sampleSubmit(@Body Map<String, Object> map);


    /**
     * 物料参照列表接口
     * @param map
     * @return
     */
    @POST("/msService/BaseSet/material/material/materialRef-query")
    Flowable<MaterialReferenceListEntity> getMaterialReference(@Body Map<String, Object> map);

    /**
     * 业务类型参照列表接口
     * @param map
     * @return
     */
    @POST("/msService/QCS/tableType/busiType/busiTypeRef-query")
    Flowable<BusinessTypeListEntity> getBusinessTypeList(@Body Map<String, Object> map);

    /**
     * 质量标准列表接口
     * @param map
     * @return
     */
    @POST("/msService/LIMSBasic/qualityStd/stdVersion/qualityStdVerRef-query")
    Flowable<QualityStandardReferenceListEntity> getQualityStandard(@Body Map<String, Object> map);

    /**
     * 采样点参照列表接口
     * @param map
     * @return
     */
    @POST("/msService/LIMSBasic/pickSite/pickSite/pickSiteRefPart-query")
    Flowable<SamplingPointListEntity> getSamplingPointList(@Body Map<String, Object> map);


    /**
     * 请检方案参照列表接口
     * @param map
     * @return
     */
    @POST("/msService/LIMSBasic/inspectProj/inspectProj/inspectProjRef-query")
    Flowable<PleaseCheckSchemeListEntity> getPleaseCheckSchemeList(@Body Map<String, Object> map);


    /**
     * 供应商参照列表接口
     * @param map
     * @return
     */
    @POST("/msService/BaseSet/cooperate/cooperate/cmcPartRef-query")
    Flowable<SupplierReferenceListEntity> getSupplierReferenceList(@Body Map<String, Object> map);

    /**
     * 质量标准所对应检验项目接口(编辑) 在没有请检方案的情况下 获取检验项目的接口
     * @param map
     * @return
     */
    @POST("/msService/QCS/inspect/inspectCom/getInspectComDataByStdVerId")
    Flowable<InspectionItemsListEntity> getInspectionItemsList(@Body Map<String, Object> map);

    /**
     * 在有请检方案的情况下获取检验项目的接口
     * @param map
     * @return
     */
    @POST("/msService/QCS/inspect/inspectCom/getInspectComDataByInspectProjId")
    Flowable<InspectionItemsListEntity> getInspectComDataByInspectProjId(@Body Map<String, Object> map);

    /**
     * 质量标准所对应检验项目接口(查看)
     * @param map
     * @return
     */
    @POST("/msService/QCS/inspect/inspectCom/getInspectComDataByInspectStdId")
    Flowable<InspectionItemsListEntity> getInspectComDataByInspectStdId(@Body Map<String, Object> map);

    /**
     * 检验申请详情 获取表头数据
     * @param id
     * @param paddingId
     * @return
     */
    @GET("/msService/QCS/inspect/inspect/data/{id}")
    Flowable<BAP5CommonEntity<InspectionApplicationDetailHeaderEntity>> getInspectionDetailHeaderData(@Path("id") String id,
                                                                                                      @Query("pendingId") String paddingId);


    /**
     * 检验申请详情 获取表体数据
     * @param dg
     * @param datagridCode
     * @param id
     * @return
     */
    @POST("/msService/QCS/inspect/inspect/{dg}")
    Flowable<InspectionDetailPtListEntity> getInspectionDetailPtData(@Path("dg") String dg,
                                                                  @Query("datagridCode") String datagridCode,
                                                                  @Query("id") String id);
    /**
     * 查看是否上载
     * @param moduleCode
     * @return
     */
    @GET("/msService/LIMSBasic/utils/utils/judgeModuleExistsByCode")
    Flowable<BAP5CommonEntity<IfUploadEntity>> getIfUpload(@Query("moduleCode")String moduleCode);



    @POST("/msService/LIMSBasic/analySample/analyProdStd/getAvailableStdVersByProductId")
    Flowable<BAP5CommonEntity<AvailableStdEntity>> getAvailableStdId(@QueryMap Map<String ,Object> map);


    /**
     * 通过物料id查询默认质量标准
     * @param map
     * @return
     */
    @POST("/msService/LIMSBasic/analySample/analySample/getDefaultPropertyByProductId")
    Flowable<BAP5CommonEntity<TemporaryQualityStandardEntity>> getDefaultStandardById(@Body Map<String, Object> map);

    /**
     * 根据质量标准id 获取对应的检验项目
     * @param stdVerId
     * @return
     */
    @GET("/msService/LIMSBasic/qualityStd/stdVerCom/getStdVerComsByStdVerId")
    Flowable<StdVerComIdListEntity> getDefaultItems(@Query("stdVerId") String stdVerId);

    /**
     * 根据质量标准id 获取对应的请检方案
     * @param map
     * @return
     */
    @POST("/msService/LIMSBasic/inspectProj/inspectProj/getDefaultInspProjByStdVerId")
    Flowable<BAP5CommonEntity<InspectionDetailPtEntity>> getDefaultInspProjByStdVerId(@QueryMap Map<String, Object> map);

    /**
     * 获取质量检验中检验报告单pt数据
     * @param url
     * @param params
     * @return
     */
    @POST
    Flowable<InspectReportDetailListEntity>  getInspectReportDetails(@Url String url, @Body Map<String,Object> params);

    /**
     * 通过id获取质量检验中检验报告单详情数据
     * @param id
     * @return
     */
    @GET("/msService/QCS/inspectReport/inspectReport/data/{id}")
    Flowable<BAP5CommonEntity<InspectHeadReportEntity>> getInspectHeadReport(@Path("id") Long id);

    /**
     * 获取质量检验中检验报告单pt的质量标准
     * @param params
     * @return
     */
    @POST("/msService/QCS/inspectReport/inspectReport/getReportComList")
    Flowable<StdJudgeSpecListEntity> getReportComList(@Body Map<String,Object> params);

    /**
     * 通过待办获取质量检验中检验报告单详情数据
     * @param moduleId
     * @param pendingId
     * @return
     */
    @GET("/msService/QCS/inspectReport/inspectReport/data/{moduleId}")
    Flowable<InspectReportEntity> getInspectReportByPending(@Path("moduleId") long moduleId,@Query("pendingId") Long pendingId);

    /**
     * 提交质量检验中工作流
     * @param path
     * @param params
     * @param reportSubmitEntity
     * @return
     */
    @POST("/msService/QCS/inspectReport/inspectReport/{inspReportView}/submit")
    Flowable<SubmitResultEntity> submitInspectReport(@Path("inspReportView") String path, @QueryMap Map<String,Object> params, @Body InspectReportSubmitEntity reportSubmitEntity);

    /**
     * 报告单编辑工作流
     * @param path
     * @param params
     * @param reportSubmitEntity
     * @return
     */
    @POST("/msService/QCS/inspectReport/inspectReport/{inspReportView}/submit")
    Flowable<SubmitResultEntity> setTestReportEditSubmit(@Path("inspReportView") String path, @QueryMap Map<String,Object> params, @Body TestReportSubmitEntity reportSubmitEntity);
    /**
     * 检验申请详情提交接口
     * @param path
     * @param params
     * @param inspectApplicationSubmitEntity
     * @return
     */
    @POST("/msService/QCS/inspect/inspect/{documentType}/submit")
    Flowable<BAP5CommonEntity> submitInspectApplication(@Path("documentType") String path, @QueryMap Map<String, Object> params, @Body InspectApplicationSubmitEntity inspectApplicationSubmitEntity);

    /**
     * 通过待办获取检验申请当前列表item的数据
     * @param moduleId
     * @param pendingId
     * @return
     */
    @GET("/msService/QCS/inspect/inspect/data/{moduleId}")
    Flowable<BAP5CommonEntity<InspectionApplicationEntity>> getInspectionApplicationByPending(@Path("moduleId") Long moduleId,
                                                                            @Query("pendingId") Long pendingId);

    /**
     * 新增检验申请保存接口
     * @param testType
     * @param params
     * @return
     */
    @POST("/msService/QCS/inspect/inspect/{testType}/submit")
    Flowable<BAP5CommonEntity<PendingEntity>> addTestRequestSave(@Path("testType") String testType,
                                                                 @QueryMap Map<String, Object> params,
                                                                 @Body InspectApplicationSubmitEntity inspectApplicationSubmitEntity);

    /**
     * 获取设备类型参照接口
     * @param map
     * @return
     */
    @POST("/msService/BaseSet/eamType/eamType/eamTypeRefPart-query")
    Flowable<BAP5CommonEntity<CommonListEntity<DeviceTypeReferenceEntity>>> getDeviceTypeReference(@Body Map<String, Object> map);

    /**
     * 获取设备参照列表接口
     * @param map
     * @return
     */
    @POST("/msService/BaseSet/eamInfo/eamInfo/eamInfoRefPart-query")
    Flowable<BAP5CommonEntity<CommonListEntity<DeviceReferenceEntity>>> getDeviceReference(@Body Map<String, Object> map);


    @POST("/msService/LIMSMaterial/mATInfo/matInfo/matInfoRef-query")
    Flowable<BAP5CommonEntity<CommonListEntity<SampleMaterialEntity>>> getSampleMaterialReference(@Body Map<String, Object> map);

    /**
     * 获取单据类型
     * @param code
     * @return
     */
    @GET("/msService/QCS/tableType/tableType/getTableTypeByCode")
    Flowable<BAP5CommonEntity<TableTypeIdEntity>> getTableTypeByCode (@Query("code") String code);

    /**
     * 获取检验报告（编辑状态下）
     * @return
     */
    @GET("/msService/QCS/inspectReport/inspectReport/data/{id}")
    Flowable<BAP5CommonEntity<TestReportEditHeadEntity>> getTestReportEdit(@Path("id") String id);

    /**
     * 通过待办获取检验申请当前列表item的数据
     * @param moduleId
     * @param pendingId
     * @return
     */
    @GET("/msService/QCS/inspectReport/inspectReport/data/{moduleId}")
    Flowable<BAP5CommonEntity<TestReportEditHeadEntity>> getTestReportEdit(@Path("moduleId") String moduleId, @Query("pendingId") String pendingId);
    /**
     * 根据质量标准id 获取结论
     * @param stdVersionId
     * @return
     */
    @GET("/msService/LIMSBasic/qualityStd/stdVerGrade/getStdVerGradesByStdVerId")
    Flowable<BAP5CommonListEntity<QualityStdConclusionEntity>> getStdVerGradesByStdVerId(@Query("stdVersionId") String stdVersionId);

    /**
     * 获取检验项目参照数据
     * @param map
     * @return
     */
    @POST("/msService/LIMSBasic/qualityStd/stdVerCom/stdVerComRef-query")
    Flowable<BAP5CommonEntity<CommonBAPListEntity<StdVerComIdEntity>>> getStdVerComList(@Body Map<String, Object> map);

    @POST("/msService/QCS/inspect/inspect/{query}")
    Flowable<BAP5CommonEntity<CommonBAPListEntity<TestRequestNoEntity>>> getTestRequestNo(@Path("query") String query,
                                                                                          @Body Map<String, Object> map);

    /**
     *
     * @param inspectId
     * @return
     */
    @POST("/msService/QCS/inspect/inspectStd/getFirstStdVerByInspectId")
    Flowable<BAP5CommonEntity<FirstStdVerEntity>> getFirstStdVerByInspectId(@Query("inspectId") String inspectId);

    @GET("/msService/QCS/inspect/inspect/data/{id}")
    Flowable<BAP5CommonEntity<TestNumEntity>> getTestNum(@Path("id") String id, @QueryMap Map<String, Object> map);

    @GET("msService/QCS/tableType/busiType/getBusiTypeByCode")
    Flowable<BAP5CommonEntity<BusiTypeIdEntity>> getBusiTypeByCode(@Query("code") String code);
    /**
     * 获取串口采集设备参照
     * @param map
     * @return
     */
    @POST("/msService/BaseSet/eamInfo/eamInfo/serialRef-query")
    Flowable<BAP5CommonEntity<CommonBAPListEntity<SerialDeviceEntity>>> getSerialDeviceRef(@Body Map<String, Object> map);

    /**
     * 根据检验申请的id获取质量标准
     * @param inspectId
     * @return
     */
    @POST("/msService/QCS/inspect/inspectStd/getStdVerByInspectId")
    Flowable<BAP5CommonListEntity<FirstStdVerEntity>> getStdVerByInspectId(@Query("inspectId") String inspectId);
    /**
     * 根据检验申请的id获取质量标准
     * @return
     */
    @GET("/msService/LIMSSample/sample/sampleInfo/getDBType")
    Flowable<BAP5CommonEntity> getDBType();
}
