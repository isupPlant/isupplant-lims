package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.IfUploadEntity;
import com.supcon.mes.module_lims.model.bean.InspectionApplicationDetailHeaderEntity;
import com.supcon.mes.module_lims.model.bean.InspectionDetailPtListEntity;

/**
 * author huodongsheng
 * on 2020/7/13
 * class name
 */
@ContractFactory(entites = {InspectionApplicationDetailHeaderEntity.class, InspectionDetailPtListEntity.class, IfUploadEntity.class})
public interface InspectionApplicationDetailApi {
    void getInspectionDetailHeaderData(String id, String paddingId);

    void getInspectionDetailPtData(String type, String level, String id);

    void getIfUpload();
}
