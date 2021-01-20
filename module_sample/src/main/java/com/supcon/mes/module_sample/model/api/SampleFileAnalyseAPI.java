package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_sample.model.bean.FileAnalyseListEntity;

import java.util.Map;

/**
 * Created by wanghaidong on 2021/1/18
 * Email:wanghaidong1@supcon.com
 * desc:
 */
@ContractFactory(entites = {FileAnalyseListEntity.class})
public interface SampleFileAnalyseAPI {
    void getAnalyseList(int pageNo,Map<String,Object> queryMap);
}
