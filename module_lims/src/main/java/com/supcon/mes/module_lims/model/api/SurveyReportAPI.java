package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.SurveyReportListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name
 */
@ContractFactory(entites = {SurveyReportListEntity.class})
public interface SurveyReportAPI {
    void getSurveyReportList(String type, boolean isAll, int pageNo, Map<String, Object> params);
}
