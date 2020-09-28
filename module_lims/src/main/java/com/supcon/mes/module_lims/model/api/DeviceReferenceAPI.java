package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/8/13
 * class name
 */
@ContractFactory(entites = {CommonListEntity.class})
public interface DeviceReferenceAPI {
    void getDeviceReferenceList(int pageNo, String eamClassId, Map<String, Object> params);
}
