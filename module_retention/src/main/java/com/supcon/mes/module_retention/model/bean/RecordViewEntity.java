package com.supcon.mes.module_retention.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */
public class RecordViewEntity extends BaseEntity {
    public Long id;
    public String memoField;
    public String observeItem;
    public String observeResult;
    public String observeValue;
}
