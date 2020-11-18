package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/11/16
 * class name
 */
@ContractFactory(entites = {CommonBAPListEntity.class})
public interface TestRequestNoAPI {
    void getTestRequestNo(int type, String pageNo, String query, Map<String, Object> params);
}
