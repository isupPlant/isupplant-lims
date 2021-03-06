package com.supcon.mes.module_retention.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.module_lims.model.bean.SampleComeEntity;
import com.supcon.mes.module_retention.model.bean.RecodeListEntity;
import com.supcon.mes.module_retention.model.bean.RecordEntity;
import com.supcon.mes.module_retention.model.bean.RecordViewEntity;
import com.supcon.mes.module_retention.model.bean.RetentionEntity;
import com.supcon.mes.module_retention.model.bean.RetentionListEntity;
import com.supcon.mes.module_retention.model.bean.RetentionSubmitEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */
@ApiFactory(name = "RetentionHttpClient")
public interface RetentionNetworkAPI {

    @POST("/msService/LIMSRetain/retention/retention/{retentionList}")
    Flowable<RetentionListEntity> getRetentionList(@Path("retentionList") String retentionList, @Body Map<String,Object> params);

    @GET("/msService/LIMSRetain/retention/retention/data/{id}")
    Flowable<BAP5CommonEntity<RetentionEntity>> getRetentionDetailById(@Path("id") Long id);

    @GET("/msService/LIMSRetain/retention/retention/data/{moduleId}")
    Flowable<BAP5CommonEntity<RetentionEntity>> getRetentionDetailById(@Path("moduleId") long moduleId,@Query("pendingId") Long pendingId);

    @POST("/msService/LIMSRetain/retention/retention/data-dg1592198735798?datagridCode=LIMSRetain_5.0.4.1_retention_retentionViewdg1592198735798")
    Flowable<RecodeListEntity> getRecord(@Query("id") Long id);

    @POST("/msService/LIMSRetain/retention/retention/{retentionView}/submit")
    Flowable<SubmitResultEntity> submitRetention(@Path("retentionView") String path, @QueryMap Map<String,Object> params, @Body RetentionSubmitEntity retentionSubmit);

    @POST("/msService/LIMSRetain/retention/retPlan/data-dg1592896739534?datagridCode=LIMSRetain_5.0.4.1_retention_retRecordViewdg1592896739534")
    Flowable<CommonBAP5ListEntity<RecordViewEntity>> getRetentionRecode(@Query("id") Long id,@Body Map<String,Object> paramsMap);


}
