package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.module_lims.model.bean.InspectHeadReportEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportDetailListEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportSubmitEntity;

import java.util.Map;


/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = {
        InspectHeadReportEntity.class,
        InspectReportEntity.class,
        InspectReportDetailListEntity.class,
        SubmitResultEntity.class
})
public interface InspectReportDetailAPI {
    void getInspectHeadReport(long id);
    void getInspectReportByPending(long moduleId,Long pendingId);
    void getInspectReportDetails(int type,Long id);
    void submitInspectReport(String path, Map<String,Object> params, InspectReportSubmitEntity reportSubmitEntity);
}
