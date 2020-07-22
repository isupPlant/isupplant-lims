package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.SupplierReferenceListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/20
 * class name
 */
@ContractFactory(entites = {SupplierReferenceListEntity.class})
public interface SupplierReferenceApi {
    void getSupplierReferenceList(int pageNo, Map<String,Object> params);
}
