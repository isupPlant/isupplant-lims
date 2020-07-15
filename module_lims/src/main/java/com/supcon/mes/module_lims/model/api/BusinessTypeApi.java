package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.BusinessTypeListEntity;

/**
 * author huodongsheng
 * on 2020/7/14
 * class name
 */
@ContractFactory(entites = {BusinessTypeListEntity.class})
public interface BusinessTypeApi {
    void getBusinessTypeList();
}
