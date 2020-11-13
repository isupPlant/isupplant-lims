package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;

/**
 * Created by wanghaidong on 2020/7/16
 * Email:wanghaidong1@supcon.com
 */
public class InspectReportDetailEntity extends BaseEntity {
    public Long id;
    public String checkResult;
    public String dispValue;
    public BaseIntIdNameEntity reportId;
    public String reportName;
    public String unitName;
    public int version;
    public String testResult;
    public String resultKey;
    public BaseIdValueEntity valueKind;

    public int getTypeView(){
        return 1;
    }



}
