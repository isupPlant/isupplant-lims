package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.SamplingPointListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/14
 * class name
 */
@ContractFactory(entites = {SamplingPointListEntity.class})
public interface SamplingPointAPI {
    void getSamplingPointList(int pageNo, Map<String, Object> map);
}
