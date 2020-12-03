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
    public BaseLongIdNameEntity busiTypeId;
    public InspectIdEntity inspectId;
    public ProdIdEntity prodId;
    public String batchCode;
    public PendingEntity pending;
    public BaseLongIdNameEntity checkStaffId;
    public BaseLongIdNameEntity checkDeptId;
    public Long checkTime;
    public StdVerIdEntity stdVerId;//质量标准
    public String checkResult;
    public String memoField;

}
