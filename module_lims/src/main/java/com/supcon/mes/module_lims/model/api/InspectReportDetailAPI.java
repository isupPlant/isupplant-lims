package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.InspectReportDetailListEntity;

import java.util.Map;


/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = {InspectReportDetailListEntity.class})
public interface InspectReportDetailAPI {
    void getInspectReportDetails(int type,Long id);
}
