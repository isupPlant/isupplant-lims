package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.module_lims.model.bean.InspectApplicationSubmitEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/10/27
 * class name
 */
@ContractFactory(entites = {PendingEntity.class})
public interface AddTestRequestAPI {
    void addTestRequestSave(String testType, Map<String, Object> map, InspectApplicationSubmitEntity inspectApplicationSubmitEntity);
}
