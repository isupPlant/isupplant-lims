package com.supcon.mes.module_retention.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.SubmitResultEntity;
import com.supcon.mes.module_retention.model.bean.RecodeListEntity;
import com.supcon.mes.module_retention.model.bean.RecordEntity;
import com.supcon.mes.module_retention.model.bean.RetentionEntity;
import com.supcon.mes.module_retention.model.bean.RetentionSubmitEntity;

import java.util.Map;

/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */

@ContractFactory(entites = {RetentionEntity.class, RecodeListEntity.class, SubmitResultEntity.class})
public interface RetentionDetailAPI {
    void getRetentionDetailById(Long id,Long pendingId);
    void getRecord(Long id);
    void submitRetention(String path, Map<String, Object> params, RetentionSubmitEntity retentionSubmit);
}
