package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.TestNumEntity;

/**
 * author huodongsheng
 * on 2020/11/17
 * class name
 */
@ContractFactory(entites = {TestNumEntity.class})
public interface TestNumAPI {
    void getTestNum(String id);
}
