package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/8
 * class name 样品检验报告接口
 */
@ContractFactory(entites = {SurveyReportListEntity.class})
public interface SampleSurveyReportAPI {

    void getSampleSurveyReportList( boolean isAll, int pageNo, Map<String, Object> params);
}
