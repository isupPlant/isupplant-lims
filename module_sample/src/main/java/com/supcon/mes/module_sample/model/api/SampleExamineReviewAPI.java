package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;

import java.util.Map;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = {BAP5CommonEntity.class, BAP5CommonEntity.class, BAP5CommonEntity.class})
public interface SampleExamineReviewAPI {
    void recordResultReview(Map<String,Object> paramsMap);
}
