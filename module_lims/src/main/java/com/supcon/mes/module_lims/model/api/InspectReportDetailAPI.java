package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.InspectHeadReportEntity;
import com.supcon.mes.module_lims.model.bean.InspectReportDetailListEntity;


/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = {InspectHeadReportEntity.class,InspectReportDetailListEntity.class})
public interface InspectReportDetailAPI {
    void getInspectHeadReport(long id);
    void getInspectReportDetails(int type,Long id);
}
