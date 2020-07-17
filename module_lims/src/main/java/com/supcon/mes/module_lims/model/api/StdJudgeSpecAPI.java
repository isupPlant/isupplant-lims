package com.supcon.mes.module_lims.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;

import java.util.Map;

/**
 * Created by wanghaidong on 2020/7/17
 * Email:wanghaidong1@supcon.com
 */

@ContractFactory(entites = {
        StdJudgeSpecListEntity.class
})
public interface StdJudgeSpecAPI {
    void getReportComList(Map<String,Object> params);
}
