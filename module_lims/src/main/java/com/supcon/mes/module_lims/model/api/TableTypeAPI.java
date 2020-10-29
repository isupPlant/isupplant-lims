package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.TableTypeIdEntity;

/**
 * author huodongsheng
 * on 2020/10/28
 * class name
 */
@ContractFactory(entites = {TableTypeIdEntity.class})
public interface TableTypeAPI {
    void getTableTypeByCode(String code);
}
