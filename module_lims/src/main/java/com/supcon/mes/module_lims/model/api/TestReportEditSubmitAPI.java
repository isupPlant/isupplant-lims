package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.module_lims.model.bean.TestReportSubmitEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/11/11
 * class name
 */
@ContractFactory(entites = {SubmitResultEntity.class})
public interface TestReportEditSubmitAPI {
    void submitInspectReport(String path, Map<String,Object> params, TestReportSubmitEntity reportSubmitEntity);
}
