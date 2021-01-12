package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2021/1/11
 * class name
 */
@ContractFactory(entites = {CommonBAPListEntity.class})
public interface SerialDeviceRefAPI {
    void getSerialDeviceRef(int pageNo, Map<String, Object> map);
}
