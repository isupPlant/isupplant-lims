package com.supcon.mes.module_retention.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_retention.model.bean.RecodeListEntity;
import com.supcon.mes.module_retention.model.bean.RecordEntity;
import com.supcon.mes.module_retention.model.bean.RetentionEntity;

/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */

@ContractFactory(entites = {RetentionEntity.class, RecodeListEntity.class})
public interface RetentionDetailAPI {
    void getRetentionDetailById(Long id);
    void getRecord(Long id);
}
