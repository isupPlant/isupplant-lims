package com.supcon.mes.module_retention.model.api;

import com.app.annotation.apt.ContractFactory;

import java.util.List;
import java.util.Map;

/**
 * author huodongsheng
 * on 2021/3/5
 * class name
 */
@ContractFactory(entites = List.class)
public interface RetentionSampleRefAPI {
    void getSampleRefInfo(Map<String,Object> params);
}
