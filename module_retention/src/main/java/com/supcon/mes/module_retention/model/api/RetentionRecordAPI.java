package com.supcon.mes.module_retention.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = CommonBAP5ListEntity.class)
public interface RetentionRecordAPI {
    void getRetentionRecode(String data_dg,String datagridCode,Long id);
}
