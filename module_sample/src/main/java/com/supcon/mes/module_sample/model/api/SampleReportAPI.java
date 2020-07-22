package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.SurveyReportEntity;


/**
 * Created by wanghaidong on 2020/7/21
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = {
        SurveyReportEntity.class,
})
public interface SampleReportAPI {
    void getSampleReportByPending(long moduleId, Long pendingId);
}
