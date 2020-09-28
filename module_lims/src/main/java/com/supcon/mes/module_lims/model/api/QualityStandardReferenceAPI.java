package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/10
 * class name
 */
@ContractFactory(entites = {QualityStandardReferenceListEntity.class})
public interface QualityStandardReferenceAPI {
    void getQualityStandardReferenceList(int pageNo,boolean hasStdVer, String id, Map<String, Object> params);
}
