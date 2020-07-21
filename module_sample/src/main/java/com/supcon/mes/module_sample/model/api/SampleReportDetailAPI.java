package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportSubmitEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;
import com.supcon.mes.module_sample.model.bean.SampleReportSubmitEntity;

import java.util.Map;

/**
 * Created by wanghaidong on 2020/7/21
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = {
        SurveyReportEntity.class,
        StdJudgeSpecListEntity.class,
        SubmitResultEntity.class
})
public interface SampleReportDetailAPI {
    void getSampleReport(Long id);
    void getReportComList(Map<String,Object> params);
    void submitSampleReport(String path, Map<String,Object> params, SampleReportSubmitEntity reportSubmitEntity);
}
