package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.module_sample.model.bean.SingleInspectionItemListEntity;

/**
 * Created by wanghaidong on 2020/8/14
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = {CommonBAP5ListEntity.class, SingleInspectionItemListEntity.class})
public interface SingleSampleResultInputAPI {
    void getSampleCom(Long sampleId);
    void getSampleInspectItem(Long sampleId);
}
