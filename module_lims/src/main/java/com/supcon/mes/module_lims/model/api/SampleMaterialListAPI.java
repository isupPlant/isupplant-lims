package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */
@ContractFactory(entites = {CommonListEntity.class})
public interface SampleMaterialListAPI {
    void getSampleMaterialReference(int pageNo, Map<String, Object> params, String matInfoCodeList);
}
