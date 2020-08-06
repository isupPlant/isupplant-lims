package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;

/**
 * author huodongsheng
 * on 2020/8/4
 * class name
 */
@ContractFactory(entites = {BAP5CommonListEntity.class})
public interface InspectionSubProjectColumnApi {
    void getInspectionSubProjectColumn(String sampleTestIds);
}
