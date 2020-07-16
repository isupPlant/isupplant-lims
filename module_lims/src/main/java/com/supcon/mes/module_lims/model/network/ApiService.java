package com.supcon.mes.module_lims.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.module_lims.model.bean.BusinessTypeListEntity;
import com.supcon.mes.module_lims.model.bean.IfUploadEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationDetailHeaderEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationListEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtListEntity;
import com.supcon.mes.module_lims.model.bean.MaterialReferenceListEntity;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceListEntity;
import com.supcon.mes.module_lims.model.bean.SampleInquiryListEntity;
import com.supcon.mes.module_lims.model.bean.SamplingPointListEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
}
