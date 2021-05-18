package com.supcon.mes.module_sample.model.api;

import com.app.annotation.apt.ContractFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by wanghaidong on 2021/2/3
 * Email:wanghaidong1@supcon.com
 * desc:
 */
@ContractFactory(entites = List.class)
public interface WareStoreRefAPI {
    void getWareStoreRefInfo(int pageNo,Map<String,Object> params);
    void getLIMSWareType();
}
