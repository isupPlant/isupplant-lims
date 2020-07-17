package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;

/**
 * Created by wanghaidong on 2020/7/17
 * Email:wanghaidong1@supcon.com
 */
public class InspectHeadReportEntity extends BaseEntity {
    public Long id;
    public int version;
    public BaseIntIdNameEntity busiTypeId;
    public InspectIdEntity inspectId;
    public ProdIdEntity prodId;
    public String batchCode;
    public PendingEntity pending;
    public BaseIntIdNameEntity checkStaffId;
    public BaseIntIdNameEntity checkDeptId;
    public Long checkTime;
    public BaseIntIdNameEntity stdVerId;//质量标准
    public String checkResult;
    public String memoField;

}
