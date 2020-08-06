package com.supcon.mes.module_retention.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_retention.model.bean.RetentionListEntity;


import java.util.Map;

/**
 * Created by wanghaidong on 2020/8/5
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = RetentionListEntity.class)
public interface RetentionAPI {
    void getRetentionList(int page, boolean all, Map<String,Object> params);
}
