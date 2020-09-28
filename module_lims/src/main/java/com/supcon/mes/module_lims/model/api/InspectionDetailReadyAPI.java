package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.BusinessTypeListEntity;
import com.supcon.mes.module_lims.model.bean.IfUploadEntity;

/**
 * author huodongsheng
 * on 2020/7/14
 * class name
 */
@ContractFactory(entites = {BusinessTypeListEntity.class, IfUploadEntity.class})
public interface InspectionDetailReadyAPI {
    void getBusinessTypeList(int type);

    void getIfUpload();
}
