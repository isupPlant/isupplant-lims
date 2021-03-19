package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;

/**
 * author huodongsheng
 * on 2021/3/15
 * class name
 */
@ContractFactory(entites = {BAP5CommonListEntity.class})
public interface StdVerByInspectIdAPI {
    void getStdVerByInspectId(String inspectId);
}
