package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by yaobing on 2021/2/3
 */
@ContractFactory(entites = List.class)
public interface SampleResultCheckAPI {
    void getPendingSample(String pageType,Map<String,Object> params);
}
