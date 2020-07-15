package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.MaterialReferenceListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/9
 * class name
 */
@ContractFactory(entites = {MaterialReferenceListEntity.class})
public interface MaterialReferenceApi {
    void getMaterialReferenceList(int pageNo, Map<String, Object> params);
}
