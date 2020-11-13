package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.TestReportEditHeadEntity;
/**
 * author huodongsheng
 * on 2020/11/4
 * class name
 */
@ContractFactory(entites = {TestReportEditHeadEntity.class})
public interface TestReportEditAPI {
    void getTestReportEdit(String id, String pendingId);
}
