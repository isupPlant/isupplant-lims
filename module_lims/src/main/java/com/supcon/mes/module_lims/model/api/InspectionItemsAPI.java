package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.InspectionItemsListEntity;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name
 */
@ContractFactory(entites = {InspectionItemsListEntity.class,InspectionItemsListEntity.class, InspectionItemsListEntity.class})
public interface InspectionItemsAPI {
    void getInspectionItemsList(String stdVersionId);

    void getInspectComDataByInspectStdId(String inspectStdId);

    void getInspectComDataByInspectProjId(String inspectProjId);
}
