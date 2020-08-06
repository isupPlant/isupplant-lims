package com.supcon.mes.module_retention.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.BaseIntIdNameEntity;
import com.supcon.mes.middleware.model.bean.BaseldEntity;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;

/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */
public class RecordEntity extends BaseEntity {
    public Long id;
    public Long planDate;
    public Long realDate;
    public BaseLongIdNameEntity retId;
    public BaseIdValueEntity retPlanState;
}
