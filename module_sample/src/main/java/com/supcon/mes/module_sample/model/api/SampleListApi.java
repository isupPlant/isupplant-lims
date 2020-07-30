package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/29
 * class name
 */
@ContractFactory(entites = {CommonListEntity.class})
public interface SampleListApi {
    void getSampleList(int pageNo, Map<String, Object> params);
}
