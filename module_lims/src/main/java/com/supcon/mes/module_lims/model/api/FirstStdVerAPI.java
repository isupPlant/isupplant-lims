package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.FirstStdVerEntity;

/**
 * author huodongsheng
 * on 2020/11/17
 * class name
 */
@ContractFactory(entites = {FirstStdVerEntity.class})
public interface FirstStdVerAPI {
    void getFirstStdVer(String inspectId);
}
