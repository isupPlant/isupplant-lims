package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_sample.model.bean.SampleSignatureEntity;

import java.util.Map;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = {BAP5CommonEntity.class, SampleSignatureEntity.class})
public interface SampleRecordResultSubmitAPI {
    void recordResultSubmit(Map<String,Object> paramsMap);
    void getSignatureEnabled(String buttonCode);
}
