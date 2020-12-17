package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.BusiTypeIdEntity;

/**
 * author huodongsheng
 * on 2020/12/17
 * class name
 */
@ContractFactory(entites = BusiTypeIdEntity.class)
public interface BusiTypeByCodeAPI {
    void getBusiTypeByCode(String code);
}
