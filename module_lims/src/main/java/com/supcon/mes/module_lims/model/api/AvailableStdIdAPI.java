package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.AvailableStdEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdListEntity;
import com.supcon.mes.module_lims.model.bean.TemporaryQualityStandardEntity;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name
 */
@ContractFactory(entites = {AvailableStdEntity.class, TemporaryQualityStandardEntity.class, StdVerComIdListEntity.class, InspectionDetailPtEntity.class})
public interface AvailableStdIdAPI {
    void getAvailableStdId(String productId);

    void getDefaultStandardById(String productId);

    void getDefaultItems(String stdVerId);

    void getDefaultInspProjByStdVerId(InspectionDetailPtEntity entity, String stdVerId);
}
