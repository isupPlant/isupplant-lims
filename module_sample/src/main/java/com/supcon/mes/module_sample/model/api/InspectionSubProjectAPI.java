package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
@ContractFactory(entites = {CommonListEntity.class})
public interface InspectionSubProjectAPI {

    void getInspectionSubProjectList(String sampleTestIds);
}
