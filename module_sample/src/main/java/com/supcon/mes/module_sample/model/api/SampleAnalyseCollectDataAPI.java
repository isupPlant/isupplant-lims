package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;

import java.util.List;

/**
 * Created by wanghaidong on 2021/1/18
 * Email:wanghaidong1@supcon.com
 * desc:
 */
@ContractFactory(entites = List.class)
public interface SampleAnalyseCollectDataAPI {
    void getFormatDataByCollectCode(String url);
}
