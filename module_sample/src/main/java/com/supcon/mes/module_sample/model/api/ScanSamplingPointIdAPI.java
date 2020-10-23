package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;


/**
 * author huodongsheng
 * on 2020/10/21
 * class name
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface ScanSamplingPointIdAPI {
    void scanSamplingPointId(String pickSiteId);
}
